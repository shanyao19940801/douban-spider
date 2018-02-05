package com.yao.douban.douban.task;

import com.yao.douban.douban.entity.move.ListMove;
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
    private ListMove move;
    private boolean isUseProxy;

    public DouBanDetailInfoDownLoadTask(ListMove move, boolean isUseProxy) {
        this.move = move;
        this.isUseProxy = isUseProxy;
    }

    public void run() {
        if (move!= null){
            //组装请求url，通过代理获取信息
            String url = move.getUrl();

        }
    }

    private void handle(Page page) {
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParser.class);
        List<Move> list = parser.parser(page.getHtml());
        if (list != null && list.size() > 0) {
            Move _move = list.get(0);
            _move.setName(move.getTitle());
            _move.setId(move.getId());
            _move.setRating(move.getRate());
            //保存到数据库
            System.out.println(_move.toString());
        }
    }
}
