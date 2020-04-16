package com.web.repository;

import com.base.JpaBaseRepository;
import com.web.pojo.Address;
import com.web.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author luwb
 * @date 2020-02-29
 */
public interface AddressRepository extends JpaBaseRepository<Address,Integer>{

    List<Address> findAllByUserAndStateNot(User user,int state);

    Address findFirstByUserAndState(User user,int state);

    @Modifying
    @Query(value = "update address set state=1 where user_id=:userId and state=2;",nativeQuery = true)
    void updateState(@Param("userId") Integer userId);

}
