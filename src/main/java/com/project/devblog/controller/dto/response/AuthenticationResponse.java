package com.project.devblog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse {

    private Integer id;
    private String login;
    private String token;
}
