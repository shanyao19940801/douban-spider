package com.yao.spider.sqkfq.service;

import com.yao.spider.sqkfq.domain.SqkfqUser;
import org.apache.ibatis.session.SqlSession;

public interface SqkfqUserService {
    void insert(SqlSession session, SqkfqUser parser);

    void updateJiguan(SqlSession session1, String jiguan, Long userMind);
}
