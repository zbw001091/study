package com.zbw.big.study.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zbw.big.study.dao.User;
import com.zbw.big.study.repository.UserMapper;
import com.zbw.big.study.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	@Override
	public List<User> getAllUsers() {
		return userMapper.selectAll();
	}

}
