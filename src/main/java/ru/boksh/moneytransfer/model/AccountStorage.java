package ru.boksh.moneytransfer.model;

import java.util.Optional;
import java.util.function.Function;

public interface AccountStorage {

  Optional<AccountView> getAccount(int accountId);

  <T> T executeWithAccountLock(int accountId, Function<AccountView, T> operation);

  void setAccountMoney(int accountId, int newMoneyAmount);

  AccountView createAccount(int initialMoneyAmount);

}
