package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.TagRequest;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiV1
@RestController
@AllArgsConstructor
public class TagController {

    @NonNull
    private final TagService tagService;

    @PostMapping("/tags")
    public TagResponse create(@NonNull TagRequest request) {
        return toResponse(tagService.create(request.getName()));
    }

    @GetMapping("/tags")
    public List<TagResponse> getAll() {
        return tagService.getAll().stream()
                .map(this::toResponse)
                .collect(toList());
    }

    @GetMapping("/tag/{tagId}")
    public TagResponse getAll(@NonNull @PathVariable Integer tagId) {
        return toResponse(tagService.get(tagId));
    }

    @DeleteMapping("/tags/{tagId}")
    public void delete(@NonNull @PathVariable Integer tagId) {
        tagService.delete(tagId);
    }

    @NonNull
    public TagResponse toResponse(@NonNull TagEntity tagEntity) {
        return new TagResponse(tagEntity.getId(), tagEntity.getName());
    }
}
