package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuInfoExtend;

public interface ZimuInfoExtendMapper {
    int insert(ZimuInfoExtend record);

    ZimuInfoExtend selectByPrimaryKey(Long id);
}