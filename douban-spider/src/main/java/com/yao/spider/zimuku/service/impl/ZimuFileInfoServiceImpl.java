package com.yao.spider.zimuku.service.impl;

import com.yao.spider.zimuku.dao.ZimuFileInfoMapper;
import com.yao.spider.zimuku.dao.ZimuInfoMapper;
import com.yao.spider.zimuku.domain.ZimuFileInfo;
import com.yao.spider.zimuku.domain.ZimuInfo;
import com.yao.spider.zimuku.service.ZimuFileInfoService;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by shanyao on 2020/3/28
 */
public class ZimuFileInfoServiceImpl implements ZimuFileInfoService {
    public void insert(ZimuFileInfo fileInfo, SqlSession session) {
        ZimuFileInfoMapper mapper = session.getMapper(ZimuFileInfoMapper.class);
        mapper.insert(fileInfo);
        session.commit();
    }

    public void isnert(ZimuFileInfo zimuInfo, SqlSession session) {
        try {
            ZimuFileInfoMapper mapper = session.getMapper(ZimuFileInfoMapper.class);
            mapper.insert(zimuInfo);
            session.commit();
        } catch (Exception e) {
        }
    }
}
