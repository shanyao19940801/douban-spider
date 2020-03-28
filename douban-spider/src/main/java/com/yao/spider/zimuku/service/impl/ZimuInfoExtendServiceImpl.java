package com.yao.spider.zimuku.service.impl;

import com.yao.spider.zimuku.dao.ZimuInfoExtendMapper;
import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.domain.ZimuInfoExtend;
import com.yao.spider.zimuku.service.ZimuInfoExtendService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by shanyao on 2020/3/28
 */
public class ZimuInfoExtendServiceImpl implements ZimuInfoExtendService {
    public void insert(ZimuInfoExtend zimuInfoExtend, SqlSession session) {
        ZimuInfoExtendMapper mapper = session.getMapper(ZimuInfoExtendMapper.class);
        mapper.insert(zimuInfoExtend);
        session.commit();
    }

    public Long selectMaxId(SqlSession session) {
        ZimuInfoExtendMapper mapper = session.getMapper(ZimuInfoExtendMapper.class);
        ZimuInfoExtend html = mapper.selectMax();
        return html.getId();
    }

    public List<ZimuInfoExtend> selectByRange(Long startId, Long endId, SqlSession session) {
        ZimuInfoExtendMapper mapper = session.getMapper(ZimuInfoExtendMapper.class);
        List<ZimuInfoExtend> htmls = mapper.selectByRange(startId, endId);
        return htmls;
    }
}
