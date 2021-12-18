package com.project.devblog.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface GenericConverter<E, D> {

    E createEntity(D dto);

    D createDto(E entity);

    E updateEntity(E entity, D dto);

    default List<D> createFromEntities(final Collection<E> entities) {
        return entities.stream()
                .map(this::createDto)
                .collect(Collectors.toList());
    }

    default List<E> createFromDtos(final Collection<D> dtos) {
        return dtos.stream()
                .map(this::createEntity)
                .collect(Collectors.toList());
    }
}
