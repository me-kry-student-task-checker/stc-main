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

    public static UserRole fromString(String string) {
        for (UserRole role : UserRole.values()) {
            if (role.roleString.equalsIgnoreCase(string)) {
                return role;
            }
        }
        throw new IllegalStateException("The given string is not a valid role");
    }
}
