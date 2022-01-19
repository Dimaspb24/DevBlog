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

    public void sendVerificationEmail(@NonNull Integer id) {
        String toAddress = userRepository.getById(id).getLogin();
        String fromAddress = mailProperties.getEmail();
        String senderName = mailProperties.getName();
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + senderName;

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", toAddress.substring(0, toAddress.indexOf('@')));
            String verifyURL = mailProperties.getUrl() + "/verify?code=" + userRepository.getById(id).getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new VerificationException("Error sending the verification message to the mail");
        }

        mailSender.send(message);
    }

    public void verify(String verificationCode) {
        UserEntity user = userRepository.findByVerificationCode(verificationCode).orElseThrow(UserNotFoundException::new);

        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
