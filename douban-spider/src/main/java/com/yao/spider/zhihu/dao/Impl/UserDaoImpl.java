package com.yao.spider.zhihu.dao.Impl;

import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zhihu.dao.IUserDao;
import com.yao.spider.zhihu.entity.User;
import com.yao.spider.zhihu.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shanyao on 2018/3/29.
 */
public class UserDaoImpl implements IUserDao{
    private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    public int inserSelective(User user) {
        SqlSession session = MyBatiesUtils.getSqlSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            if (user != null) {
                User u = mapper.selectByPrimaryKey(user.getUserToken());
                if (u == null) {
                    mapper.insertSelective(user);
                    session.commit();
                    return 1;
                } else {
                    mapper.updateByPrimaryKey(user);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return 0;
    }
}
