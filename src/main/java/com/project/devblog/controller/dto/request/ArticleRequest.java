package com.project.devblog.controller.dto.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
    @NotNull(message = "Tags must no be null")
    private final List<String> tags;
}
