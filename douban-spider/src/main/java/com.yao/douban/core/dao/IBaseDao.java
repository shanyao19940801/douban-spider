package com.yao.douban.core.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * Created by shanyao on 2018/3/10.
 */
public interface IBaseDao<T> {

    public SqlSession getSession();

    public void insert(T extity);
}
