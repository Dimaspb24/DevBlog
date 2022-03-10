package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.TagRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TagServiceTest {

    @Autowired
    private TagService tagService;
    @MockBean
    private TagRepository tagRepository;
    private static TagEntity tag;
    private static TagEntity tag2;

    @BeforeAll
    static void init() {
        tag = new TagEntity("tag");
        tag.setId(1);
        tag2 = new TagEntity("tag2");
        tag2.setId(2);
    }

    @Test
    void findTest() throws Exception {
        Mockito.when(tagRepository.findById(tag.getId()))
                .thenReturn(Optional.of(tag));
        final TagEntity foundTag = tagService.find(tag.getId());

        assertThat(foundTag.getId()).isEqualTo(tag.getId());
        assertThat(foundTag.getName()).isEqualTo(tag.getName());
    }

    @Test
    void findTestWithNotFoundTag() throws Exception {
        Mockito.when(tagRepository.findById(any()))
                .thenReturn(Optional.empty());

        final Integer tagId = 5;
        assertThatThrownBy(() -> tagService.find(tagId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(format("%s with id=%s not found", TagEntity.class.getSimpleName(), tagId));
    }

    @Test
    void findAllTestByNameContains() throws Exception {
        final String nameContains = "tag";

        final Page<TagEntity> page = new PageImpl<>(List.of(tag, tag2));
        final Pageable pageable = Pageable.ofSize(2);

        Mockito.when(tagRepository.findTagEntitiesByNameContains(nameContains, pageable)).thenReturn(page);

        Page<TagEntity> foundPage = tagService.findAll(nameContains, pageable);

        assertThat(foundPage.getContent()).containsExactly(tag, tag2);
    }

    @Test
    void findAllTestByEmptyAndNullNameContains() throws Exception {
        final Page<TagEntity> page = new PageImpl<>(List.of(tag, tag2));
        final Pageable pageable = Pageable.ofSize(2);

        Mockito.when(tagRepository.findAll(pageable)).thenReturn(page);

        final Page<TagEntity> foundPageByEmptyNameContains = tagService.findAll("", pageable);
        final Page<TagEntity> foundPageByNullNameContains = tagService.findAll(null, pageable);

        assertThat(foundPageByEmptyNameContains.getContent()).containsExactly(tag, tag2);
        assertThat(foundPageByNullNameContains.getContent()).containsExactly(tag, tag2);
        assertThat(foundPageByEmptyNameContains).isEqualTo(foundPageByNullNameContains);
    }

    @Test
    void createTest() throws Exception {
        final String name = "newTag";
        final TagEntity newTag = new TagEntity(name);

        Mockito.when(tagRepository.save(new TagEntity(name))).thenReturn(newTag);

        final TagEntity createdTag = tagService.create(name);

        assertThat(createdTag.getName()).isEqualTo(name);
    }

    @Test
    void createAndGetAllByNameTest() throws Exception {
        final String newTag1Name = "newTag1";
        final String newTag2Name = "tag2";

        final List<TagEntity> tags = List.of(tag, tag2);
        final List<String> newTags = List.of(newTag1Name, newTag2Name);

        newTags.forEach(name -> {
            Mockito.when(tagRepository.findByName(name))
                    .thenReturn(Optional.ofNullable(tags.stream()
                            .filter(tag -> name.equals(tag.getName()))
                            .findAny()
                            .orElse(null)));
            Mockito.when(tagRepository.save(new TagEntity(name)))
                    .thenReturn(new TagEntity(name));
        });

        final List<TagEntity> resultTags = tagService.createAndGetAllByName(newTags);

        assertThat(resultTags.size()).isEqualTo(newTags.size());
        resultTags.forEach(res -> assertThat(newTags.contains(res.getName())).isTrue());
    }

    @Test
    void deleteTest() throws Exception {
        final Integer tagId = 1;

        Mockito.doNothing().when(tagRepository).deleteById(tagId);
        tagService.delete(tagId);

        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(tagId);
        Mockito.verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void updateTest() throws Exception {
        final String updateName = "updateName";
        final TagEntity updateTag = new TagEntity(updateName);
        updateTag.setId(tag.getId());

        Mockito.when(tagRepository.getById(tag.getId())).thenReturn(tag);
        Mockito.when(tagRepository.save(updateTag)).thenReturn(updateTag);

        final TagEntity result = tagService.update(tag.getId(), updateName);

        assertThat(result).isEqualTo(updateTag);
        assertThat(result.getName()).isEqualTo(updateName);
    }
}
