package com.yao.spider.zimuku.service;

import com.yao.spider.zimuku.domain.ZimuFileInfo;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by shanyao on 2020/3/28
 */
public interface ZimuFileInfoService {
    void insert(ZimuFileInfo fileInfo, SqlSession session);
}
