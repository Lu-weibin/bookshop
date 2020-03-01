package com.web.service.impl;

import com.base.BaseServiceImpl;
import com.base.JpaBaseRepository;
import com.web.pojo.Collection;
import com.web.repository.CollectionRepository;
import com.web.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luwb
 * @date 2020-02-29
 */
@Service
public class CollectionServiceImpl extends BaseServiceImpl<Collection,Integer> implements CollectionService{

    @Autowired
    private CollectionRepository collectionRepository;

    @Override
    public JpaBaseRepository<Collection, Integer> getRepository() {
        return this.collectionRepository;
    }
}
