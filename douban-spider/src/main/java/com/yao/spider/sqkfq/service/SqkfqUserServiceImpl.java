package com.yao.spider.sqkfq.service;

import com.yao.spider.sqkfq.dao.SqkfqUserMapper;
import com.yao.spider.sqkfq.domain.SqkfqUser;
import org.apache.ibatis.session.SqlSession;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 18:04
 */
public class SqkfqUserServiceImpl implements SqkfqUserService {
    @Override
    public void insert(SqlSession session, SqkfqUser user) {
        SqkfqUserMapper mapper = session.getMapper(SqkfqUserMapper.class);
        mapper.insert(user);
        session.commit();
        session.close();
    }
}
