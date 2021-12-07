package com.project.devblog.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DynamicInsert
@DynamicUpdate
@Data
@ToString(exclude = "posts")
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    @Builder.Default
    @ManyToMany(mappedBy = "tags")
    List<PostEntity> posts = new ArrayList<>();
}
