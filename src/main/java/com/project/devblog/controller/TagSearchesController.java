package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@ApiV1
@RestController
@AllArgsConstructor
public class TagSearchesController {

    @NonNull
    private final TagService tagService;

    @GetMapping("/searches/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<TagResponse> getByName(@NonNull @RequestParam(name = "nameContains") String name) {
        return tagService.getByName(name).stream()
                .map(this::toResponse)
                .collect(toList());
    }

    @NonNull
    public TagResponse toResponse(@NonNull TagEntity tagEntity) {
        return new TagResponse(tagEntity.getId(), tagEntity.getName());
    }
}
