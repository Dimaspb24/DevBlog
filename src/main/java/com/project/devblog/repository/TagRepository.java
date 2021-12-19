package com.project.devblog.repository;

import com.project.devblog.model.TagEntity;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @NonNull
    List<TagEntity> findAllByNameIn(@NonNull List<String> tags);
    @NonNull
    Optional<TagEntity> findByName(@NonNull String name);
}
