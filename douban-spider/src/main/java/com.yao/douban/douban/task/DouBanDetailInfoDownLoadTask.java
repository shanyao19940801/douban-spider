package com.yao.douban.douban.task;

import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveParser;
import com.yao.douban.proxytool.entity.Page;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/28.
 * 电影详细信息下载任务
 */
public class DouBanDetailInfoDownLoadTask implements Runnable {
    private Move move;
    private boolean isUseProxy;

    public DouBanDetailInfoDownLoadTask(Move move) {
        this.move = move;
    }

    public void run() {
        if (move!= null){
            //组装请求url，通过代理获取信息
        }
    }

    private void handle(Page page) {
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParser.class);
        List<Move> list = parser.parser(page.getHtml());
        if (list != null && list.size() > 0) {
            Move _move = list.get(0);
            _move.setName(move.getName());
            _move.setId(move.getId());
            _move.setRating(move.getRating());
            //保存到数据库
        }
    }
}
