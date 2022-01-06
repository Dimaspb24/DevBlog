package com.project.devblog.service;

import com.project.devblog.model.ArticleEntity;
import com.project.devblog.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class SearchService {

    private final ArticleRepository articleRepository;

    @NonNull
    public Page<ArticleEntity> findArticlesByTagName(@NonNull String tagName, @NonNull Pageable pageable) {
        return articleRepository.findByTagName(tagName, pageable);
    }

}
