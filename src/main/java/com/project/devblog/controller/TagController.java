package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.TagRequest;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@ApiV1
@RestController
@AllArgsConstructor
public class TagController {

    @NonNull
    private final TagService tagService;

    @PostMapping("/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@NonNull @Valid TagRequest request) {
        return toResponse(tagService.create(request.getName()));
    }

    @GetMapping("/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<TagResponse> getAll() {
        return tagService.getAll().stream()
                .map(this::toResponse)
                .collect(toList());
    }

    @GetMapping("/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public TagResponse get(@NonNull @PathVariable String tagId) {
        return toResponse(tagService.get(tagId));
    }

    @DeleteMapping("/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable String tagId) {
        tagService.delete(tagId);
    }

    @NonNull
    public TagResponse toResponse(@NonNull TagEntity tagEntity) {
        return new TagResponse(tagEntity.getId(), tagEntity.getName());
    }
}
