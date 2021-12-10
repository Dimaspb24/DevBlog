package com.project.devblog.repository;

import com.project.devblog.model.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    Optional<TagEntity> findByName(String name);
}
