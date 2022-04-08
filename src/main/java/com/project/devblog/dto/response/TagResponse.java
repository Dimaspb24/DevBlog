package com.project.devblog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode
@Getter
@Builder
@ToString
@Jacksonized
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagResponse {

    @NonNull
    Integer id;
    @NonNull
    String name;
}
