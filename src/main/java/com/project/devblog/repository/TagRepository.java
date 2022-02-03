package com.project.devblog.repository;

import com.project.devblog.model.TagEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    List<TagEntity> findAllByNameIn(@NonNull List<String> tags);

    Page<TagEntity> findAll(@NonNull Pageable pageable);

    Optional<TagEntity> findByName(@NonNull String name);

    Page<TagEntity> findTagEntitiesByNameContains(@NonNull String substring, @NonNull Pageable pageable);
}
