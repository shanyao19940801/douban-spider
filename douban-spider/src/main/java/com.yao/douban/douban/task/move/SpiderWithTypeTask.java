package com.yao.douban.douban.task.move;

import com.yao.douban.douban.constants.DBConstants;
import com.yao.douban.douban.task.AbstractTask;
import com.yao.douban.douban.task.DouBanInfoListPageTask;
import com.yao.douban.proxytool.entity.Page;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2018/2/8.
 */
public class SpiderWithTypeTask extends AbstractTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(SpiderWithTypeTask.class);
    private String typeName;
    private String typeValue;
    private int currentPersent;
    private int currentStart;
    private Page page;

    public SpiderWithTypeTask(String typeName, String typeValue, boolean isUseProxy) {
        this.typeName = typeName;
        this.typeValue = typeValue;
        super.isUseProxy = isUseProxy;
        super.retryTimes = 0;
    }

    public SpiderWithTypeTask(String typeName, String typeValue, int currentPersent, int currentStart, boolean isUseProxy, int retryTimes) {
        this.typeName = typeName;
        this.typeValue = typeValue;
        super.isUseProxy = isUseProxy;
        super.retryTimes = retryTimes;
    }

    public SpiderWithTypeTask(String url, boolean isUseProxy, int retryTimes) {
        super.url = url;
        super.isUseProxy = isUseProxy;
        super.retryTimes = retryTimes;
    }


    public void run() {
        try {
            if (retryTimes == 0) {
                //获取该标签的总条数
                for (int persent = 100; persent > 0; persent -= 10) {
                    this.currentPersent = persent;
                    String url = String.format(DBConstants.MOVE_PERSENT_COUNT_URL, typeValue, persent, persent - 10);
                    getPage(url);
                    if (page != null) {
                        newMoveListTask(persent);
                        Thread.sleep(1000);
                    }
                }
            } else {
                logger.info("重试：currentPersent=" + currentPersent + "---" + " 减去10" + (currentPersent - 10 ));
                String url = String.format(DBConstants.MOVE_PERSENT_COUNT_URL, typeValue, currentPersent, currentPersent - 10);
                getPage(url);
                if (page != null) {
                    newMoveListTask(currentPersent);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void newMoveListTask(int persent) throws InterruptedException {
        JSONObject object = JSONObject.fromObject(page.getHtml());
        if (object != null) {
            int total = object.getInt("total");
            logger.info("请求成功：total=" + total + "TypeName=" + typeName + "TypeValue=" + typeValue + "Persent=" + persent);
            for (int start = 0; start < total; start += 20) {
                this.currentStart = start;
                String listURL = String.format(DBConstants.MOVE_TOP_LIST_URL, typeValue, persent, persent - 10, start);
                doubanHttpClient.getDownLoadMoveListExector().execute(new DouBanInfoListPageTask(listURL, true));
                Thread.sleep(1000);
            }
        }
    }

    public void retry() {
        logger.info("URL=" + super.url + " 重试次数=" + retryTimes + "--开始编号/百分比：" + currentStart + "/" + currentPersent + "---重试代理：" + currentProxy.getProxyStr() + "---代理失败/成功次数：" + currentProxy.getFailureTimes()+ "/" + currentProxy.getSuccessfulTimes());
        doubanHttpClient.getDownLoadMoveListExector().execute(new SpiderWithTypeTask(typeName, typeValue, currentPersent, currentStart, true, retryTimes + 1));
    }

    public void handle(Page page){
        this.page = page;
    }
}
