package com.yao.spider.zimuku.service.impl;

import com.yao.spider.zimuku.dao.ZimuInfoExtendMapper;
import com.yao.spider.zimuku.domain.ZimuInfoExtend;
import com.yao.spider.zimuku.service.ZimuInfoExtendService;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by shanyao on 2020/3/28
 */
public class ZimuInfoExtendServiceImpl implements ZimuInfoExtendService {
    public void insert(ZimuInfoExtend zimuInfoExtend, SqlSession session) {
        ZimuInfoExtendMapper mapper = session.getMapper(ZimuInfoExtendMapper.class);
        mapper.insert(zimuInfoExtend);
        session.commit();
    }
}
