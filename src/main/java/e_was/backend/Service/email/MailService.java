package e_was.backend.Service.email;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;
    
    // Send plainTextEmail
    public void sendPlainTextEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, false); // false = plain text
        mailSender.send(message);
        System.out.println("Plain text email sent to " + to);
    }

    // Send HTML email
    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML content
        mailSender.send(message);
        System.out.println("HTML email sent to " + to);
    }

    // Send a verification code email
    public ResponseEntity<String> sendForgotPasswordEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Password Reset Verification Code");
            message.setText("Your verification code is: " + code);
            mailSender.send(message);
            
            // Return success response with message
            return ResponseEntity.ok("Code sent");
        } catch (Exception e) {
            // Handle errors (e.g., email sending failure)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }

    // Send registration password email
    public void sendRegistrationPassword(String toEmail, String password) throws MessagingException {
        String subject = "Your Registration Password for E-Waste";
        String body = "Your randomly generated password for registration is: " + password;
        sendPlainTextEmail(toEmail, subject, body);
    }

    // Send email to reject an organization
    public void sendOrganizationRejection(String toEmail) throws MessagingException {
        String subject = "E-Waste Application Rejection";
        String body = "Dear Sir/Madam,\n\nYour organization has been rejected by our admin. Our sincerest apologies.\n\nBest Regards,\nE-Waste Team";
        sendPlainTextEmail(toEmail, subject, body);
    }

    //Maybe No needed
    // Generate a random string of letters and digits of a specified length
    public String getRandomCode(int length) {
        String validSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%^&*()_+-=[]{};:,<.>/?";
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            codeBuilder.append(validSymbols.charAt(random.nextInt(validSymbols.length())));
        }
        return codeBuilder.toString();
    }

    // // Generate a random password of a specified length
    // public String getRandomPassword(int length) {
    //     String validSymbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%^&*()_+-=[]{}|;:,<.>/?";
    //     Random random = new Random();
    //     StringBuilder passwordBuilder = new StringBuilder();
    //     for (int i = 0; i < length; i++) {
    //         passwordBuilder.append(validSymbols.charAt(random.nextInt(validSymbols.length())));
    //     }
    //     return passwordBuilder.toString();
    // }
}
