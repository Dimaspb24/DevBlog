package com.project.devblog.model;

import com.project.devblog.model.enums.StatusArticle;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Data
@ToString(exclude = {"comments", "author", "tags", "relationUsers"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "articles")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;
    String body;
    @Enumerated(EnumType.STRING)
    StatusArticle status;
    String description;
    Boolean enabled;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    LocalDateTime publishedDate;
    LocalDateTime deletedDate;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // здесь указываем название поля
    List<CommentEntity> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity author;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<TagEntity> tags = new ArrayList<>();

    // todo нужна ли нам эта связь здесь? не нужна
    @Builder.Default
    @OneToMany(mappedBy = "article")
    List<UserArticleEntity> relationUsers = new ArrayList<>();

    /*-----------------------------------FOR_MANY_TO_MANY_TAG--------------------------------*/
    public void addTag(TagEntity tag) {
        tags.add(tag);
        tag.getArticles().add(this);
    }

    public void removeTag(TagEntity tag) {
        tags.remove(tag);
        tag.getArticles().remove(this);
    }

    /*-----------------------------------FOR_MANY_TO_ONE_AUTHOR--------------------------------*/
    public void setAuthor(UserEntity author) {
        this.author = author;
        author.getSelfArticles().add(this);
    }
}
