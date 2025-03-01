package org.example.ratingsystem.services.interfaces;

public interface EmailValidationService {
    void setToken(String token, String id);
    String getUserId(String token);
}
