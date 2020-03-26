package com.yao.spider.zimuku.service;

import com.yao.spider.zimuku.domain.ZimuHtml;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface ZimuHtmlService {
    public void insert(ZimuHtml zimuHtml, SqlSession session);

    Long selectMaxId(SqlSession session);

    List<ZimuHtml> selectByRange(Long startId, Long endId, SqlSession session);
}
