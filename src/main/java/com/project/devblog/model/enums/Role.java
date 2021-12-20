package com.project.devblog.model.enums;

public enum Role {
    USER,
    ADMIN;

    public String nameWithPrefix() {
        final String prefix = "ROLE_";
        return prefix + this.name();
    }
}
