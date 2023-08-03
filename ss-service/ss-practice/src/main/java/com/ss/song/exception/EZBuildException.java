package com.ss.song.exception;

public class EZBuildException extends RuntimeException{

    private static final long serialVersionUID = 6922970840107066104L;

    private String errorCode;

    public EZBuildException() {
        super();
    }

    public EZBuildException(String message){
        super(message);
    }

    public EZBuildException(Throwable cause) {
        super(cause);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
