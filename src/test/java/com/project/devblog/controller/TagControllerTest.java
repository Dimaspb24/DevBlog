package com.project.devblog.controller;

import com.project.devblog.dto.request.TagRequest;
import com.project.devblog.model.TagEntity;
import com.project.devblog.service.TagService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    TagService tagService;
    @InjectMocks
    TagController controller;

    TagEntity tagEntity;

    @BeforeEach
    void setUp() {
        tagEntity = TagEntity.builder()
                .id(42)
                .name("Name")
                .build();
    }

    @Test
    void create() {
        final var tagRequest = TagRequest.builder().name("Name").build();
        when(tagService.create(any())).thenReturn(tagEntity);

        controller.create(tagRequest);

        verify(tagService).create(any());
    }

    @Test
    void findAll() {
        final var tagNameContains = "tagNameContains";
        final var pageable = Pageable.ofSize(5);
        when(tagService.findAll(any(), any())).thenReturn(new PageImpl<>(List.of(tagEntity)));

        controller.findAll(tagNameContains, pageable);

        verify(tagService).findAll(any(), any());
    }

    @Test
    void find() {
        final var tadId = 42;
        when(tagService.find(any())).thenReturn(tagEntity);

        controller.find(tadId);

        verify(tagService).find(any());
    }

    @Test
    void delete() {
        final var tadId = 42;
        doNothing().when(tagService).delete(any());

        controller.delete(tadId);

        verify(tagService).delete(any());
    }

    @Test
    void update() {
        final var tadId = 42;
        final var tagRequest = TagRequest.builder().name("Name").build();
        when(tagService.update(any(), any())).thenReturn(tagEntity);

        controller.update(tadId, tagRequest);

        verify(tagService).update(any(), any());
    }
}
