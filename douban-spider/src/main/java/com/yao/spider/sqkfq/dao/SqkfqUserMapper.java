package com.yao.spider.sqkfq.dao;

import com.yao.spider.sqkfq.domain.SqkfqUser;
import org.apache.ibatis.annotations.Param;

public interface SqkfqUserMapper {

    int insert(SqkfqUser record);

    SqkfqUser selectByPrimaryKey(Long id);

    void updateJiguan(@Param("jiguan") String jiguan, @Param("userMid") Long userMid);
}