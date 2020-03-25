package com.yao.spider.zimuku.service;

import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zimuku.domain.ZimuInfo;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public interface ZimuInfoService {
    public void batchInsert(List<ZimuInfo> zimuInfoList, SqlSession session);
}
