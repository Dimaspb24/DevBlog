package com.project.devblog.model;

import com.project.devblog.model.enums.BookmarkType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Data
@ToString(exclude = {"user", "article"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users_articles")
public class UserArticleEntity extends AuditableBaseEntity<String> {

    @Id
    String id;

    Integer rating;

    @Enumerated(EnumType.STRING)
    BookmarkType bookmarkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    ArticleEntity article;

    public UserArticleEntity(@NonNull String id, Integer rating, UserEntity user, ArticleEntity article) {
        this.id = id;
        this.rating = rating;
        this.setUser(user);
        this.setArticle(article);
    }

    public UserArticleEntity(@NonNull String id, BookmarkType bookmarkType, UserEntity user, ArticleEntity article) {
        this.id = id;
        this.bookmarkType = bookmarkType;
        this.setUser(user);
        this.setArticle(article);
    }

    /*-----------------------------------FOR_MANY_TO_ONE_USER--------------------------------*/
    public void setUser(UserEntity user) {
        this.user = user;
        user.getRelationArticles().add(this);
    }

    /*-----------------------------------FOR_MANY_TO_ONE_ARTICLE--------------------------------*/
    public void setArticle(ArticleEntity article) {
        this.article = article;
        article.getRelationUsers().add(this);
    }
}
