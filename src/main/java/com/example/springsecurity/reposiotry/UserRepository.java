package com.example.springsecurity.reposiotry;

import com.example.springsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    //findBy 규칙 -> UserName 문법
    //select * from user Where username =1?
    User findByUsername(String username); //유저 검색

}
