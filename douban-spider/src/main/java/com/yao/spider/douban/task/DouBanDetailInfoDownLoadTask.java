package com.yao.spider.douban.task;

import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.task.*;
import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.dao.IMoveDao;
import com.yao.spider.douban.dao.Impl.MoveDaoImpl;
import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.douban.parsers.move.MoveDetailInfoParser;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.core.entity.Page;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.util.ProxyUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/28.
 * 电影详细信息下载任务
 */
public class DouBanDetailInfoDownLoadTask extends AbstractTask<DouBanInfoListPageTask> implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DouBanInfoListPageTask.class);
    private Move move;
    private boolean isUseProxy;
    private Proxy currentProxy;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private IMoveDao dao = new MoveDaoImpl();

    public DouBanDetailInfoDownLoadTask(Move move, boolean isUseProxy) {
        this.move = move;
        this.isUseProxy = isUseProxy;
        if (move != null) super.url = move.getUrl();
    }

    public void run() {
        getPage(url);
    }

    public void retry() {

    }

    public void handle(Page page) {
        IPageParser parser = ParserFactory.getParserClass(MoveDetailInfoParser.class);
        List<Move> list = parser.parser(page.getHtml());
        if (list != null && list.size() > 0) {
            Move _move = list.get(0);
            _move.setId(move.getId());
            dao.update(_move);
//            _move.setName(move.getTitle());
//            _move.setId(move.getId());
//            _move.setScore(move.getRate());
            //保存到数据库
//            logger.info(_move.toString());
        }
    }
}
