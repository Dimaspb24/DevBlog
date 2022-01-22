package com.project.devblog.service;

import com.project.devblog.config.MailProperties;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserRepository;
import com.project.devblog.service.exception.UserNotFoundException;
import com.project.devblog.service.exception.VerificationException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
@Transactional
public class VerificationService {

    @NonNull
    private final JavaMailSender mailSender;
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final MailProperties mailProperties;

    public void sendVerificationEmail(@NonNull Integer userId) {
        String toAddress = userRepository.getById(userId).getLogin();
        String fromAddress = mailProperties.getEmail();
        String senderName = mailProperties.getName();
        String subject = mailProperties.getSubject();
        String content = mailProperties.getContent();

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", toAddress.substring(0, toAddress.indexOf('@')));
            String verifyURL = mailProperties.getUrl();
            verifyURL = verifyURL.replace("[[userId]]", userId.toString());
            verifyURL = verifyURL.replace("[[code]]", userRepository.getById(userId).getVerificationCode());
            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationException("Error sending the verification message to the mail");
        }

        mailSender.send(message);
    }

    public void verify(@NonNull Integer userId, @NonNull String verificationCode) {
        UserEntity user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new VerificationException("Invalid verification code");
        }

        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
