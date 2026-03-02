package com.voidex.thoth.exception;

public class ThothException extends Exception{
    public ThothException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThothException(String message) {
        super(message);
    }
}
