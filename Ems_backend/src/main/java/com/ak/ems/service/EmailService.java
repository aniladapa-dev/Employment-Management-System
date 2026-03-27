package com.ak.ems.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public void sendCredentialsEmail(String toEmail, String name, String username, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Welcome to EMS - Your Login Credentials");
            message.setText(String.format(
                "Hello %s,\n\n" +
                "Welcome to the Employee Management System (EMS).\n" +
                "Your account has been created successfully. Below are your login credentials:\n\n" +
                "Username: %s\n" +
                "Password: %s\n\n" +
                "Please log in and change your password for security.\n\n" +
                "Best Regards,\n" +
                "EMS Admin Team",
                name, username, password
            ));

            mailSender.send(message);
            logger.info("Credentials email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage());
            // We don't throw exception here to prevent user creation from failing if email fails
        }
    }
}
