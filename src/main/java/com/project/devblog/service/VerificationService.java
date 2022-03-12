package com.project.devblog.service;

import com.project.devblog.config.MailProperties;
import com.project.devblog.exception.NotFoundException;
import com.project.devblog.exception.VerificationException;
import com.project.devblog.model.UserEntity;
import com.project.devblog.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final MailProperties mailProperties;

    @Transactional(readOnly = true)
    public void sendVerificationEmail(@NonNull String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(UserEntity.class, userId));
        String toAddress = user.getLogin();

        String content = mailProperties.getContent();
        String verifyURL = mailProperties.getUrl();
        content = content.replace("[[name]]", toAddress.substring(0, toAddress.indexOf('@')));
        verifyURL = verifyURL.replace("[[userId]]", userId);
        verifyURL = verifyURL.replace("[[code]]", user.getVerificationCode());
        content = content.replace("[[URL]]", verifyURL);

        try {
            MimeMessage message = getMimeMessage(toAddress, content);
            mailSender.send(message);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            throw new VerificationException("Error sending the verification message to the mail");
        }
    }

    @NonNull
    private MimeMessage getMimeMessage(String toAddress, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(mailProperties.getEmail(), mailProperties.getName());
        helper.setSubject(mailProperties.getSubject());
        helper.setTo(toAddress);
        helper.setText(content, true);
        return message;
    }

    @Transactional
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
