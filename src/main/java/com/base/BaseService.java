package com.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T extends Serializable,ID> {

	JpaBaseRepository<T, ID> getRepository();

	Optional<T> findById(ID id);

	List<T> findAll();

	T save(T t);

	void delete(ID id);

	List<T> findAll(String orderBy, Sort.Direction direction);

	Page<T> findAll(int pageIndex, int pageSize);

}
