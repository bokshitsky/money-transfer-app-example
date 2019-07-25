package ru.boksh.moneytransfer.model;

public enum MoneyTransferResult {
  SUCCESS,
  FAIL_SELF_TRANSFER_IS_NOT_SUPPORTED,
  FAIL_FROM_ACCOUNT_NOT_EXISTS,
  FAIL_TO_ACCOUNT_NOT_EXISTS,
  FAIL_NOT_ENOUGH_MONEY,
  FAIL_NON_POSITIVE_MONEY_NOT_SUPPORTED_YET
}