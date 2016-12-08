package com.wsns.lor.entity;

/**
 * Created by liukun on 16/3/5.
 */
public class RegisterResult<T> {

    private T code;


    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("code=" );
        return sb.toString();
    }
}
