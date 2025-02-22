package com.boardcamp.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;




@ControllerAdvice
public class GlobalErrorsHandler {

  @ExceptionHandler({NotFoundError.class})
  public ResponseEntity<String> handlerNotFound(NotFoundError body) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body.getMessage());
  }
  @ExceptionHandler({ConflictError.class})
  public ResponseEntity<String> handlerConflict(ConflictError body) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body.getMessage());
  }


}
