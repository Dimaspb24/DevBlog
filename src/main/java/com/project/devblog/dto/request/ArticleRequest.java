package com.project.devblog.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
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
    List<@NotBlank @Size(max = 20, message = "The tag can contain up to 20 characters") String> tags;
}
