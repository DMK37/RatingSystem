package org.example.ratingsystem.exceptions;

public class NotPendingStatus extends RuntimeException {
    public NotPendingStatus(String message) {
        super(message);
    }
}
