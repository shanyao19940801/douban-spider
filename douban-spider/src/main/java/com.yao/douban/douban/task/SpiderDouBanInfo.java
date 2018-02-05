package com.yao.douban.douban.task;

import com.yao.douban.core.util.Constants;
import com.yao.douban.douban.DoubanHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * Created by 单耀 on 2018/2/5.
 */
public class SpiderDouBanInfo implements Runnable {
    public static volatile boolean isContinue = true;
    private DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    //电影开始条数
    public static volatile int MOVE_START = 0;
    private static Logger logger = LoggerFactory.getLogger(SpiderDouBanInfo.class);
    public void run() {
        while (isContinue) {
            //TODO 后期改成可拓展，即可以查询其他信息
            try {
                String url = String.format(Constants.STRTY_URL_MOVE,Constants.TYPE, URLEncoder.encode(Constants.TAG,"UTF-8"), Constants.LIMIT, MOVE_START);
                doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(url, true));
                MOVE_START += Constants.LIMIT;
                Thread.sleep(2000);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
