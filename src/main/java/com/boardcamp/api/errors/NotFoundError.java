package com.boardcamp.api.errors;

public class NotFoundError extends RuntimeException {
  public NotFoundError(String message) {
    super(message);
  }
}
