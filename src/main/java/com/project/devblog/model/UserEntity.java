package com.project.devblog.model;

import com.project.devblog.model.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
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
public class UserEntity extends AuditableBaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String login;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;

    Boolean enabled;
    String verificationCode;

    PersonalInfo personalInfo;

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("creationDate")
    List<CommentEntity> selfComments = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("creationDate")
    List<CommentEntity> receivedComments = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("creationDate")
    List<ArticleEntity> selfArticles = new ArrayList<>();

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<UserArticleEntity> relationArticles = new ArrayList<>();

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

    public UserEntity(@NonNull String login, @NonNull Role role) {
        this.login = login;
        this.role = role;
    }

    /*-----------------------------------FOR_MANY_TO_MANY_SUBSCRIBERS--------------------------------*/
    public void addSubscription(UserEntity subscriber) {
        subscriptions.add(subscriber);
    }

    public void removeSubscription(UserEntity subscriber) {
        subscriptions.remove(subscriber);
    }

    /*-----------------------------------FOR_MANY_TO_MANY_USER_ARTICLE--------------------------------*/
    public void addRelationArticle(UserArticleEntity userArticle) {
        relationArticles.add(userArticle);
        userArticle.setUser(this);
    }

    public void removeRelationArticle(UserArticleEntity userArticle) {
        relationArticles.remove(userArticle);
    }

}
