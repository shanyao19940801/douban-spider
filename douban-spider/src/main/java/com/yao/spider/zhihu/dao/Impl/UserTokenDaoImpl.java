package com.yao.spider.zhihu.dao.Impl;

import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zhihu.dao.IUserTokenDao;
import com.yao.spider.zhihu.entity.UserToken;
import com.yao.spider.zhihu.mapper.UserMapper;
import com.yao.spider.zhihu.mapper.UserTokenMapper;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by user on 2018/4/2.
 */
public class UserTokenDaoImpl implements IUserTokenDao {
    public void insertSelective(UserToken userToken) {
        SqlSession session = MyBatiesUtils.getSqlSession();
        UserTokenMapper mapper = session.getMapper(UserTokenMapper.class);
        try {
            UserToken token = mapper.selectByPrimaryKey(userToken.getUserToken());
            if (token == null) {
                mapper.insertSelective(userToken);
                session.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public UserToken selectByPrimaryKey(String userToken) {
        SqlSession session = MyBatiesUtils.getSqlSession();
        UserToken token = new UserToken();
        UserTokenMapper mapper = session.getMapper(UserTokenMapper.class);
        try {
            token = mapper.selectByPrimaryKey(userToken);
        } catch (Exception e) {

        } finally {
            session.close();
        }
        return token;
    }

    public boolean judgeAndInsert(UserToken userToken) {
        SqlSession session = MyBatiesUtils.getSqlSession();
        UserToken token = new UserToken();
        try {
            UserTokenMapper mapper = session.getMapper(UserTokenMapper.class);
            if (mapper.selectByPrimaryKey(userToken.getUserToken()) == null) {
                mapper.insertSelective(userToken);
                session.commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return true;
    }
}
