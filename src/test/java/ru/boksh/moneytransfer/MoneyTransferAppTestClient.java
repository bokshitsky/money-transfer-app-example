package ru.boksh.moneytransfer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Response;
import ru.boksh.moneytransfer.http.dtos.AccountDto;
import ru.boksh.moneytransfer.http.dtos.ErrorDto;

public class MoneyTransferAppTestClient {

  private final HttpClient httpClient;
  private final URI serverUri;
  private ObjectMapper objectMapper = new ObjectMapper();

  public MoneyTransferAppTestClient(HttpClient httpClient, URI serverUri) {
    this.httpClient = httpClient;
    this.serverUri = serverUri;
  }

  private URI getServerUriWithQueryParams(String path, Map<String, String> queryParams) {
    UriBuilder uriBuilder = UriBuilder.fromUri(serverUri).path(path);
    queryParams.forEach(uriBuilder::queryParam);
    return uriBuilder.build();
  }

  private HttpResponse executeGet(String path) {
    return executeGet(path, Map.of());
  }

  private HttpResponse executeGet(String path, Map<String, String> queryParams) {
    try {
      return httpClient.execute(new HttpGet(getServerUriWithQueryParams(path, queryParams)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private HttpResponse executePost(String path) {
    return executePost(path, Map.of());
  }

  private HttpResponse executePost(String path, Map<String, String> formParams) {
    HttpPost httpPost = new HttpPost(getServerUriWithQueryParams(path, Map.of()));
    httpPost.setEntity(new UrlEncodedFormEntity(
        formParams.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList()),
        Charset.forName("UTF-8")
    ));
    try {
      return httpClient.execute(httpPost);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public ResponseOrError<AccountDto, ErrorDto> getAccount(int accountId) {
    HttpResponse httpResponse = executeGet(String.format("/account/%s", accountId));
    return parseAccountOrError(httpResponse);
  }

  public ResponseOrError<AccountDto, ErrorDto> createAccount(@Nullable Integer amountOfMoney) {
    HttpResponse httpResponse = executePost(
        "/account",
        Optional.ofNullable(amountOfMoney).map(money -> Map.of("money", String.valueOf(money))).orElseGet(Map::of)
    );
    return parseAccountOrError(httpResponse);
  }

  public ResponseOrError<Object, ErrorDto> performMoneyTransfer(int accountIdFrom, int accountIdTo, @Nullable Integer amountOfMoney) {
    HttpResponse httpResponse = executePost(
        String.format("/moneyTransfer/from/%s/to/%s", accountIdFrom, accountIdTo),
        Optional.ofNullable(amountOfMoney).map(money -> Map.of("money", String.valueOf(money))).orElseGet(Map::of)
    );
    return parseNoContentOrError(httpResponse);
  }

  private ResponseOrError<Object, ErrorDto> parseNoContentOrError(HttpResponse httpResponse) {
    int responseStatus = httpResponse.getStatusLine().getStatusCode();
    if (responseStatus == Response.SC_NO_CONTENT) {
      return ResponseOrError.createForSuccess(responseStatus);
    }
    try {
      return ResponseOrError.createForError(
          responseStatus,
          objectMapper.readValue(httpResponse.getEntity().getContent(), ErrorDto.class)
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ResponseOrError<AccountDto, ErrorDto> parseAccountOrError(HttpResponse httpResponse) {
    int responseStatus = httpResponse.getStatusLine().getStatusCode();
    if (responseStatus == Response.SC_OK) {
      try {
        return ResponseOrError.createForSuccess(
            responseStatus,
            objectMapper.readValue(httpResponse.getEntity().getContent(), AccountDto.class)
        );
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    try {
      return ResponseOrError.createForError(
          responseStatus,
          objectMapper.readValue(httpResponse.getEntity().getContent(), ErrorDto.class)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
