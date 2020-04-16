package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

	/**
	 * 根据图书id查找购物该图书的用户
	 */
	@Query(value = "select * from user where id = (select user_id from orders where id = (select order_id from order_details WHERE state in (2,4) and book_id = :bookId))", nativeQuery = true)
	User findOneByBookid(@Param("bookId") int bookId);

	/**
	 * 查找所有用户
	 */
	List<User> findAllByUserType(int userType);

}
