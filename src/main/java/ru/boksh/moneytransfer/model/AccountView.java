package ru.boksh.moneytransfer.model;

public class AccountView {

  private final int accountId;
  private final int moneyAmount;

  public AccountView(int accountId, int moneyAmount) {
    this.accountId = accountId;
    this.moneyAmount = moneyAmount;
  }

  public int getAccountId() {
    return accountId;
  }

  public int getMoneyAmount() {
    return moneyAmount;
  }

}
