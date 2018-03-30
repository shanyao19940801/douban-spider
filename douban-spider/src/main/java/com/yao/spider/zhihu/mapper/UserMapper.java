package com.yao.spider.zhihu.mapper;

import com.yao.spider.zhihu.entity.User;

public interface UserMapper {

    int deleteByPrimaryKey(String userToken);

    int insert(User record);

    int insertSelective(User record);


    User selectByPrimaryKey(String userToken);



    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}