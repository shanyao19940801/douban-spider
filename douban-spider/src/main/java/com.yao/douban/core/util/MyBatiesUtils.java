package com.yao.douban.core.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by shanyao on 2018/3/10.
 */
public class MyBatiesUtils {
    private static Logger logger = LoggerFactory.getLogger(MyBatiesUtils.class);

    public static SqlSessionFactory getSqlSessionFactory() {
        InputStream inputStream = MyBatiesUtils.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    public static SqlSession getSqlSession(){
        SqlSession session = null;
        try {

            session = getSqlSessionFactory().openSession();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return session;
    }

    /**
     *
     * @param isAutocommit 在sqlSession对象执行完后是否自动提交
     *                     true是，false 否
     * @return
     */
    public static SqlSession getSqlSession(boolean isAutocommit) {
        return getSqlSessionFactory().openSession(isAutocommit);
    }
}
