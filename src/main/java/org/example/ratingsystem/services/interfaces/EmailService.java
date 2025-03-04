package org.example.ratingsystem.services.interfaces;

public interface EmailService {
    void sendVerificationEmail(String email, String firstName, String verificationLink);
    void sendApprovalEmail(String email, String firstName);
    void sendRejectionEmail(String email, String firstName);
    void sendForgotPasswordEmail(String email, String firstName, int token, String link);
    void sendResetPasswordEmail(String email, String firstName);
}
