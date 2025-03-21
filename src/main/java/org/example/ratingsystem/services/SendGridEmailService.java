package org.example.ratingsystem.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.exceptions.MailServiceException;
import org.example.ratingsystem.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SendGridEmailService implements EmailService {
    private final SendGrid sendGrid;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String email, String firstName, String verificationLink) {
        Request request = new Request();

        String subject = "Email Verification";
        String content = String.format("Hello %s, <br> Please click the link below to verify your email address: " +
                "<br> <a href=\"%s\">Verify Email</a>", firstName, verificationLink);

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(email),
                new Content("text/html", content)
        );

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            throw new MailServiceException("Could not send verification email");
        }
    }

    @Override
    public void sendApprovalEmail(String email, String firstName) {
        Request request = new Request();

        String subject = "Account Approved";
        String content = String.format("Hello %s, <br> Your account has been approved.", firstName);

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(email),
                new Content("text/html", content)
        );

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            throw new MailServiceException("Could not send approval email");
        }
    }

    @Override
    public void sendRejectionEmail(String email, String firstName) {
        Request request = new Request();

        String subject = "Account Rejected";
        String content = String.format("Hello %s, <br> Your account has been rejected.", firstName);

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(email),
                new Content("text/html", content)
        );

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            throw new MailServiceException("Could not send rejection email");
        }
    }

    @Override
    public void sendForgotPasswordEmail(String email, String firstName, int token, String link) {
        Request request = new Request();

        String subject = "Password Reset";
        String content = String.format("Hello %s, <br> Please click the link below to reset your password: " +
                "<br> <a href=\"%s\">Reset Password</a> <br> Token: %d", firstName, link, token);

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(email),
                new Content("text/html", content)
        );

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            throw new MailServiceException("Could not send forgot password email");
        }
    }

    @Override
    public void sendResetPasswordEmail(String email, String firstName) {
        Request request = new Request();

        String subject = "Password Reset Successful";
        String content = String.format("Hello %s, <br> Your password has been reset successfully.", firstName);

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(email),
                new Content("text/html", content)
        );

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException ex) {
            throw new MailServiceException("Could not send reset password email");
        }
    }
}
