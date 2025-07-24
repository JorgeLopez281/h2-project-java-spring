package com.tecnova.technical_test.infrastructure.adapter.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum RoleList {
    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String value;

    RoleList(String value) {
        this.value = value;
    }

    public static Optional<RoleList> fromValue(String value) {
        return Arrays.stream(RoleList.values())
                .filter(role -> role.getValue().equalsIgnoreCase(value))
                .findFirst();
    }
}
