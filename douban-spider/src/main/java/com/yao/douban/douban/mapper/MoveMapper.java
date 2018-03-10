package com.yao.douban.douban.mapper;

import com.yao.douban.douban.entity.Move;

public interface MoveMapper {
    int deleteByPrimaryKey(String id);

    int insert(Move record);

    int insertSelective(Move record);

    Move selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Move record);

    int updateByPrimaryKey(Move record);
}