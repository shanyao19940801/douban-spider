package com.yao.spider.douban.task.move;

import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.utils.DBUtil;

import java.util.Map;

/**
 * Created by user on 2018/2/8.
 */
public class StartWithTypeTask implements Runnable{
    private static DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private static volatile boolean SUCCESS = true;
    public void run() {
            Map<String,String> mapTpe = DBUtil.getType("move");
            for (String name : mapTpe.keySet()) {
                doubanHttpClient.getDownLoadMoveListExector().execute(new SpiderWithTypeTask(name, mapTpe.get(name), true));
            }
    }


}
