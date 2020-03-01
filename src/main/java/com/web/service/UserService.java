package com.web.service;

import com.base.BaseService;
import com.web.pojo.User;

/**
 * @author luwb
 * @date 2020/02/25
 */
public interface UserService extends BaseService<User,Integer> {

	/**
	 * 根据用户名查询用户信息
	 */
	User findOneByUsername(String username);

	User findOneByUsernameAndPassword(User user);
}
