package com.yao.spider.zhihu.mapper;

import com.yao.spider.zhihu.entity.UserToken;

public interface UserTokenMapper {
    int deleteByPrimaryKey(String userToken);

    int insert(UserToken record);

    int insertSelective(UserToken record);

    UserToken selectByPrimaryKey(String userToken);
}