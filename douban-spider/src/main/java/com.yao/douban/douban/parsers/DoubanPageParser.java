package com.yao.douban.douban.parsers;

import com.yao.douban.douban.entity.Move;

import java.util.List;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by 单耀 on 2018/1/30.
 */
public interface DoubanPageParser {
    List<Move> parser(String html);
    List<T> parserTest(String html);
}
