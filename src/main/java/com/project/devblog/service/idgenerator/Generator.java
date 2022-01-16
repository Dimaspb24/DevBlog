package com.project.devblog.service.idgenerator;

import lombok.NonNull;

public interface Generator {
    @NonNull
    String generateId();
}
