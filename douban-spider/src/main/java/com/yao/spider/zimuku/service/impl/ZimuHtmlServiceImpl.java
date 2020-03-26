package com.yao.spider.zimuku.service.impl;

import com.yao.spider.zimuku.dao.ZimuHtmlMapper;
import com.yao.spider.zimuku.dao.ZimuInfoMapper;
import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.service.ZimuHtmlService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ZimuHtmlServiceImpl implements ZimuHtmlService {

    public void insert(ZimuHtml zimuHtml, SqlSession session) {
        ZimuHtmlMapper mapper = session.getMapper(ZimuHtmlMapper.class);
        mapper.insert(zimuHtml);
        session.commit();
    }

    public Long selectMaxId(SqlSession session) {
        ZimuHtmlMapper mapper = session.getMapper(ZimuHtmlMapper.class);
        ZimuHtml html = mapper.selectMax();
        return html.getId();
    }

    public List<ZimuHtml> selectByRange(Long startId, Long endId, SqlSession session) {
        ZimuHtmlMapper mapper = session.getMapper(ZimuHtmlMapper.class);
        List<ZimuHtml> htmls = mapper.selectByRange(startId, endId);
        return htmls;
    }
}
