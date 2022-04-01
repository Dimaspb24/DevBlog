package com.project.devblog.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TagRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 20, message = "The tag can contain up to 20 characters")
    String name;
}
