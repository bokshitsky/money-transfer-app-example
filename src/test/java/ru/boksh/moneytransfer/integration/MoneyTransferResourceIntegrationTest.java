package ru.boksh.moneytransfer.integration;

import org.eclipse.jetty.server.Response;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import ru.boksh.moneytransfer.ResponseOrError;
import ru.boksh.moneytransfer.http.dtos.ErrorDto;

public class MoneyTransferResourceIntegrationTest extends AppIntegrationTestBase {

  @Test
  public void testTransferToNonExistingAccountMustFail() {
    Integer accountFromId = appTestClient.createAccount(TEST_MONEY_AMOUNT).getEntity().getId();
    ResponseOrError<Object, ErrorDto> errorResponse = appTestClient.performMoneyTransfer(accountFromId, NON_EXISTING_ACCOUNT_ID, TEST_MONEY_AMOUNT);
    assertFalse(errorResponse.isSuccess());
    assertEquals(Response.SC_NOT_FOUND, errorResponse.getStatusCode());
    assertAccountMoney(accountFromId, TEST_MONEY_AMOUNT);
  }

  @Test
  public void testTransferFromNonExistingAccountMustFail() {
    Integer accountToId = appTestClient.createAccount(TEST_MONEY_AMOUNT).getEntity().getId();
    ResponseOrError<Object, ErrorDto> errorResponse = appTestClient.performMoneyTransfer(NON_EXISTING_ACCOUNT_ID, accountToId, 1);
    assertFalse(errorResponse.isSuccess());
    assertEquals(Response.SC_NOT_FOUND, errorResponse.getStatusCode());
    assertAccountMoney(accountToId, TEST_MONEY_AMOUNT);
  }

  @Test
  public void testSelfMoneyTransferIsNotSupported() {
    Integer accountId = appTestClient.createAccount(TEST_MONEY_AMOUNT * 2).getEntity().getId();
    ResponseOrError<Object, ErrorDto> errorResponse = appTestClient.performMoneyTransfer(accountId, accountId, TEST_MONEY_AMOUNT);
    assertFalse(errorResponse.isSuccess());
    assertEquals(Response.SC_BAD_REQUEST, errorResponse.getStatusCode());
    assertAccountMoney(accountId, TEST_MONEY_AMOUNT * 2);
  }

  @Test
  public void testSuccessfulMoneyTransfer() {
    Integer accountFromId = appTestClient.createAccount(TEST_MONEY_AMOUNT * 2).getEntity().getId();
    Integer accountToId = appTestClient.createAccount(TEST_MONEY_AMOUNT).getEntity().getId();

    ResponseOrError<Object, ErrorDto> errorResponse = appTestClient.performMoneyTransfer(accountFromId, accountToId, TEST_MONEY_AMOUNT);
    assertTrue(errorResponse.isSuccess());
    assertAccountMoney(accountFromId, TEST_MONEY_AMOUNT);
    assertAccountMoney(accountToId, TEST_MONEY_AMOUNT * 2);
  }

  @Test
  public void testTransferWithoutEnoughMoneyMustFail() {
    Integer accountFromId = appTestClient.createAccount(TEST_MONEY_AMOUNT * 2).getEntity().getId();
    Integer accountToId = appTestClient.createAccount(TEST_MONEY_AMOUNT).getEntity().getId();

    ResponseOrError<Object, ErrorDto> errorResponse = appTestClient.performMoneyTransfer(accountFromId, accountToId, TEST_MONEY_AMOUNT * 3);
    assertFalse(errorResponse.isSuccess());
    assertAccountMoney(accountFromId, TEST_MONEY_AMOUNT * 2);
    assertAccountMoney(accountToId, TEST_MONEY_AMOUNT);
  }

  private void assertAccountMoney(int accountId, int expectedMoneyAmount) {
    assertEquals(expectedMoneyAmount, appTestClient.getAccount(accountId).getEntity().getMoney());
  }

}
