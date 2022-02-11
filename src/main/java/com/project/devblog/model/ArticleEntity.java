package com.project.devblog.model;

import com.project.devblog.model.enums.StatusArticle;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Data
@ToString(exclude = {"comments", "author", "tags", "relationUsers"})
@EqualsAndHashCode(of = {"title", "body", "status", "description", "author"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "articles")
public class ArticleEntity extends AuditableBaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NonNull
    String title;

    @NonNull
    String body;

    @NonNull
    @Enumerated(EnumType.STRING)
    StatusArticle status;

    @NonNull
    String description;

    Boolean enabled;

    @Column(name = "publication_date")
    LocalDateTime publicationDate;

    @Formula("(select avg(ua.rating) from users_articles ua where ua.article_id = id)")
    Double rating;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    UserEntity author;

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    List<CommentEntity> comments = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<TagEntity> tags = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "article")
    List<UserArticleEntity> relationUsers = new ArrayList<>();

    public ArticleEntity(@NonNull String title, @NonNull String body, @NonNull StatusArticle status,
                         @NonNull String description, @NonNull UserEntity author) {
        this.title = title;
        this.body = body;
        this.status = status;
        this.description = description;
        this.author = author;
    }

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
