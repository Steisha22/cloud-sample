package ua.profitsoft.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendSimpleEmail(String consumerEmail, String subject, String content) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(senderEmail);
        simpleMailMessage.setTo(consumerEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        emailSender.send(simpleMailMessage);
    }
}
