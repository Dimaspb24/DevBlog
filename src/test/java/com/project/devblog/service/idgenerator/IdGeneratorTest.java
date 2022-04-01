package com.project.devblog.service.idgenerator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class IdGeneratorTest {

    @InjectMocks
    IdGenerator idGenerator;

    @Test
    void generateId() {
        String generatedId = idGenerator.generateId();
        assertThat(generatedId).isNotNull();
        assertThat(generatedId).hasSizeGreaterThan(10);
    }
}