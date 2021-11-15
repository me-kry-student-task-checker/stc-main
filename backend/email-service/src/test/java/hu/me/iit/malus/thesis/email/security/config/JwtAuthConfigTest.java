package hu.me.iit.malus.thesis.email.security.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

interface Constants {
    String SECRET = "dummy-secret";
    String HEADER = "dummy-header";
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JwtAuthConfig.class)
@TestPropertySource(properties = {
        "security.jwt.secret=" + Constants.SECRET,
        "security.jwt.inner.header=" + Constants.HEADER
})
public class JwtAuthConfigTest {

    @Autowired
    private JwtAuthConfig jwtAuthConfig;

    @Test
    public void getSecret() {
        Assert.assertEquals(Constants.SECRET, jwtAuthConfig.getSecret());
    }

    @Test
    public void getTokenHeader() {
        Assert.assertEquals(Constants.HEADER, jwtAuthConfig.getTokenHeader());
    }
}