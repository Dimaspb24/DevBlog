package com.project.devblog.controller;

import com.project.devblog.dto.request.TagRequest;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {
    @Mock
    private TagService tagService;
    @InjectMocks
    private TagController controller;
    private TagEntity tagEntity;

    @BeforeEach
    void setUp() {
        tagEntity = TagEntity.builder()
                .id(42)
                .name("Name")
                .build();
    }

    @Test
    void create() {
        final var tagRequest = TagRequest.builder()
                .name("Name")
                .build();
        Mockito.when(tagService.create(any()))
                .thenReturn(tagEntity);
        controller.create(tagRequest);
        Mockito.verify(tagService).create(any());
    }

    @Test
    void findAll() {
        final var tagNameContains = "tagNameContains";
        final var pageable = Pageable.ofSize(5);
        Mockito.when(tagService.findAll(any(), any()))
                .thenReturn(new PageImpl<>(List.of(tagEntity)));
        controller.findAll(tagNameContains, pageable);
        Mockito.verify(tagService).findAll(any(), any());
    }

    @Test
    void find() {
        final var tadId = 42;
        Mockito.when(tagService.find(any()))
                .thenReturn(tagEntity);
        controller.find(tadId);
        Mockito.verify(tagService).find(any());
    }

    @Test
    void delete() {
        final var tadId = 42;
        Mockito.doNothing().when(tagService).delete(any());
        controller.delete(tadId);
        Mockito.verify(tagService).delete(any());
    }

    @Test
    void update() {
        final var tadId = 42;
        final var tagRequest = TagRequest.builder()
                .name("Name")
                .build();
        Mockito.when(tagService.update(any(), any()))
                .thenReturn(tagEntity);
        controller.update(tadId, tagRequest);
        Mockito.verify(tagService).update(any(), any());
    }
}
