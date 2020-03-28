package com.yao.spider.zimuku.service;

import com.yao.spider.zimuku.domain.ZimuInfoExtend;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by shanyao on 2020/3/28
 */
public interface ZimuInfoExtendService {
    void insert(ZimuInfoExtend zimuInfoExtend, SqlSession session);
}
