package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuInfo;

import java.util.List;

public interface ZimuInfoMapper {
    int insert(ZimuInfo record);

    ZimuInfo selectByPrimaryKey(Long id);

    int batchInsert(List<ZimuInfo> zimuInfoList);
}