package org.example.ratingsystem.exceptions;

public class MailServiceException extends RuntimeException {
    public MailServiceException(String message) {
        super(message);
    }
}
