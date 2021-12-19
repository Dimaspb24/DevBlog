package com.project.devblog.service;

import com.project.devblog.model.TagEntity;
import com.project.devblog.repository.TagRepository;
import com.project.devblog.service.exception.TagNotFoundException;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TagService {
    @NonNull
    private final TagRepository tagRepository;

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
    public List<TagEntity> getAllByName(@NonNull List<String> tags) {
        return tagRepository.findAllByNameIn(tags);
    }

    public void delete(@NonNull Integer tagId) {
        tagRepository.deleteById(tagId);
    }
}
