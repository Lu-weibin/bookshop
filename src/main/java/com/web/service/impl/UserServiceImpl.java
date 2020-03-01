package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.User;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import com.web.util.JwtUtil;
import com.web.util.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luwb
 * @date 2020/02/25
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User,Integer> implements UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public JpaBaseRepository<User,Integer> getRepository() {
		return this.userRepository;
	}

	@Override
	public User findOneByUsername(String username) {
		return userRepository.findTop1ByUsername(username);
	}

	@Override
	public User findOneByUsernameAndPassword(User user) {
		User user1 = userRepository.findTop1ByUsername(user.getUsername());
		if (user1.getPassword().equals(ShaUtils.encrypt(user.getPassword()))) {
			user1.setPassword("");
			return user1;
		}
		return null;
	}
}
