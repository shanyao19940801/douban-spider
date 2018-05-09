package com.yao.spider.zhihu.dao;

import com.yao.spider.zhihu.entity.UserToken;

/**
 * Created by user on 2018/4/2.
 */
public interface IUserTokenDao {
    void insertSelective(UserToken userToken);

    public UserToken selectByPrimaryKey(String userToken);

    boolean judgeAndInsert(UserToken userToken);
}
