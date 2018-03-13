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

    public static SqlSessionFactory sqlSessionFactory;

    static {
        InputStream inputStream = MyBatiesUtils.class.getResourceAsStream("/mybatis-config.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        InputStream inputStream = MyBatiesUtils.class.getResourceAsStream("/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    public static SqlSession getSqlSession(){
        SqlSession session = null;
        try {
            //是个坑，不能每次都创建一个SessionFactory，会导致错误Too many connection的error
            //具体可以看http://blog.csdn.net/u013412772/article/details/73648537
//            session = getSqlSessionFactory().openSession();
            session = sqlSessionFactory.openSession();
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
        return sqlSessionFactory.openSession(isAutocommit);
    }
}
