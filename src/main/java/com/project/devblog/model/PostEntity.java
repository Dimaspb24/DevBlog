package com.project.devblog.model;

import com.project.devblog.model.enums.StatusPost;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"comments", "author", "tags", "relationUsers"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String body;
    @Enumerated(EnumType.STRING)
    StatusPost status;
    String description;
    Boolean enabled;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    LocalDateTime publishedDate;
    LocalDateTime deletedDate;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) // здесь указываем название поля
    List<CommentEntity> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity author;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "posts_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    List<TagEntity> tags = new ArrayList<>();

    // todo нужна ли нам эта связь здесь? не нужна
    @Builder.Default
    @OneToMany(mappedBy = "post")
    List<UserPostEntity> relationUsers = new ArrayList<>();

    /*-----------------------------------FOR_MANY_TO_MANY_TAG--------------------------------*/
    private void addTag(TagEntity tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    private void removeTag(TagEntity tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    /*-----------------------------------FOR_MANY_TO_ONE_AUTHOR--------------------------------*/
    private void setAuthor(UserEntity author) {
        this.author = author;
        author.getSelfPosts().add(this);
    }
}
