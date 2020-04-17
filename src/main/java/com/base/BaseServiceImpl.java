package com.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends Serializable, ID> implements BaseService<T, ID> {

	@Override
	public abstract JpaBaseRepository<T, ID> getRepository();

	@Override
	public Optional<T> findById(ID id) {
		return getRepository().findById(id);
	}

	@Override
	public List<T> findAll() {
		return getRepository().findAll();
	}

	@Override
	public T save(T t) {
		return getRepository().save(t);
	}

	@Override
	public void delete(ID id) {
		getRepository().deleteById(id);
	}

	@Override
	public List<T> findAll(String orderBy, Sort.Direction direction){
		Sort sort = new Sort(direction, orderBy);
		return this.getRepository().findAll(sort);
	}

	@Override
	public Page<T> findAll(int pageIndex, int pageSize) {
		Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
		return this.getRepository().findAll(pageable);
	}
}
