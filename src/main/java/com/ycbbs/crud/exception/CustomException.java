package com.ycbbs.crud.exception;

public class CustomException extends Exception {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
        this.message = message;
    }
}
