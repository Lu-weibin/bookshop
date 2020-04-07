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
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public JpaBaseRepository<User, Integer> getRepository() {
        return this.userRepository;
    }

    @Override
    public User findOneByUsername(String username, int userType) {
        return userRepository.findTop1ByUsernameAndUserType(username, userType);
    }

    @Override
    public User findOneByEmailAndPassword(User user) {
        User user1 = userRepository.findTop1ByEmailAndUserType(user.getEmail(), user.getUserType());
        if (user1 != null && user1.getPassword().equals(ShaUtils.encrypt(user.getPassword()))) {
            user1.setPassword("");
            return user1;
        }
        return null;
    }

    @Override
    public User findOneByEmail(String email, int userType) {
        return userRepository.findTop1ByEmailAndUserType(email, userType);
    }

    @Override
    public User findOneByCode(String code) {
        return userRepository.findTop1ByCode(code);
    }

}
