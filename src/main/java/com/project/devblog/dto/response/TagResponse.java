package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagResponse {

    @NonNull
    Integer id;
    @NonNull
    String name;
}
