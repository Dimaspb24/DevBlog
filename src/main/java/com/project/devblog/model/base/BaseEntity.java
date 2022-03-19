package com.project.devblog.model.base;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable> {

    void setId(T id);

    T getId();
}
