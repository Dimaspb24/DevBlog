package com.project.devblog.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleRequest {

    @NotBlank(message = "Title is required")
    String title;
    @NotBlank(message = "Body is required")
    String body;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "(PUBLISHED)|(CREATED)")
    String status;
    @NotBlank(message = "Description is required")
    String description;
    @Nullable
    @Size(max = 4, message = "The maximum number of tags is 4")
    List<String> tags;
}
