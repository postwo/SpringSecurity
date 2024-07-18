package com.example.springsecurity.controller;

import com.example.springsecurity.config.auth.PrincipalDetails;
import com.example.springsecurity.model.User;
import com.example.springsecurity.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 둘중에 사용하고 싶은 방법 사용
    //1. @AuthenticationPrincipal 세션정보 접근 가능
    //2. PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); 이렇게 다운 캐스팅해서 세션정보를 가지고 온다
    // 원래는 userDetails로 다운캐스팅을 해줘야 한다 하지만 지금은 principalDeatils에서 userDeatils를 구현받았기 때문에 이렇게 사용할 수 있는거다
    @GetMapping("/test/login")
    public @ResponseBody String TestLogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login =============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //getPrincipal은 오브젝트 타입이어서 형변환 = 다운캐스팅을 해준다
        System.out.println("authentication :"+principalDetails.getUser()); //유저정보

        System.out.println("userDetails:"+ userDetails.getUser());

        return "세션 정보 확인하기";
    }

    //oauth 세션 정보
    //(OAuth2User) 다운 캐스팅을 해줘야 한다
    // 여기도 위에처럼 2가지 방식으로 세션정보를 가지고 올수 있다
    @GetMapping("/test/oauth/login")
    public @ResponseBody String TestOauthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login =============");
        OAuth2User auth2User = (OAuth2User) authentication.getPrincipal(); //getPrincipal은 오브젝트 타입이어서 형변환 = 다운캐스팅을 해준다
        System.out.println("authentication :"+auth2User.getAttributes()); //유저정보

        System.out.println("OAuth2User"+ oauth.getAttributes());

        return "OAUTH 세션 정보 확인하기";
    }

    @GetMapping({"","/"})
    public String index(){
        return "index";
    }


    //이렇게 PrincipalDetails에서 userDetails,OAuth2User 두개를 구현해서 받아오면 위에 방식처럼 따로따로 안받아와도 된다
    //oauth로그인을 해도 principalDeatils로 받을수 있고
    //일반 로그인을 해도 principaldetials로 받을수 있다
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("pricipalDeatils:"+principalDetails.getUser());
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
