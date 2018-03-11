package com.yao.douban.douban.dao;


import com.yao.douban.douban.entity.move.Move;

import java.util.List;

/**
 * Created by shanyao on 2018/3/10.
 */
public interface IMoveDao{
    public void insert(Move move);

    void inserSelective(Move move);

    void insertList(List<Move> moveList);

    Move selectByPrimaryKey(String id);
}
