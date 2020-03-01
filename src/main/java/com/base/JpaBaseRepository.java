package com.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by luwb on 2019/11/22.
 * Jpa基类
 */
public interface JpaBaseRepository<T,ID> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {
}
