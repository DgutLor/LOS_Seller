package com.wsns.lor.entity;

/**
 * Created by liukun on 16/3/5.
 */
public class SellerHttpResult<T> {

    private String code;

    //用来模仿Data
    private T seller;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getSeller() {
        return seller;
    }

    public void setSeller(T seller) {
        this.seller = seller;
    }

    @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("code=" +code);
            if (null != seller) {
                sb.append(" seller:" + seller.toString());
            }
        return sb.toString();
    }
}
