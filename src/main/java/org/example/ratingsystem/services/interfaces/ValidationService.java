package org.example.ratingsystem.services.interfaces;

public interface ValidationService {
    void setEmailValidationToken(String token, String id);
    String getUserIdFromValidationToken(String token);
    void setForgotPasswordToken(String token, String id);
    String getUserIdFromForgotPasswordToken(String token);
}
