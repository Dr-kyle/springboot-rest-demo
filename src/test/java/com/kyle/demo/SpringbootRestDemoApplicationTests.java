package com.kyle.demo;

import com.kyle.demo.Util.JwtUtil;
import com.kyle.demo.jdbc.User;
import com.kyle.demo.jdbc.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserRepository userRepository;

    @Test
    void testInsert(){
        User user = userRepository.save(new User("kyle"));
        System.out.println(user);
    }

}
