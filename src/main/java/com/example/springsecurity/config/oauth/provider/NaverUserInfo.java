package com.example.springsecurity.config.oauth.provider;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    //response={id=Dvs8taWRay7edNd4KCEwkoYNq-nWN4kGnh5ybIL0XbA, email=codo7717@naver.com, name=최치언}}
    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }



}
