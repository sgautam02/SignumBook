package com.signumapp.exception;

public class EmptyCommentException extends RuntimeException {
    public EmptyCommentException() {
    }

    public EmptyCommentException(String message) {
        super(message);
    }
}
