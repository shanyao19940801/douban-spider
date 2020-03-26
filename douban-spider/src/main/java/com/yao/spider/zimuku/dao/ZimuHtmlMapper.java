package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuHtml;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZimuHtmlMapper {
    int insert(ZimuHtml record);

    ZimuHtml selectByPrimaryKey(Long id);

    ZimuHtml selectMax();

    List<ZimuHtml> selectByRange(@Param("startId") Long startId, @Param("endId") Long endId);

}