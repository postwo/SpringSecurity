package com.example.springsecurity.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인을 진행이 완료가 되면 session을 만들어준다 (Security ContextHolder = 세션정보를 저장)
// 오브젝트 타입=> Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 된다
// User 오브젝트탕비 => UserDeatils 타입 객체

// Security session => Authentication => UserDetails(PrincipalDetails)

import com.example.springsecurity.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user; // 콤포지션

    public PrincipalDetails(User user){
        this.user =user;
    }


    //해당 user의 권한을 리턴하는 곳 == user에 있는 권한은String 타입이여서 여기서 못쓰기 때문에 변경해준다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>(); //GrantedAuthority 타입을 생성
        collect.add(new GrantedAuthority() { //add 한다음에 new GrantedAuthority() 생성해서 user에 있는 권한을 넘겨주면 된다
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겼니?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정에 비밀번호가 너무 오래 사용한거 아니야?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화 되었니?
    @Override
    public boolean isEnabled() {
        // 우리 사이트 1년 동안 회언이 로그인을 안하면 휴면 계정으로 하기로 함
        // 현재시간 - 로긴시간 => 1년을 초과하면 retun을 false로 반환
        return true;
    }
}
