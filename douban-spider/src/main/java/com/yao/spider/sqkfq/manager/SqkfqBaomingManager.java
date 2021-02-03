package com.yao.spider.sqkfq.manager;

import com.alibaba.fastjson.JSON;
import com.yao.spider.common.OKHttp2Utils;
import com.yao.spider.common.util.CollectionsUtil;
import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.sqkfq.bean.BaoMingDetailBean;
import com.yao.spider.sqkfq.bean.SqjjBean;
import com.yao.spider.sqkfq.domain.SqkfqBaoming;
import com.yao.spider.sqkfq.service.SqkfqBaomingService;
import com.yao.spider.sqkfq.service.SqkfqBaomingServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.mule.util.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 12:30
 */
public class SqkfqBaomingManager {
    public static void getAll(Integer mix) {
        List<SqkfqBaoming> sqkfqBaomings = selectAll();
        Set<Long> userMids = CollectionsUtil.getComposerIdSet(sqkfqBaomings, SqkfqBaoming::getUserMid);
        for (int i = 0;i<mix;i++) {
            if (userMids.contains(Long.valueOf(i))) {
                continue;
            }
            signleRequest(i);
            System.out.println("mid:" + i);
        }

    }

    private static void signleRequest(int i) {
        String s = simpleRequest(i);
        SqjjBean sqjjBean = JSON.parseObject(s, SqjjBean.class);
        for (BaoMingDetailBean detailBean : sqjjBean.getData()) {
            insert(convert(detailBean, Long.valueOf(i)));
            System.out.println(detailBean);
        }
    }

    public static SqkfqBaoming convert(BaoMingDetailBean detailBean, Long userMid) {
        SqkfqBaoming bean = new SqkfqBaoming();
        bean.setAddTime(detailBean.getAddTime());
        bean.setBgm(detailBean.getBGM());
        bean.setEndAppyTime(dateFormate(detailBean.getBMSJE()));
        bean.setStartAppyTime(dateFormate(detailBean.getBMSJB()));
        bean.setEndPayTime(dateFormate(detailBean.getFKSJE()));
        bean.setStartPayTime(dateFormate(detailBean.getFKSJB()));
        bean.setEndPrintTime(dateFormate(detailBean.getDYSJB()));
        bean.setStartPrintTime(dateFormate(detailBean.getDYSJE()));
        bean.setOpt(detailBean.getOPT());
        bean.setState(detailBean.getState());
        bean.setTitle(detailBean.getBT());
        bean.setUserMid(userMid);
        bean.setZpid(detailBean.getZPID());
        return bean;
    }

    public static void insert(SqkfqBaoming baoming) {
        SqlSession session = MyBatiesUtils.getSqlSession();
        SqkfqBaomingService service = new SqkfqBaomingServiceImpl();
        service.insert(baoming, session);
    }

    public static  List<SqkfqBaoming> selectAll() {
        SqlSession session = MyBatiesUtils.getSqlSession();
        SqkfqBaomingService service = new SqkfqBaomingServiceImpl();
        List<SqkfqBaoming> sqkfqBaomings = service.selectAll(session);
        return sqkfqBaomings;
    }

    public static String simpleRequest(int code) {
        String result = null;
        String url = "http://www.sqjkqrc.com/JoinSys/SysMain/Ajax/HandlerJoin.ashx?mid=" + code + "&action=01&self=true&page=1&limit=100";
        try {
            result = OKHttp2Utils.sendPost(url, "");
        } catch (IOException e) {
            try {
                // 发生异常重新请求一次
                result = OKHttp2Utils.sendPost(url, "");
            } catch (IOException e1) {

            }
        }
        return result;
    }
    public static Date dateFormate(String dateString) {
        return DateUtils.getDateFromString(dateString, "yyyy/MM/dd HH:MM:SS");
    }
    public static void main(String[] args) {
//        selectAll();
//        signleRequest(173);
//        Date dateFromString = DateUtils.getDateFromString("2020/4/30 9:00:00", "yyyy/MM/dd HH:MM:SS");;
//        System.out.println(dateFromString);
        getAll(100000);
    }
}
