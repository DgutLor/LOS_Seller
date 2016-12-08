package com.wsns.lor.entity;

/**
 * Created by liukun on 16/3/5.
 */
public class GoodsHttpResult<T> {

    private String code;

    //用来模仿Data
    private T support;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getgoods() {
        return support;
    }

    public void setgoods(T support) {
        this.support = support;
    }

    @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("code=" );
            if (null != support) {
                sb.append("goods:" + support.toString());
            }
        return sb.toString();
    }
}
