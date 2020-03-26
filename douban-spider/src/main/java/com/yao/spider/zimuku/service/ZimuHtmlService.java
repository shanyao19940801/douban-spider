package com.yao.spider.zimuku.service;

import com.yao.spider.zimuku.domain.ZimuHtml;
import org.apache.ibatis.session.SqlSession;

public interface ZimuHtmlService {
    public void insert(ZimuHtml zimuHtml, SqlSession session);
}
