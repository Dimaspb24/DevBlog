package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Comments of articles")
@ApiV1
@RestController
@RequiredArgsConstructor
public class ArticleCommentController {
}
