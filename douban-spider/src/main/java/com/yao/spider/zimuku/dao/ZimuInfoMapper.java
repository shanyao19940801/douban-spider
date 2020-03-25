package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuInfo;

public interface ZimuInfoMapper {
    int insert(ZimuInfo record);

    ZimuInfo selectByPrimaryKey(Long id);
}