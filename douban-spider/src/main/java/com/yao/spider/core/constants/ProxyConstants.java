package com.yao.spider.core.constants;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class ProxyConstants {
    //是否爬取新的的代理
    public static final boolean ISUSERFILE_ONLY = false;
    public static final String STANDARD = "standard";
    public static int SocketTimeout = 10000;
    public static int ConnectionTimeout = 10000;
    public static int TIMEOUT = 10000;
    //代理测试地址
    public static String PROXYTEST_URL = "http://mcar.cc/forum.php";

    public static final long TIME_INTERVAL = 1000;
    //文件路劲地址
//    public static final String FILE_PATH= "src/main/resources/file";
//    public static final String FILE_PATH= "src/main/resources/file";
      public static final String FILE_PATH= "../proxy/proxy.ser";
    public static String RESOURCES__FILE_PATH;
    //保存序列化代理的文件名
    public static String PROXYSER_FILE_NMAE = "proxy.ser";

    public final static String[] userAgentArray = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2623.110 Safari/537.36",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:50.0) Gecko/20100101 Firefox/50.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36"
    };

    static {
        RESOURCES__FILE_PATH = ProxyConstants.class.getClassLoader().getResource("").getPath() + "file";
    }
}
