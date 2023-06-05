package com.example.urlshorteningservice.error;

public class UrlCreationException extends RuntimeException {

    public UrlCreationException(String message) {
        super(message);
    }

    public UrlCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlCreationException(Throwable cause) {
        super(cause);
    }
}