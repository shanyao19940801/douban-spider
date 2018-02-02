package com.yao.douban.douban.parsers.move;

import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class MoveParser implements DoubanPageParser<Move>{
    public  List<Move> parser(String html) {
        List<Move> list = new ArrayList<Move>();
        Move move= new Move();
        move.setId("1");
        move.setName("test");
        list.add(move);
        return list;
    }
}
