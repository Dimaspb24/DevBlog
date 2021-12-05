package com.project.devblog.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString(exclude = {"post", "author", "receiver"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String message;
    Boolean enabled;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    LocalDateTime deletedDate;

    @ManyToOne // нужно или нет каскады?
    @JoinColumn(name = "post_id")
    PostEntity post;

    @ManyToOne
    @JoinColumn(name = "author_id")
    UserEntity author;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    UserEntity receiver;

    /*-----------------------------------FOR_MANY_TO_ONE--------------------------------*/
    public void setPost(PostEntity post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
        author.getSelfComments().add(this);
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
        receiver.getReceivedComments().add(this);
    }
}