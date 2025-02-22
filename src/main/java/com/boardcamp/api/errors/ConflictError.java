package com.boardcamp.api.errors;

public class ConflictError extends RuntimeException{
  public ConflictError(String message) {
    super(message);
  }
}
