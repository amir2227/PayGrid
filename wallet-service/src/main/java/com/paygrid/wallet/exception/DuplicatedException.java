package com.paygrid.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public DuplicatedException(String confield) {
    super(String.format(confield));
  }
}
