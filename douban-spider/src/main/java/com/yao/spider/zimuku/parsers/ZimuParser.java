package com.yao.spider.zimuku.parsers;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.zimuku.domain.ZimuInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZimuParser implements IPageParser<ZimuInfo> {
    private static final Logger logger = LoggerFactory.getLogger(ZimuParser.class);
    private static final String host = "http://www.zimuku.la/";

    public List<ZimuInfo> parser(String html) {
        Document document = Jsoup.parse(html);
        Elements oddList = document.select(".odd");
        Elements evenList = document.select(".even");
        List<ZimuInfo> list = new ArrayList<ZimuInfo>(30);
        for (Element element : oddList) {
            try {
                list.add(buildBean(element));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Element element : evenList) {
            try {
                list.add(buildBean(element));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static ZimuInfo buildBean( Element element) {
        ZimuInfo info = new ZimuInfo();
        Elements select = element.select("a");
        fillSubInfo(info, select);

        Element detail = select.get(1);
        info.setZimuId(Long.valueOf(getZimuId(detail)));
        info.setZimuTitle(detail.attr("title"));
        info.setDetailUrl(host + detail.attr("href"));
        info.setZimuType(1);
        info.setZimuTranslator(getTranslator(element));
        info.setZimuQuality(zimuQuality(element));
        info.setZimuLanguage(langInt(element));
        return info;
    }

    public static ZimuInfo getBeanWithHtml( String html) {
        Document document = Jsoup.parse(html);
        ZimuInfo info = new ZimuInfo();
        Elements select = document.select("a");
        fillSubInfo(info, select);

        Element detail = select.get(1);
        info.setZimuId(Long.valueOf(getZimuId(detail)));
        info.setZimuTitle(detail.attr("title"));
        info.setDetailUrl(host + detail.attr("href"));
        info.setZimuType(1);
        info.setZimuTranslator(getTranslator(document));
        info.setZimuQuality(zimuQuality(document));
        info.setZimuLanguage(langInt(document));
        return info;
    }

    private static void fillSubInfo(ZimuInfo info, Elements select) {
        try {
            Element subElement = select.get(0);
            info.setSubId(getSubId(subElement));
            info.setSubName(subElement.attr("title"));
        } catch (NumberFormatException e) {
            logger.warn("no sub info");
        }
    }

    private static String getTranslator(Element element) {
        Elements select = element.select(".label").select(".label-danger");
        return select.get(0).childNode(0).attr("text");
    }

    private static String getZimuId( Element subElement) {
        String subId = subElement.attr("href");
        int startIndex = subId.lastIndexOf("/");
        int endIndex = subId.indexOf(".");
        String id = subId.substring(startIndex + 1, endIndex);
        return id;
    }

    private static Long getSubId( Element subElement) {
        String subId = subElement.attr("href");
        if (StringUtils.isEmpty(subId)) {
            return null;
        }
        int startIndex = subId.lastIndexOf("/");
        int endIndex = subId.indexOf(".");
        String id = subId.substring(startIndex + 1, endIndex);
        return Long.valueOf(id);
    }

    private static float zimuQuality(Element elements) {
        Elements select = elements.select(".tac").select(".hidden-xs");
        Element element = select.get(0);
        String title = element.child(0).attr("title");
        int start = title.indexOf(":");
        return Float.valueOf(title.substring(start + 1, title.length() - 1));
    }
    private static float zimuQualityForMysqlHtml(Document elements) {
        Elements select = elements.select(".tac").select(".hidden-xs");
        Element element = select.get(0);
        String title = element.child(0).attr("title");
        int start = title.indexOf(":");
        return Float.valueOf(title.substring(start + 1, title.length() - 1));
    }

    private static int langInt(Element element) {
        Elements lang = element.select(".tac").select(".lang");
        Elements langImgs = lang.select("img");
        int sum = 0;
        for (Element lan : langImgs) {
            if ("简体中文字幕".equals(lan.attr("title"))) {
                sum += 1 << 3;
            }
            if ("繁體中文字幕".equals(lan.attr("title"))) {
                sum += 1 << 1;
            }
            if ("English字幕".equals(lan.attr("title"))) {
                sum += 1 << 2;
            }
            if ("双语字幕".equals(lan.attr("title"))) {
                sum += 1 << 0;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
      ZimuParser parser = new ZimuParser();
        ZimuInfo parser1 = parser.getBeanWithHtml("<html lang=\"zh-CN\"><head></head><body><table class=\"table\"><tbody>\uFEFF<td class=\"first\"><div  class=\"l mr10\"><a target=\"_blank\" href=\"/subs/50823.html\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"反恐特警组 第三季 (2019)\"><img alt=\"反恐特警组 第三季 (2019) 字幕下载\" src=\"/images/v2/no_litpic.gif\" class=\"lazy\" data-original=\"//static.zimuku.la/Picture/litpic/2019/05Oct2019210422.jpg\" width=\"32px\"></a></div><a href=\"/detail/132673.html\" target=\"_blank\" title=\"反恐特警队 第三季第十七集【YYeTs字幕组 简繁英双语字幕】S.W.A.T.2017.S03E17.720p.HDTV.x264-KILLERS\"><b>反恐特警队 第三季第十七集【YYeTs字幕组 简繁英双语字幕】S.W.A.T.2017.S03E17.720p.HDTV.x264-KILLERS</b></a><span class=\"label label-info\">SRT</span>&nbsp;<span class=\"label label-info\">ASS/SSA</span>&nbsp;<br><span class=\"gray\"><em>制作：</em>见字幕文件</span><span class=\"gray\"><em>校订：</em>见字幕文件</span><span class=\"gray\"><em>来源：</em><span class=\"label label-danger\">YYeTs字幕组</span></span></td><td  class=\"tac lang\"><img border=\"0\" src=\"/images/v2/flag/china.gif\" alt=\"简体中文字幕\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"简体中文字幕\" style=\"padding-bottom: 2px\"><img border=\"0\" src=\"/images/v2/flag/hongkong.gif\" alt=\"繁體中文字幕\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"繁體中文字幕\" style=\"padding-bottom: 2px\"><img border=\"0\" src=\"/images/v2/flag/uk.gif\" alt=\"English字幕\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"English字幕\" style=\"padding-bottom: 2px\"><img border=\"0\" src=\"/images/v2/flag/jollyroger.gif\" alt=\"双语字幕\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"双语字幕\" style=\"padding-bottom: 2px\"></td><td class=\"tac hidden-xs\"><i class=\"rating-star allstar00\" class=\"tooltips\" data-toggle=\"tooltip\" data-placement=\"top\" title=\"字幕质量:0分\"></i></td><td class=\"tac hidden-xs\">35</td><td class=\"last hidden-xs\"><span class=\"glyphicon glyphicon-user\"></span> <a href=\"/u/4Q5bQmCs0\">风吹来的那片云</a><br><span class=\"glyphicon glyphicon-time\"></span> 57分钟前</td></tbody></table></body></html>");
        System.out.println(parser);
    }
}
