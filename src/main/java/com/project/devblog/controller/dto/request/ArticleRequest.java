package com.project.devblog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleRequest {
    @NotBlank(message = "Title is required")
    private final String title;
    @NotBlank(message = "Body is required")
    private final String body;
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "(PUBLISHED)|(CREATED)|(REMOVED)")
    private final String status;
    @NotBlank(message = "Description is required")
    private final String description;
    @Nullable
    private final List<String> tags;
}
