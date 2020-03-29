package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.domain.ZimuInfoExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZimuInfoExtendMapper {
    int insert(ZimuInfoExtend record);

    ZimuInfoExtend selectByPrimaryKey(Long id);

    ZimuInfoExtend selectMax();

    List<ZimuInfoExtend> selectByRange(@Param("startId") Long startId, @Param("endId") Long endId);

    void update(ZimuInfoExtend builderZimuInfoExtend);
}