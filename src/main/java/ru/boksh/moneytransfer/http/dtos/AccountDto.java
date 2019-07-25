package ru.boksh.moneytransfer.http.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDto {

  @JsonProperty
  private Integer id;

  @JsonProperty
  private Integer money;

  public AccountDto(Integer id, Integer money) {
    this.id = id;
    this.money = money;
  }

  public AccountDto() {
  }
}
