package com.example.springsecurity.config.oauth;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //여기서 후처리 = 후처리 되는 함수 = 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest.getClientRegistration());
        System.out.println("userAccessToken:"+userRequest.getAccessToken());
        System.out.println("userAccessTokenValue:"+userRequest.getAccessToken().getTokenValue()); //getAccessToken
        System.out.println("userAttributes:"+super.loadUser(userRequest).getAttributes()); //여기에 모든 정보가 들어있음

        
        return super.loadUser(userRequest);
    }
}
