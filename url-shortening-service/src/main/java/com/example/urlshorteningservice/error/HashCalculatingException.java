package com.example.urlshorteningservice.error;

public class HashCalculatingException extends RuntimeException {

    public HashCalculatingException(String message) {
        super(message);
    }

    public HashCalculatingException(String message, Throwable cause) {
        super(message, cause);
    }

    public HashCalculatingException(Throwable cause) {
        super(cause);
    }
}