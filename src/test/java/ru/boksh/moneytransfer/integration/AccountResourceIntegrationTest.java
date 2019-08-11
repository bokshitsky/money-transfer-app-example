package ru.boksh.moneytransfer.integration;

import org.eclipse.jetty.server.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.boksh.moneytransfer.ResponseOrError;
import ru.boksh.moneytransfer.http.dtos.AccountDto;
import ru.boksh.moneytransfer.http.dtos.ErrorDto;

public class AccountResourceIntegrationTest extends MoneyTransferAppIntegrationTestBase {

  @Test
  public void testGetNonExistingAccountReturnsNotFound() {
    ResponseOrError<AccountDto, ErrorDto> errorResponse = appTestClient.getAccount(NON_EXISTING_ACCOUNT_ID);
    Assertions.assertFalse(errorResponse.isSuccess());
    Assertions.assertEquals(Response.SC_NOT_FOUND, errorResponse.getStatusCode());
  }

  @Test
  public void testSuccessfulCreateNewAccount() {
    int accountMoney = TEST_MONEY_AMOUNT * 2;
    ResponseOrError<AccountDto, ErrorDto> accountResponse = appTestClient.createAccount(accountMoney);

    Assertions.assertTrue(accountResponse.isSuccess());
    AccountDto accountDto = accountResponse.getEntity();
    Assertions.assertEquals(accountMoney, accountDto.getMoney());
    Assertions.assertNotNull(accountDto.getId());
  }

  @Test
  public void testGetCreatedAccount() {
    int accountMoney = TEST_MONEY_AMOUNT * 3;
    Integer accountId = appTestClient.createAccount(accountMoney).getEntity().getId();

    ResponseOrError<AccountDto, ErrorDto> accountResponse = appTestClient.getAccount(accountId);
    Assertions.assertTrue(accountResponse.isSuccess());
    AccountDto accountDto = accountResponse.getEntity();
    Assertions.assertEquals(accountMoney, accountDto.getMoney());
    Assertions.assertEquals(accountId, accountDto.getId());
  }
}
