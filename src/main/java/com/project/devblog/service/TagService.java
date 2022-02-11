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

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @NonNull
    public TagEntity find(@NonNull Integer tagId) {
        return tagRepository.findById(tagId).orElseThrow(() ->
                new NotFoundException(TagEntity.class, tagId.toString()));
    }

    @NonNull
    public TagEntity create(@NonNull String name) {
        return tagRepository.save(new TagEntity(name));
    }

    @NonNull
    public Page<TagEntity> findAll(String tagNameContains, Pageable pageable) {
        if (Objects.nonNull(tagNameContains) && !tagNameContains.isEmpty()) {
            return tagRepository.findTagEntitiesByNameContains(tagNameContains, pageable);
        }
        return tagRepository.findAll(pageable);
    }

    @NonNull
    public List<TagEntity> createAndGetAllByName(@NonNull List<String> tags) {
        ArrayList<TagEntity> tagEntities = new ArrayList<>();
        tags.forEach(name -> tagEntities.add(tagRepository
                .findByName(name)
                .orElseGet(() -> tagRepository.save(new TagEntity(name)))));
        return tagEntities;
    }

    public void delete(@NonNull Integer tagId) {
        tagRepository.deleteById(tagId);
    }
}
