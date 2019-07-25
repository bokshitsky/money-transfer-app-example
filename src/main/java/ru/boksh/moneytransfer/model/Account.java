package ru.boksh.moneytransfer.model;

public class Account {

  private int accountId;
  private int moneyAmount;

  public Account(int accountId, int moneyAmount) {
    this.accountId = accountId;
    this.moneyAmount = moneyAmount;
  }

  public int getAccountId() {
    return accountId;
  }

  public int getMoneyAmount() {
    return moneyAmount;
  }

  public Account setMoneyAmount(int moneyAmount) {
    this.moneyAmount = moneyAmount;
    return this;
  }
}
