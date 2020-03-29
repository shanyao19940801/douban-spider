package com.yao.spider.exception;

import org.mule.config.i18n.Message;

/**
 * Created by shanyao on 2020/3/29
 */
public class SpiderRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6728041560892553159L;

    public SpiderRuntimeException(Message message) {
        super(message.getMessage());
    }

    public SpiderRuntimeException(Message message, Throwable cause) {
        super(message.getMessage(), cause);
    }

    public SpiderRuntimeException(Throwable cause) {
        super(cause);
    }
}
