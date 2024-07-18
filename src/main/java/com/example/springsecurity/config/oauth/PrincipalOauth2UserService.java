package com.example.springsecurity.config.oauth;


import com.example.springsecurity.config.auth.PrincipalDetails;
import com.example.springsecurity.model.User;
import com.example.springsecurity.reposiotry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    //여기서 후처리 = 후처리 되는 함수 = 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest.getClientRegistration()); //registarionId로 어떤 oauth로 로그인 했는지 확인가능
        System.out.println("userAccessToken:"+userRequest.getAccessToken());
        System.out.println("userAccessTokenValue:"+userRequest.getAccessToken().getTokenValue()); //getAccessToken

        OAuth2User oauth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창-> 로그인을 완료 -> code를 리턴(oauth-client라이브러리) -> accessToken요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필을 받아준다
        //getAttributes 여기서 정보를 가지고온거다
        System.out.println("userAttributes:"+oauth2User.getAttributes()); //여기에 모든 정보가 들어있음

        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oauth2User.getAttribute("sub"); // google Id
        String username = provider+"_"+providerId; // google_113461757431495519610
        String password = passwordEncoder.encode("겟인데어"); //크게 의미는없다 그냥 유저정보를 만들어보는거다
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userENtity =  userRepository.findByUsername(username);

        if (userENtity == null){
            System.out.println("구글 로그인이 최초입니다");
            userENtity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userENtity);
        }else{
            System.out.println("구글 로그인을 이미 한적이 있습니다");
        }

        return new PrincipalDetails(userENtity,oauth2User.getAttributes());
    }
}
