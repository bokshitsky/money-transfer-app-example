package ru.boksh.moneytransfer;

import javax.annotation.Nullable;

public class ResponseOrError<EntityClass, ErrorClass> {

  private final EntityClass entity;
  private final ErrorClass error;
  private final int statusCode;
  private final boolean success;

  private ResponseOrError(EntityClass entity, ErrorClass error, int statusCode, boolean success) {
    this.entity = entity;
    this.error = error;
    this.statusCode = statusCode;
    this.success = success;
  }

  public static <EntityClass> ResponseOrError createForSuccess(int statusCode, @Nullable EntityClass entity) {
    return new ResponseOrError<>(entity, null, statusCode, true);
  }

  public static ResponseOrError createForSuccess(int statusCode) {
    return new ResponseOrError<>(null, null, statusCode, true);
  }

  public static <ErrorClass> ResponseOrError createForError(int statusCode, ErrorClass error) {
    return new ResponseOrError<>(null, error, statusCode, false);
  }

  public EntityClass getEntity() {
    return entity;
  }

  public ErrorClass getError() {
    return error;
  }

  public boolean isSuccess() {
    return success;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
