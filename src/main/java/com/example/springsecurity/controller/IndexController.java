package com.example.springsecurity.controller;

import com.example.springsecurity.model.User;
import com.example.springsecurity.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping({"","/"})
    public String index(){
        return "index";
    }


    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //로그인 페이지
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    //회원가입 페이지
    @GetMapping("/joinForm")
    public  String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("USER");
        String encPassword  = passwordEncoder.encode(user.getPassword());
        user.setPassword(encPassword);
        userRepository.save(user);
        return "join";
    }

}
