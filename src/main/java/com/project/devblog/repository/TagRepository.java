package com.project.devblog.repository;

import com.project.devblog.model.TagEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @NonNull
    Optional<TagEntity> findByName(@NonNull String name);
}
