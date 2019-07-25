package ru.boksh.moneytransfer.model;

import java.util.Optional;
import java.util.function.Function;

public interface AccountStorage {

  Optional<Account> getAccount(int accountId);

  <T> T executeWithAccountLock(int accountId, Function<Account, T> operation);

  void setAccountMoney(int accountId, int newMoneyAmount);

  Account createAccount(int initialMoneyAmount);

}
