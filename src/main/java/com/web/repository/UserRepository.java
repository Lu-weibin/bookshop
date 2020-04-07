package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.User;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface UserRepository extends JpaBaseRepository<User,Integer> {

	/**
	 * 通过用户名查询用户
	 */
	User findTop1ByUsernameAndUserType(String username,int usertype);

	/**
	 * 通过邮箱查询用户
	 */
	User findTop1ByEmailAndUserType(String email, int usertype);

	/**
	 * 根据激活码查找用户
	 */
	User findTop1ByCode(String code);
}
