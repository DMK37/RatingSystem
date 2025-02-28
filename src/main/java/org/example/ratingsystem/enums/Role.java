package org.example.ratingsystem.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role valueOfRole(String role) {
        for (Role r : Role.values()) {
            if (r.role.equals(role)) {
                return r;
            }
        }
        return null;
    }
}
