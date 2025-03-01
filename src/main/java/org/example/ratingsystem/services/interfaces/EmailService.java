package org.example.ratingsystem.services.interfaces;

public interface EmailService {
    void sendVerificationEmail(String email, String firstName, String verificationLink);
}
