package com.yao.test.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by 单耀 on 2018/2/5.
 */
public class EncodeTest {
    public static void main(String[] args) {
        String str = "%3A";
        try {
            String re = URLDecoder.decode(str,"UTF-8");
            System.out.println(re);
            String ss = URLEncoder.encode(re,"UTF-8");
            System.out.println(ss);
            if (ss.equals(str)) {
                System.out.println("==");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
