package com.yao.spider.sqkfq.service;

import com.yao.spider.sqkfq.domain.SqkfqBaoming;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface SqkfqBaomingService {
    public void insert(SqkfqBaoming sqkfqBaoming, SqlSession session);

    public List<SqkfqBaoming> selectAll(SqlSession session);

    public List<SqkfqBaoming> selectByZipCodeAndOpt(SqlSession session, Integer code, Long opt);
}
