package com.seaky.centralconf.core.exception;

public class CentralConfigException extends RuntimeException {

  private static final long serialVersionUID = -1376207615655475982L;

  public CentralConfigException(String msg) {
    super(msg);
  }

  public CentralConfigException(Throwable cause) {
    super(cause);
  }

  public CentralConfigException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
