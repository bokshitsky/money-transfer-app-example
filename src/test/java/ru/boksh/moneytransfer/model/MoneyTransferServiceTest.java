package ru.boksh.moneytransfer.model;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.boksh.moneytransfer.MoneyTransferAppTestBase;

public class MoneyTransferServiceTest extends MoneyTransferAppTestBase {

  private MoneyTransferService moneyTransferService;
  private AccountStorage accountStorage;

  @BeforeAll
  void setUp() {
    moneyTransferService = getInjector().getInstance(MoneyTransferService.class);
    accountStorage = getInjector().getInstance(AccountStorage.class);
  }

  @Test
  void testTransferFromNonExistingAccountMustFail() {
    Account existingAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    int accountIdHavingNoChanceToExistInTestEnv = Integer.MIN_VALUE / 2;
    assertEquals(
        MoneyTransferResult.FAIL_FROM_ACCOUNT_NOT_EXISTS,
        moneyTransferService.performMoneyTransfer(accountIdHavingNoChanceToExistInTestEnv, existingAccount.getAccountId(), 1000)
    );
    assertAccountMoney(existingAccount.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testTransferToNonExistingAccountMustFail() {
    Account existingAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    int accountIdHavingNoChanceToExistInTestEnv = Integer.MIN_VALUE / 2;
    assertEquals(
        MoneyTransferResult.FAIL_TO_ACCOUNT_NOT_EXISTS,
        moneyTransferService.performMoneyTransfer(existingAccount.getAccountId(), accountIdHavingNoChanceToExistInTestEnv, 1000)
    );
    assertAccountMoney(existingAccount.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testTransferBetweenSameAccountMustFail() {
    Account account = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    assertEquals(
        MoneyTransferResult.FAIL_SELF_TRANSFER_IS_NOT_SUPPORTED,
        moneyTransferService.performMoneyTransfer(account.getAccountId(), account.getAccountId(), 1000)
    );
    assertAccountMoney(account.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testNegativeMoneyTransferNotSupported() {
    Account account1 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    Account account2 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    assertEquals(
        MoneyTransferResult.FAIL_NON_POSITIVE_MONEY_NOT_SUPPORTED_YET,
        moneyTransferService.performMoneyTransfer(account1.getAccountId(), account2.getAccountId(), -1000)
    );
    assertAccountMoney(account1.getAccountId(), TEST_MONEY_AMOUNT);
    assertAccountMoney(account2.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testZeroMoneyTransferNotSupported() {
    Account account1 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    Account account2 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    assertEquals(
        MoneyTransferResult.FAIL_NON_POSITIVE_MONEY_NOT_SUPPORTED_YET,
        moneyTransferService.performMoneyTransfer(account1.getAccountId(), account2.getAccountId(), 0)
    );
    assertAccountMoney(account1.getAccountId(), TEST_MONEY_AMOUNT);
    assertAccountMoney(account2.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testTransferWithoutEnoughMoneyMustFail() {
    Account account1 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    Account account2 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    assertEquals(
        MoneyTransferResult.FAIL_NOT_ENOUGH_MONEY,
        moneyTransferService.performMoneyTransfer(account1.getAccountId(), account2.getAccountId(), TEST_MONEY_AMOUNT * 2)
    );
    assertAccountMoney(account1.getAccountId(), TEST_MONEY_AMOUNT);
    assertAccountMoney(account2.getAccountId(), TEST_MONEY_AMOUNT);
  }

  @Test
  void testSuccessfullMoneyTransfer() {
    Account account1 = accountStorage.createAccount(TEST_MONEY_AMOUNT * 2);
    Account account2 = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    assertEquals(
        MoneyTransferResult.SUCCESS,
        moneyTransferService.performMoneyTransfer(account1.getAccountId(), account2.getAccountId(), TEST_MONEY_AMOUNT * 2)
    );
    assertAccountMoney(account1.getAccountId(), 0);
    assertAccountMoney(account2.getAccountId(), TEST_MONEY_AMOUNT * 3);
  }

  private void assertAccountMoney(int accountId, int expectedMoney) {
    Optional<Account> accountOptional = accountStorage.getAccount(accountId);
    assertTrue(accountOptional.isPresent());
    assertEquals(expectedMoney, accountOptional.get().getMoneyAmount());
  }

}
