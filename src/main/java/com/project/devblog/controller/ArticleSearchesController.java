package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import org.springframework.web.bind.annotation.RestController;

@ApiV1
@RestController
public class ArticleSearchesController {


    /**
     * Поиск будет производиться с помощью @PathParam
     * Не реализован, так как нужна реализация репозитория сначала для полноты информации в @PathParam
     *
     * @GetMapping("/articles/searches")
     * @ResponseStatus(HttpStatus.OK) public ArticleResponse searchArticle(@PathParam("")) {
     * return
     * }
     */
}
