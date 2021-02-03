package com.yao.spider.sqkfq.manager;

import com.yao.spider.common.OKHttp2Utils;
import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.sqkfq.domain.SqkfqBaoming;
import com.yao.spider.sqkfq.domain.SqkfqUser;
import com.yao.spider.sqkfq.parses.SqkfaUserParser;
import com.yao.spider.sqkfq.service.SqkfqBaomingService;
import com.yao.spider.sqkfq.service.SqkfqBaomingServiceImpl;
import com.yao.spider.sqkfq.service.SqkfqUserService;
import com.yao.spider.sqkfq.service.SqkfqUserServiceImpl;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 14:42
 */
public class UserInfoDetailManager {
    public static void getUserInfo() {
        String idCard = "321321199408017856";
        SqlSession session = MyBatiesUtils.getSqlSession();
        SqkfqBaomingService service = new SqkfqBaomingServiceImpl();
        SqkfqUserService userService = new SqkfqUserServiceImpl();
        List<SqkfqBaoming> sqkfqBaomings = service.selectByZipCodeAndOpt(session, 57, 3001L);
        for (SqkfqBaoming baoming : sqkfqBaomings) {
            SqlSession session1 = MyBatiesUtils.getSqlSession();
            String s = simpleRequest(baoming.getUserMid(), idCard);
            SqkfqUser parser = SqkfaUserParser.parser(s);
            parser.setUserMid(baoming.getUserMid());
            idCard = parser.getUserIdCard();
            userService.insert(session1, parser);
            System.out.println(parser.toString());
        }
    }

    public static String simpleRequest(Long mid, String idCard) {
        String result = null;
        String url = "http://www.sqjkqrc.com/JoinSys/SysMain/Members/MyResume.aspx";
        try {
            String cookie = "ASP.NET_SessionId=qktncfx02ed4odyihga3izo5; SYSLogin=MID=" + mid + "&LoginName=" + idCard;
            result = OKHttp2Utils.sendPostWithHeaders(url, getHeader(cookie));
        } catch (IOException e) {
            try {
                // 发生异常重新请求一次
                result = OKHttp2Utils.sendPost(url, "");
            } catch (IOException e1) {

            }
        }
        return result;
    }

    public static Map<String, String> getHeader(String cokie) {
        Map<String, String> header = new HashMap<>();

        header.put("Cookie", cokie);
        return header;
    }

    public static void main(String[] args) {
        getUserInfo();
    }
}
