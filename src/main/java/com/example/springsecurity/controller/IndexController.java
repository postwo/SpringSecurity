package com.example.springsecurity.controller;

import com.example.springsecurity.model.User;
import com.example.springsecurity.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return "redirect:/loginForm";
    }

    //하나만 권한을 걸고 싶을때 사용
    @Secured("ROLE_ADMIN") //특정 메서드에 권한을부여 대신 securityconfig에서 @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) 이거를 넣어줘야 한다
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    //data메서드가 실행되기 직전에 PreAuthorize가 실행된다
    //여러개 권한을 걸고 싶을때 사용
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //특정 메서드에 권한을부여 대신 securityconfig에서 @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) 이거를 넣어줘야 한다
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
