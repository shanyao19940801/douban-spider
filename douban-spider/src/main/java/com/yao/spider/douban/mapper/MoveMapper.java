package com.yao.spider.douban.mapper;


import com.yao.spider.douban.entity.move.Move;

public interface MoveMapper {
    int deleteByPrimaryKey(String id);

    int insert(Move record);

    int insertSelective(Move record);

    Move selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Move record);

    int updateByPrimaryKey(Move record);
}