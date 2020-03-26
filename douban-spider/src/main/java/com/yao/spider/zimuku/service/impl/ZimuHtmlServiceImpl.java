package com.yao.spider.zimuku.service.impl;

import com.yao.spider.zimuku.dao.ZimuHtmlMapper;
import com.yao.spider.zimuku.dao.ZimuInfoMapper;
import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.service.ZimuHtmlService;
import org.apache.ibatis.session.SqlSession;

public class ZimuHtmlServiceImpl implements ZimuHtmlService {

    public void insert(ZimuHtml zimuHtml, SqlSession session) {
        ZimuHtmlMapper mapper = session.getMapper(ZimuHtmlMapper.class);
        mapper.insert(zimuHtml);
        session.commit();
    }
}
