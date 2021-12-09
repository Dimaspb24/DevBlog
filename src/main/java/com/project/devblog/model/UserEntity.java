package com.project.devblog.model;

import com.project.devblog.model.enums.Role;
import com.project.devblog.model.enums.StatusUser;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Data
@ToString(exclude = {"selfComments", "receivedComments", "selfArticles", "subscribers", "subscriptions", "relationArticles"})
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
    Integer id;
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

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate")
    List<CommentEntity> selfComments = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate")
    List<CommentEntity> receivedComments = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "author"/*, cascade = CascadeType.ALL*/, orphanRemoval = true)
    @OrderBy("createdDate")
    List<ArticleEntity> selfArticles = new ArrayList<>();

    // TODO проверить правильность на деле!
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id")
    )
    @OrderBy("personalInfo.nickname")
    List<UserEntity> subscribers = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "subscribers",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @OrderBy("personalInfo.nickname")
    List<UserEntity> subscriptions = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<UserArticleEntity> relationArticles = new ArrayList<>();

    /*-----------------------------------FOR_MANY_TO_MANY_SUBSCRIBERS--------------------------------*/
    public void addSubscription(UserEntity subscriber) {
        subscriptions.add(subscriber);
    }

    public void removeSubscription(UserEntity subscriber) {
        subscriptions.remove(subscriber);
    }

}