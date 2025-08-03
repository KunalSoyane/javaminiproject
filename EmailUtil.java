package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {

    // Method to generate a 6-digit OTP
    public static String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // Method to send the email
    public static boolean sendOtpEmail(String recipientEmail, String otp) {
        final String senderEmail = "kunalviveksoyane@gmail.com"; // Your Gmail address
        final String senderPassword = "otlm uocx jevd dljb"; // Your App Password

        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a Session object
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP for Smart Vote Registration");
            message.setText("Dear User,\n\nYour One Time Password (OTP) is: " + otp);

            // Send the email
            Transport.send(message);
            System.out.println("OTP email sent successfully to " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send OTP email.");
            return false;
        }
    }
}