package ru.boksh.moneytransfer.model;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class SimpleInMemoryAccountStorage implements AccountStorage {

  private static Map<Integer, Account> ACCOUNTS = new ConcurrentHashMap<>();
  private static AtomicInteger ACCOUNT_ID_SEQUENCE = new AtomicInteger(0);

  @Override
  public Optional<Account> getAccount(int accountId) {
    // return new object to save in-memory stored objects from corruption
    return Optional.ofNullable(ACCOUNTS.get(accountId)).map(account -> new Account(account.getAccountId(), account.getMoneyAmount()));
  }

  @Override
  public <T> T executeWithAccountLock(int accountId, Function<Account, T> operation) {
    Account account = ACCOUNTS.get(accountId);

    // Synchronization on local variable, assumes account can not be deleted, account object is mutable
    // and new account object never recreated in storage for accountId
    synchronized (account) {
      return operation.apply(new Account(account.getAccountId(), account.getMoneyAmount()));
    }
  }

  @Override
  public void setAccountMoney(int accountId, int newMoneyAmount) {
    // Assumes account can not be deleted, so always exists
    ACCOUNTS.get(accountId).setMoneyAmount(newMoneyAmount);
  }

  @Override
  public Account createAccount(int initialMoneyAmount) {
    int newAccountId = ACCOUNT_ID_SEQUENCE.getAndIncrement();
    ACCOUNTS.put(newAccountId, new Account(newAccountId, initialMoneyAmount));
    // return new object to save in-memory stored objects from corruption
    return new Account(newAccountId, initialMoneyAmount);
  }
}
