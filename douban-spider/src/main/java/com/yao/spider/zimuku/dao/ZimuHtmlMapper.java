package com.yao.spider.zimuku.dao;

import com.yao.spider.zimuku.domain.ZimuHtml;
import org.springframework.stereotype.Repository;

@Repository
public interface ZimuHtmlMapper {
    int insert(ZimuHtml record);

    ZimuHtml selectByPrimaryKey(Long id);
}