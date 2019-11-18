package com.zbw.big.study.repository;

import java.util.List;

import com.zbw.big.study.dao.User;

public interface UserMapper {
    
    int insert(User record);

    User selectByUserId(Integer userId);

	List<User> selectAll();
}