package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.User;
import com.web.repository.UserRepository;
import com.web.service.UserService;
import com.web.util.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luwb
 * @date 2020/02/25
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User findOneByEmailAndPassword(String email,String password,Integer userType) {
        User user1 = userRepository.findTop1ByEmailAndUserType(email, userType);
        if (user1 != null && user1.getPassword().equals(ShaUtils.encrypt(password))) {
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

    @Override
    public User findOneByBookId(int bookId) {
        return userRepository.findOneByBookid(bookId);
    }

    @Override
    public List<User> findAllUser() {
        // userType 1为用户；2为管理员
        return userRepository.findAllByUserType(1);
    }

    @Override
    public List<User> search(String email, String username, String phone, Integer state) {
        Specification<User> specification = createSpecification(email, username, phone, state);
        return userRepository.findAll(specification);
    }

    private Specification<User> createSpecification(String email, String username, String phone, Integer state) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (email != null && !"".equals(email)) {
                predicateList.add(cb.like(root.get("email"), "%" + email + "%"));
            }
            if (username != null && !"".equals(username)) {
                predicateList.add(cb.like(root.get("username"), "%" + username + "%"));
            }
            if (phone != null && !"".equals(phone)) {
                predicateList.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }
            if (state != null) {
                predicateList.add(cb.equal(root.get("state"), state));
            }
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
