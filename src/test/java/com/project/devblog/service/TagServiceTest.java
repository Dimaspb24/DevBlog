package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.TagRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    TagRepository tagRepository;
    @InjectMocks
    TagService tagService;
    @Captor
    ArgumentCaptor<List<TagEntity>> captureArgumentTags;

    TagEntity tag1;
    TagEntity tag2;

    @BeforeEach
    void init() {
        tag1 = TagEntity.builder().name("tag1").id(1).build();
        tag2 = TagEntity.builder().name("tag2").id(2).build();
    }

    @Test
    void findById() {
        when(tagRepository.findById(tag1.getId())).thenReturn(Optional.of(tag1));
        final TagEntity foundTag = tagService.findById(tag1.getId());
        assertThat(foundTag).isEqualTo(tag1);
    }

    @Test
    void findTestWithNotFoundTag() {
        when(tagRepository.findById(any())).thenReturn(Optional.empty());

        final Integer tagId = 5;
        assertThatThrownBy(() -> tagService.findById(tagId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", TagEntity.class.getSimpleName(), tagId));
    }

    @Test
    void findAllTestByNameContains() {
        final String nameContains = "tag";
        final Page<TagEntity> page = new PageImpl<>(List.of(tag1, tag2));
        final Pageable pageable = Pageable.ofSize(2);
        when(tagRepository.findTagEntitiesByNameContains(nameContains, pageable)).thenReturn(page);

        Page<TagEntity> foundPage = tagService.findAll(nameContains, pageable);

        assertThat(foundPage.getContent()).containsExactly(tag1, tag2);
    }

    @Test
    void findAllTestByEmptyAndNullNameContains() {
        final Page<TagEntity> page = new PageImpl<>(List.of(tag1, tag2));
        final Pageable pageable = Pageable.ofSize(2);
        when(tagRepository.findAll(pageable)).thenReturn(page);

        final Page<TagEntity> foundPageByEmptyNameContains = tagService.findAll("", pageable);
        final Page<TagEntity> foundPageByNullNameContains = tagService.findAll(null, pageable);

        assertThat(foundPageByEmptyNameContains.getContent()).containsExactly(tag1, tag2);
        assertThat(foundPageByNullNameContains.getContent()).containsExactly(tag1, tag2);
        assertThat(foundPageByEmptyNameContains).isEqualTo(foundPageByNullNameContains);
    }

    @Test
    void createTest() {
        final String name = "newTag";
        final TagEntity newTag = new TagEntity(name);
        when(tagRepository.save(new TagEntity(name))).thenReturn(newTag);

        final TagEntity createdTag = tagService.create(name);

        assertThat(createdTag.getName()).isEqualTo(name);
    }

    @Test
    void createAndGetAllByNameTest() {
        TagEntity tag3 = new TagEntity("tag3");
        TagEntity tag4 = new TagEntity("tag4");
        List<String> tagNames = List.of(tag2.getName(), tag3.getName(), tag4.getName());
        final List<TagEntity> expectedTagsForSaving = List.of(tag3, tag4);

        doReturn(List.of(tag2)).when(tagRepository).findByNameIn(tagNames);
        doReturn(expectedTagsForSaving).when(tagRepository).saveAll(expectedTagsForSaving);


        List<TagEntity> tags = tagService.createAndGetAllByName(tagNames);


        verify(tagRepository).saveAll(captureArgumentTags.capture());
        final List<TagEntity> tagsForSaving = captureArgumentTags.getValue();
        assertThat(tagsForSaving).hasSize(expectedTagsForSaving.size())
                .containsAll(expectedTagsForSaving);
        assertThat(tags).hasSize(tagNames.size());
    }

    @Test
    void deleteTest() {
        final Integer tagId = 1;
        doNothing().when(tagRepository).deleteById(tagId);

        tagService.delete(tagId);

        verify(tagRepository, times(1)).deleteById(tagId);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void updateTest() {
        final String updateName = "updateName";
        final TagEntity updateTag = new TagEntity(updateName);
        updateTag.setId(tag1.getId());
        when(tagRepository.getById(tag1.getId())).thenReturn(tag1);
        when(tagRepository.save(updateTag)).thenReturn(updateTag);

        final TagEntity result = tagService.update(tag1.getId(), updateName);

        assertThat(result).isEqualTo(updateTag);
        assertThat(result.getName()).isEqualTo(updateName);
    }
}
