package com.wsns.lor.entity;

/**
 * Created by liukun on 16/3/5.
 */
public class UserHttpResult<T> {

    private String code;

    //用来模仿Data
    private T user;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("code=" );
            if (null != user) {
                sb.append("user:" + user.toString());
            }
        return sb.toString();
    }
}
