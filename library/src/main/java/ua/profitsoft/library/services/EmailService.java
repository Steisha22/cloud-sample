package ua.profitsoft.library.services;

public interface EmailService {
    void sendSimpleEmail(String consumerEmail, String subject, String content);
}
