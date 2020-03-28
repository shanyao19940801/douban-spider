package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuFileInfo;

public interface ZimuFileInfoMapper {
    int insert(ZimuFileInfo record);

    ZimuFileInfo selectByPrimaryKey(Long id);
}