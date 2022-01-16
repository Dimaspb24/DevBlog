package com.project.devblog.repository;

import com.project.devblog.model.TagEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, String> {
    @NonNull
    List<TagEntity> findAllByNameIn(@NonNull List<String> tags);

    @NonNull
    Optional<TagEntity> findByName(@NonNull String name);
}
