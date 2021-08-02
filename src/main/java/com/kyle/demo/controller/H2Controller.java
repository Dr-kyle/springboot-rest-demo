package com.kyle.demo.controller;

import com.kyle.demo.jdbc.User;
import com.kyle.demo.jdbc.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class H2Controller {

    @Autowired
    UserRepository userRepository;

    @GetMapping("test")
    public void test() {
        System.out.println(userRepository.save(new User("kyle")));
    }

    @GetMapping("test1")
    public void test1() {
        System.out.println(userRepository.save(new User(1L, "kyle")));
    }

    @GetMapping("test2/{id}")
    public void test2(@PathVariable Long id) {
        System.out.println(userRepository.findById(id));
    }

    @GetMapping("test3")
    public void test3() {
        System.out.println(userRepository.findAll());
    }
}
