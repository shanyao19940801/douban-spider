package com.yao.douban.core.dao.Impl;

import com.yao.douban.core.dao.IBaseDao;
import com.yao.douban.core.util.MyBatiesUtils;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.ParameterizedType;

/**
 * Created by shanyao on 2018/3/10.
 */
public class BaseDaoImpl<T> implements IBaseDao<T>{
   /* private Class<T> entityClass;

    public BaseDaoImpl(Class<T> entityClass) {
        ParameterizedType pt = (ParameterizedType) this.getClass()
                .getGenericSuperclass();
        entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }*/

    public SqlSession getSession() {
        return MyBatiesUtils.getSqlSession();
    }

    public void insert(T extity) {
        try {
            getSession().insert(extity.getClass().getName() + ".insert", extity);
            getSession().commit();
        } catch (Exception e) {
            e.printStackTrace();
            getSession().rollback();
        } finally {
            getSession().close();
        }
    }
}
