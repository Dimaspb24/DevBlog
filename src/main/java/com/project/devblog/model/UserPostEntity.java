package com.project.devblog.model;

import com.project.devblog.model.enums.BookmarkType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@ToString(exclude = {"user", "post"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users_posts")
public class UserPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer rating;

    @Enumerated(EnumType.STRING)
    BookmarkType bookmarkType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    PostEntity post;

    /*-----------------------------------FOR_MANY_TO_ONE_USER--------------------------------*/
    private void setUser(UserEntity user) {
        this.user = user;
        user.getRelationPosts().add(this);
    }

    /*-----------------------------------FOR_MANY_TO_ONE_POST--------------------------------*/
    private void setPost(PostEntity post) {
        this.post = post;
        post.getRelationUsers().add(this);
    }
}
