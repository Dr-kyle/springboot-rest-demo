package com.kyle.demo;

import com.kyle.demo.Util.JwtUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRestDemoApplicationTests {

    @Test
    void jwtUtil() {
        String token = JwtUtil.createToken("kyle", "kyle1", "admin", "kz37", "haha", "kyle");
        Assert.assertEquals(JwtUtil.getEmail(token, "haha", "kyle"), "kyle");
        Assert.assertEquals(JwtUtil.getUsername(token, "haha", "kyle"), "kyle1");
        Assert.assertEquals(JwtUtil.getRole(token, "haha", "kyle"), "admin");
    }

}
