package com.project.devblog.service;

import com.project.devblog.exception.NotFoundException;
import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.TagRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @NonNull
    @Transactional(readOnly = true)
    public TagEntity findById(@NonNull Integer tagId) {
        return tagRepository.findById(tagId).orElseThrow(() ->
                new NotFoundException(TagEntity.class, tagId.toString()));
    }

    @NonNull
    @Transactional
    public TagEntity create(@NonNull String name) {
        return tagRepository.save(new TagEntity(name));
    }

    @NonNull
    @Transactional(readOnly = true)
    public Page<TagEntity> findAll(String tagNameContains, Pageable pageable) {
        if (Objects.nonNull(tagNameContains) && !tagNameContains.isEmpty()) {
            return tagRepository.findTagEntitiesByNameContains(tagNameContains, pageable);
        }
        return tagRepository.findAll(pageable);
    }

    @Transactional
    public void delete(@NonNull Integer tagId) {
        tagRepository.deleteById(tagId);
    }

    @NonNull
    @Transactional
    public TagEntity update(@NonNull Integer tagId, @NonNull String name) {
        TagEntity tagEntity = tagRepository.getById(tagId);
        tagEntity.setName(name);
        tagRepository.save(tagEntity);
        return tagEntity;
    }

    @NonNull
    public List<TagEntity> createAndGetAllByName(@NonNull List<String> tagNames) {
        List<TagEntity> existTags = tagRepository.findByNameIn(tagNames);
        List<String> existTagNames = existTags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toList());

        List<TagEntity> newTags = tagNames.stream()
                .filter(tagName -> !existTagNames.contains(tagName))
                .map(TagEntity::new)
                .collect(Collectors.toList());

        List<TagEntity> savedTags = tagRepository.saveAll(newTags);

        ArrayList<TagEntity> tags = new ArrayList<>(existTags);
        tags.addAll(savedTags);
        return tags;
    }
}
