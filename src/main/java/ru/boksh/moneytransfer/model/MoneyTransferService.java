package ru.boksh.moneytransfer.model;

import javax.inject.Inject;

public class MoneyTransferService {

  private AccountStorage accountStorage;

  @Inject
  public MoneyTransferService(AccountStorage accountStorage) {
    this.accountStorage = accountStorage;
  }

  public MoneyTransferResult performMoneyTransfer(int fromAccountId, int toAccountId, int moneyAmount) {
    if (fromAccountId == toAccountId) {
      return MoneyTransferResult.FAIL_SELF_TRANSFER_IS_NOT_SUPPORTED;
    }

    if (moneyAmount <= 0) {
      return MoneyTransferResult.FAIL_NON_POSITIVE_MONEY_TRANSFER_NOT_SUPPORTED_YET;
    }

    if (accountStorage.getAccount(fromAccountId).isEmpty()) {
      return MoneyTransferResult.FAIL_FROM_ACCOUNT_NOT_EXIST;
    }

    if (accountStorage.getAccount(toAccountId).isEmpty()) {
      return MoneyTransferResult.FAIL_TO_ACCOUNT_NOT_EXIST;
    }

    return accountStorage.executeWithAccountLock(Math.min(fromAccountId, toAccountId), firstLockedAccount ->
        accountStorage.executeWithAccountLock(Math.max(fromAccountId, toAccountId), secondLockedAccount -> {
          final AccountView fromAccount;
          final AccountView toAccount;
          if (fromAccountId == firstLockedAccount.getAccountId()) {
            fromAccount = firstLockedAccount;
            toAccount = secondLockedAccount;
          } else {
            fromAccount = secondLockedAccount;
            toAccount = firstLockedAccount;
          }

          if (fromAccount.getMoneyAmount() < moneyAmount) {
            return MoneyTransferResult.FAIL_NOT_ENOUGH_MONEY;
          }

          accountStorage.setAccountMoney(fromAccount.getAccountId(), fromAccount.getMoneyAmount() - moneyAmount);
          accountStorage.setAccountMoney(toAccount.getAccountId(), toAccount.getMoneyAmount() + moneyAmount);
          return MoneyTransferResult.SUCCESS;
        })
    );
  }

}
