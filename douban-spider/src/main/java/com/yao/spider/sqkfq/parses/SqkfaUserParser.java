package com.yao.spider.sqkfq.parses;

import com.yao.spider.sqkfq.domain.SqkfqUser;
import com.yao.spider.zimuku.domain.ZimuInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 17:00
 */
public class SqkfaUserParser {
    public static  SqkfqUser parser(String html) {
        Document document = Jsoup.parse(html);
        Elements oddList = document.select(".table-c");
        Elements tr = document.select("tr");
        Elements td = tr.select("td");
        SqkfqUser user = new SqkfqUser();
        user.setUserName(td.get(1).text());
        user.setUserSex(td.get(3).text());
        user.setUserBirthday(td.get(5).text());
        user.setUserHeaderUrl(td.get(6).select("img").attr("src"));
        user.setUserIdCard(td.get(10).text());
        user.setUserJiguan(td.get(13).text());
        user.setUserMerage(td.get(16).text());
        user.setUserZzmm(td.get(18).text());
        user.setUserSg(td.get(24).text());
        user.setUserWeight(td.get(26).text());
        user.setUserAddress(td.get(28).text());
        user.setUserPhone(td.get(30).text());
        user.setUserJinji(td.get(34).text());
        user.setUserJinjiPhone(td.get(36).text());
        user.setUserXuli(td.get(40).text());
        user.setUserZhuanye(td.get(42).text());
        return user;

    }


}
