package ru.boksh.moneytransfer.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorDto {

  @JsonProperty
  private String message;

  private ErrorDto(String message) {
    this.message = message;
  }

  public ErrorDto() {
  }

  public static ErrorDto withMessage(String message, Object... stringFormatParams) {
    return new ErrorDto(String.format(message, stringFormatParams));
  }

}
