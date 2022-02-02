package com.project.devblog.controller;

import com.project.devblog.controller.annotation.ApiV1;
import com.project.devblog.controller.dto.request.TagRequest;
import com.project.devblog.controller.dto.response.TagResponse;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import static java.util.stream.Collectors.toList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Tag")
@ApiV1
@RestController
@RequiredArgsConstructor
public class TagController {

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
    public TagResponse get(@NonNull @PathVariable Integer tagId) {
        return toResponse(tagService.get(tagId));
    }

    @DeleteMapping("/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable Integer tagId) {
        tagService.delete(tagId);
    }

    @NonNull
    public TagResponse toResponse(@NonNull TagEntity tagEntity) {
        return new TagResponse(tagEntity.getId(), tagEntity.getName());
    }
}
