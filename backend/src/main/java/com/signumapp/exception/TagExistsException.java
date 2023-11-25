package com.signumapp.exception;

public class TagExistsException extends RuntimeException {
    public TagExistsException() {
    }

    public TagExistsException(String message) {
        super(message);
    }
}
