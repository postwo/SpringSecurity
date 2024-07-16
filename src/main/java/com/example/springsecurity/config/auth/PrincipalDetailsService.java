package com.example.springsecurity.config.auth;

import com.example.springsecurity.model.User;
import com.example.springsecurity.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//시큐리티 설정에서 loginProcessingUrl("/login"); 요청이 오면
//자동으로 userDetailsService 타입으로 ioc되어 잇는 loadUserByUsername 함수가 실행

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username은 login에서 받아오는 파라미터이다
    // 만약에 파라미터명을 변경하고 싶으면
    // Securityconfig 에서 .usernameParameter("변경할 파라미터명") 이걸 추가하면된다

    //로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username:"+username);
        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null){ //있으면
            return new PrincipalDetails(userEntity); //PrincipalDetails객체 생성해서 PrincipalDetails에있는 생성자에 전달
        }
        throw new UsernameNotFoundException("User not found with username: " + username); //없으면
    }
}
