package hu.me.iit.malus.thesis.user.model.factory.impl;

import hu.me.iit.malus.thesis.user.model.UserRole;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class UserRoleTest {

    @Test
    public void fromString() {
        // GIVEN
        String roleStr = "ROLE_Admin";
        UserRole role = UserRole.ADMIN;

        // THEN
        assertThat(UserRole.fromString(roleStr) , equalTo(role));
    }
}
