package com.yao.spider.sqkfq.dao;

import com.yao.spider.sqkfq.domain.SqkfqBaoming;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SqkfqBaomingMapper {
    int insert(SqkfqBaoming record);

    SqkfqBaoming selectByPrimaryKey(Long id);

    List<SqkfqBaoming> selectAll();

    List<SqkfqBaoming> selectByZipCodeAndOpt(@Param("code") Integer code, @Param("opt") Long opt);
}