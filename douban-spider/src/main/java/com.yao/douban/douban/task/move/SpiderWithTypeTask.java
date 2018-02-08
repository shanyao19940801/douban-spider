package com.yao.douban.douban.task.move;

import com.yao.douban.core.util.Constants;
import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.constants.DBTypeConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/2/8.
 */
public class SpiderWithTypeTask implements Runnable{
    private static DoubanHttpClient doubanHttpClient = DoubanHttpClient.getInstance();
    private static volatile boolean SUCCESS = true;
    public void run() {
        while (Constants.ISCONTINUE) {

        }
    }


}
