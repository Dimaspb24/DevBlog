package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 20, message = "The tag can contain up to 20 characters")
    String name;
}
