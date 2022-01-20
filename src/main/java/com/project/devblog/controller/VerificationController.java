package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.service.VerificationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@ApiV1
@RestController
@AllArgsConstructor
public class VerificationController {

    @NonNull
    private final VerificationService verificationService;

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUser(@NonNull @RequestParam("code") String verificationCode) {
        verificationService.verify(verificationCode);
    }
}
