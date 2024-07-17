package com.example.springsecurity.config;

import com.example.springsecurity.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) //securedEnabled,prePostEnabled 어노테이션 활성화 // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //hasRole 및 hasAnyRole 메서드를 사용할 때 ROLE_ 접두사를 자동으로 추가
                //그리고 다른데에서 권한부여 할때는 꼭 ROLE_ 붙여서넣기
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
//                        .usernameParameter("username2") 여기서 파라미터 명을 변경하면 된다
                        .defaultSuccessUrl("/", true) //로그인 성공시 메인페이지 이동
                        .permitAll()
                );

        // 1.코드받기(인증) 2.엑세스토큰(권한)
        // 3.사용자프로필 정보를 가져오고 4.그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
        //구글 로그인이 완료된 뒤의 후처리가 필요함 Tip.코드x,(엑세스토큰+사용자프로필정보)
        http    .oauth2Login(oauth2 -> oauth2
                .loginPage("/loginForm") // OAuth2 로그인 페이지 설정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        )
                .defaultSuccessUrl("/", true) // OAuth2 로그인 성공시 메인페이지 이동
                 );


        return http.build();
    }

}