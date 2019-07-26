package ru.boksh.moneytransfer.model;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.boksh.moneytransfer.MoneyTransferAppTestBase;

public class AccountStorageTest extends MoneyTransferAppTestBase {

  private AccountStorage accountStorage;

  @BeforeAll
  void setUp() {
    accountStorage = getInjector().getInstance(AccountStorage.class);
  }

  @Test
  void testSuccesfulAccountCreate() {
    Account createdAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    Optional<Account> accountFromStorageOptional = accountStorage.getAccount(createdAccount.getAccountId());
    assertTrue(accountFromStorageOptional.isPresent());
    Account accountFromStorage = accountFromStorageOptional.get();

    assertEquals(createdAccount.getAccountId(), accountFromStorage.getAccountId());
    assertEquals(createdAccount.getMoneyAmount(), accountFromStorage.getMoneyAmount());

    // Check new account is actually created
    assertTrue(createdAccount.getAccountId() < accountStorage.createAccount(TEST_MONEY_AMOUNT).getAccountId());
  }

  @Test
  void testCreateAccountReturnObjectIsNotBindToStorage() {
    Account createdAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    createdAccount.setMoneyAmount(TEST_MONEY_AMOUNT * 2);
    assertEquals(TEST_MONEY_AMOUNT, accountStorage.getAccount(createdAccount.getAccountId()).get().getMoneyAmount());
  }

  @Test
  void testGetAccountReturnObjectIsNotBindToStorage() {
    Account createdAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    accountStorage.getAccount(createdAccount.getAccountId()).get().setMoneyAmount(TEST_MONEY_AMOUNT * 2);
    assertEquals(TEST_MONEY_AMOUNT, accountStorage.getAccount(createdAccount.getAccountId()).get().getMoneyAmount());
  }

  @Test
  void testExecuteOperationWithAccountLockCanNotAffectStorageDirrectly() {
    Account createdAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    accountStorage.executeWithAccountLock(createdAccount.getAccountId(), account -> account.setMoneyAmount(TEST_MONEY_AMOUNT * 2));
    assertEquals(TEST_MONEY_AMOUNT, accountStorage.getAccount(createdAccount.getAccountId()).get().getMoneyAmount());
  }

  @Test
  void testSetAccountMoney() {
    Account createdAccount = accountStorage.createAccount(TEST_MONEY_AMOUNT);
    accountStorage.setAccountMoney(createdAccount.getAccountId(), TEST_MONEY_AMOUNT * 2);
    assertEquals(TEST_MONEY_AMOUNT * 2, accountStorage.getAccount(createdAccount.getAccountId()).get().getMoneyAmount());
  }
}
