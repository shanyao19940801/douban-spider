package com.yao.spider.zimuku.task;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zhihu.entity.User;
import com.yao.spider.zhihu.parsers.ZhiHuUserParser;
import com.yao.spider.zimuku.domain.ZimuInfo;
import com.yao.spider.zimuku.parsers.ZimuParser;
import com.yao.spider.zimuku.service.ZimuInfoService;
import com.yao.spider.zimuku.service.impl.ZimuInfoServiceImpl;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @create by  单耀
 * @create date  2020/3/25
 */
public class ZimuPageListTask extends AbstractTask<ZimuPageListTask> {
    private String url;
    SqlSession session;
    public void run() {
        getPage(url);
    }

    public void retry() {

    }

    public void handle(Page page) {
        IPageParser pageParser = ParserFactory.getParserClass(ZimuParser.class);
        if (pageParser != null) {
            List<ZimuInfo> list = pageParser.parser(page.getHtml());
            ZimuInfoService service = new ZimuInfoServiceImpl();
            service.batchInsert(list, this.session);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SqlSession getSession() {
        return session;
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }

    public static void main(String[] args) {
        ZimuPageListTask task = new ZimuPageListTask();
        SqlSession session = MyBatiesUtils.getSqlSession();
        task.setSession(session);
        task.getPage("http://www.zimuku.la/t/HJns0?p=2");
        session.close();
    }


}
