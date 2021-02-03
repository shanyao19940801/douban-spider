package com.yao.spider.common.intf;

public interface Condition<T>{
    public static final Condition<Object> TRUE = new Condition<Object>() {
        public boolean match(Object o) {
            return true;
        }
    };

    boolean match(T t);
}