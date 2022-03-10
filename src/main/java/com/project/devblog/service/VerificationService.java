package com.project.devblog.service;

import com.project.devblog.config.MailProperties;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final MailProperties mailProperties;

    public void sendVerificationEmail(@NonNull String userId) {
        String toAddress = userRepository.getById(userId).getLogin();
        String fromAddress = mailProperties.getEmail();
        String senderName = mailProperties.getName();
        String subject = mailProperties.getSubject();
        String content = mailProperties.getContent();

        MimeMessage message = mailSender.createMimeMessage();

        try {
            content = content.replace("[[name]]", toAddress.substring(0, toAddress.indexOf('@')));
            String verifyURL = mailProperties.getUrl();
            verifyURL = verifyURL.replace("[[userId]]", userId);
            verifyURL = verifyURL.replace("[[code]]", userRepository.getById(userId).getVerificationCode());
            content = content.replace("[[URL]]", verifyURL);

            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationException("Error sending the verification message to the mail");
        }

        mailSender.send(message);
    }

    public void verify(@NonNull String userId, @NonNull String verificationCode) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(UserEntity.class, userId));

        if (user.getVerificationCode() == null) {
            throw new VerificationException("User already verified");
        }

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new VerificationException("Invalid verification code");
        }

        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
