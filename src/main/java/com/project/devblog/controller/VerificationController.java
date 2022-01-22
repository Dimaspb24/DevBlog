package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.service.VerificationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@ApiV1
@RestController
@AllArgsConstructor
public class VerificationController {

    @NonNull
    private final VerificationService verificationService;

    @GetMapping("/user/{userId}/verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyUser(@NonNull @PathVariable Integer userId,
                           @NonNull @RequestParam("code") String verificationCode) {
        verificationService.verify(userId, verificationCode);
    }
}
