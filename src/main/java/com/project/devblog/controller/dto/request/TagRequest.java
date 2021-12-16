package com.project.devblog.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagRequest {
    @NotBlank(message = "Name is required")
    private final String name;
}
