package ru.boksh.moneytransfer.httpresources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response test() {
    return Response.ok("Money transfer app is running").build();
  }

}
