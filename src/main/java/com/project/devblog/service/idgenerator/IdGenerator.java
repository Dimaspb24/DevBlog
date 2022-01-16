package com.project.devblog.service.idgenerator;

import com.devskiller.friendly_id.FriendlyId;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class IdGenerator implements Generator{

    @NonNull
    @Override
    public String generateId() {
        return FriendlyId.createFriendlyId();
    }
}
