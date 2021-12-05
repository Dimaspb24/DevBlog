package com.project.devblog.model;

import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"selfComments", "receivedComments", "selfPosts", "subscribers", "subscriptions", "relationPosts"})
@EqualsAndHashCode(of = "login")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String login;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
    @Enumerated(EnumType.STRING)
    StatusUser status;

    @Embedded
    @AttributeOverride(name = "createdDate", column = @Column(name = "created_date"))
    @AttributeOverride(name = "updatedDate", column = @Column(name = "updated_date"))
    PersonalInfo personalInfo;

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @OrderBy("createdDate")
    List<CommentEntity> selfComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @OrderBy("createdDate")
    List<CommentEntity> receivedComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author"/*, cascade = CascadeType.ALL*/, orphanRemoval = true)
    @OrderBy("createdDate")
    List<PostEntity> selfPosts = new ArrayList<>();

    // TODO проверить правильность на деле!
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    @OrderBy("personalInfo.nickname")
    List<UserEntity> subscribers = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "subscribers",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @OrderBy("personalInfo.nickname")
    List<UserEntity> subscriptions = new ArrayList<>();

    // todo нужна ли нам эта связь здесь?
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<UserPostEntity> relationPosts = new ArrayList<>();

    /*-----------------------------------FOR_MANY_TO_MANY_SUBSCRIBERS--------------------------------*/
    private void addSubscription(UserEntity subscriber) {
        subscriptions.add(subscriber);
        subscriber.getSubscribers().add(this);
    }

    private void removeSubscription(UserEntity subscriber) {
        subscriptions.remove(subscriber);
        subscriber.getSubscribers().remove(this);
    }

    /*-----------------------------------FOR_MANY_TO_MANY_RELATION_POSTS--------------------------------*/
}
