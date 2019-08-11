package ru.boksh.moneytransfer.http.resources;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.boksh.moneytransfer.http.WebExceptionsFactory;
import ru.boksh.moneytransfer.http.dtos.AccountDto;
import ru.boksh.moneytransfer.model.AccountStorage;
import ru.boksh.moneytransfer.model.AccountView;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

  private AccountStorage accountStorage;

  @Inject
  public AccountResource(AccountStorage accountStorage) {
    this.accountStorage = accountStorage;
  }

  @POST
  public AccountDto createNewAccount(@FormParam("money") Integer moneyAmount) {
    if (moneyAmount == null) {
      throw WebExceptionsFactory.createWebException(Response.Status.BAD_REQUEST, "'money' param is required");
    }
    // Assume negative balance is supported on account create
    AccountView account = accountStorage.createAccount(moneyAmount);
    return new AccountDto(account.getAccountId(), account.getMoneyAmount());
  }

  @GET
  @Path("/{accountId:-?[0-9]+}")
  public AccountDto getAccount(@PathParam("accountId") int accountId) {
    return accountStorage.getAccount(accountId)
        .map(account -> new AccountDto(account.getAccountId(), account.getMoneyAmount()))
        .orElseThrow(() -> WebExceptionsFactory.createWebException(Response.Status.NOT_FOUND, "Account %s not found", accountId));
  }

}
