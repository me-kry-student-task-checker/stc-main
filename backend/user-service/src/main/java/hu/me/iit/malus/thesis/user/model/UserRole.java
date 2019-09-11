package hu.me.iit.malus.thesis.user.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_Admin"),
    TEACHER("ROLE_Teacher"),
    STUDENT("ROLE_Student");

    private String roleString;

    UserRole(String roleString) {
        this.roleString = roleString;
    }
}
