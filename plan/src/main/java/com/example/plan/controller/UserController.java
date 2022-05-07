package com.example.plan.controller;


import com.example.plan.dao.UserRepository;
import com.example.plan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Transactional
public class UserController {

    final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/login.do")
    public String login(
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "password") String password
    ) {
        System.out.println("***************************");
        System.out.println("--login.do");
        System.out.println("phone = " + phone);
        System.out.println("password = " + password);

        List<User> login = userRepository.login(phone, password);
        if (login == null || login.size() == 0) return JSONController.jsonTemplate(false);

        return JSONController.jsonDataPush(login.size() == 1, login.get(0));
    }


    @RequestMapping("/checkPhoneExist.do")
    public String checkPhoneExist(@RequestParam(value = "phone") String phone) {
        System.out.println("***************************");
        System.out.println("--checkPhoneExist.do");
        System.out.println("phone:" + phone);

        List<User> users = userRepository.checkPhoneExist(phone);
        if (users == null || users.size() == 0) return JSONController.jsonTemplate(false);
        return JSONController.jsonTemplate(users.size() == 1);
    }


    @RequestMapping("/register.do")
    public String register(
            @RequestParam(value = "phone") String phone,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "birthday") String birthday,
            @RequestParam(value = "create_time") String create_time
    ) {
        User user = User.builder()
                .phone(phone)
                .password(password)
                .name(name)
                .email(email)
                .birthday(birthday)
                .create_time(create_time)
                .build();

        System.out.println("***************************");
        System.out.println("--register.do");
        System.out.println("user = " + user);

        if (user == null) return JSONController.jsonTemplate(false);

        userRepository.save(user);
        return JSONController.jsonTemplate(true);
    }


}
