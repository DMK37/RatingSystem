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
}
