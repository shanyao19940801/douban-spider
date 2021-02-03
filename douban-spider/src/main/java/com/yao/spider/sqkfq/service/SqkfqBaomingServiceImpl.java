package com.yao.spider.sqkfq.service;

import com.yao.spider.sqkfq.dao.SqkfqBaomingMapper;
import com.yao.spider.sqkfq.domain.SqkfqBaoming;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class SqkfqBaomingServiceImpl implements SqkfqBaomingService{

    public void insert(SqkfqBaoming sqkfqBaoming, SqlSession session) {
        SqkfqBaomingMapper mapper = session.getMapper(SqkfqBaomingMapper.class);
        mapper.insert(sqkfqBaoming);
        session.commit();
        session.close();
    }

    public List<SqkfqBaoming> selectAll(SqlSession session) {
        SqkfqBaomingMapper mapper = session.getMapper(SqkfqBaomingMapper.class);
        return mapper.selectAll();
    }
}
