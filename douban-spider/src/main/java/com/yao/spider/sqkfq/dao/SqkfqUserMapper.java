package com.yao.spider.sqkfq.dao;

import com.yao.spider.sqkfq.domain.SqkfqUser;

public interface SqkfqUserMapper {

    int insert(SqkfqUser record);

    SqkfqUser selectByPrimaryKey(Long id);
}