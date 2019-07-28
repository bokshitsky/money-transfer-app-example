package ru.boksh.moneytransfer.http.resources;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.boksh.moneytransfer.http.WebExceptionsFactory;
import ru.boksh.moneytransfer.model.MoneyTransferResult;
import ru.boksh.moneytransfer.model.MoneyTransferService;

@Path("/moneyTransfer")
@Produces(MediaType.APPLICATION_JSON)
public class MoneyTransferResource {

  private MoneyTransferService moneyTransferService;

  @Inject
  public MoneyTransferResource(MoneyTransferService moneyTransferService) {
    this.moneyTransferService = moneyTransferService;
  }

  @POST
  @Path("/from/{fromAccountId:[0-9]+}/to/{toAccountId:[0-9]+}")
  // POST запрос на /moneyTransfer со всеми данными транзакции в теле более "rest-овый", но такой проще искать в логах + нужно меньше валидации
  public Response performMoneyTransfer(@PathParam("fromAccountId") int fromAccountId,
                                       @PathParam("toAccountId") int toAccountId,
                                       @FormParam("money") Integer moneyAmount) {
    if (moneyAmount == null) {
      throw WebExceptionsFactory.createWebException(Response.Status.BAD_REQUEST, "'money' param is required");
    }

    MoneyTransferResult moneyTransferResult = moneyTransferService.performMoneyTransfer(fromAccountId, toAccountId, moneyAmount);
    if (moneyTransferResult == MoneyTransferResult.FAIL_NOT_ENOUGH_MONEY) {
      throw WebExceptionsFactory.createWebException(Response.Status.CONFLICT, "Not enough money on account %s", fromAccountId);
    }
    if (moneyTransferResult == MoneyTransferResult.FAIL_NON_POSITIVE_MONEY_TRANSFER_NOT_SUPPORTED_YET) {
      throw WebExceptionsFactory.createWebException(Response.Status.BAD_REQUEST, "Negative or zero transaction will be implemented in next version");
    }
    if (moneyTransferResult == MoneyTransferResult.FAIL_FROM_ACCOUNT_NOT_EXIST) {
      throw WebExceptionsFactory.createWebException(Response.Status.NOT_FOUND, "Account %s does not exist", fromAccountId);
    }
    if (moneyTransferResult == MoneyTransferResult.FAIL_TO_ACCOUNT_NOT_EXIST) {
      throw WebExceptionsFactory.createWebException(Response.Status.NOT_FOUND, "Account %s does not exist", toAccountId);
    }
    if (moneyTransferResult == MoneyTransferResult.FAIL_SELF_TRANSFER_IS_NOT_SUPPORTED) {
      throw WebExceptionsFactory.createWebException(Response.Status.BAD_REQUEST, "Transfer between same account is not supported");
    }
    return Response.noContent().build();
  }

}
