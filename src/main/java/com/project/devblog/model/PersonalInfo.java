package com.project.devblog.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class PersonalInfo {

    String firstname;
    String lastname;
    String nickname;
    String photo;
    String info;
    String phone;
    Boolean enabled;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}
