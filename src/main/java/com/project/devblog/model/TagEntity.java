package com.project.devblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(exclude = "articles")
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    String id;

    @NonNull
    String name;

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    List<ArticleEntity> articles = new ArrayList<>();

    public TagEntity(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
