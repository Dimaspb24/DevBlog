package com.project.devblog.service;

import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.service.exception.TagNotFoundException;
import com.project.devblog.service.idgenerator.Generator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TagService {
    @NonNull
    private final TagRepository tagRepository;
    @NonNull
    private final Generator idGenerator;

    @NonNull
    public TagEntity get(@NonNull Integer tagId) {
        return tagRepository.findById(tagId).orElseThrow(TagNotFoundException::new);
    }

    @NonNull
    public TagEntity create(@NonNull String name) {
        return tagRepository.save(new TagEntity(name));
    }

    @NonNull
    public List<TagEntity> getAll() {
        return tagRepository.findAll();
    }

    @NonNull
    public List<TagEntity> getByNameContains(@NonNull String substring) {
        return tagRepository.findTagEntitiesByNameContains(substring);
    }

    @NonNull
    public List<TagEntity> createAndGetAllByName(@NonNull List<String> tags) {
        ArrayList<TagEntity> tagEntities = new ArrayList<>();
        tags.forEach(name ->
                tagEntities.add(tagRepository.findByName(name).orElseGet(() ->
                        tagRepository.save(new TagEntity(name)))));
        return tagEntities;
    }

    public void delete(@NonNull Integer tagId) {
        tagRepository.deleteById(tagId);
    }
}
