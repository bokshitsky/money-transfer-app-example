package ru.boksh.moneytransfer.http;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import ru.boksh.moneytransfer.http.dtos.ErrorDto;

public final class WebExceptionsFactory {

  private WebExceptionsFactory() {
  }

  public static WebApplicationException createWebException(Response.Status status, String message, Object... messageFormatParams) {
    return new WebApplicationException(
        Response.status(status).entity(ErrorDto.withMessage(message, messageFormatParams)).build()
    );
  }

}
