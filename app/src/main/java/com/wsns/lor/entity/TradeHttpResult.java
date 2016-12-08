package com.wsns.lor.entity;

/**
 * Created by liukun on 16/3/5.
 */
public class TradeHttpResult<T> {

    private T code;
    private  T orderID;

    public T getOrderID() {
        return orderID;
    }

    public void setOrderID(T orderID) {
        this.orderID = orderID;
    }

    public T getCode() {
        return code;
    }

    public void setCode(T code) {
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" +code);
        sb.append("orderID=" +orderID);

        return sb.toString();
    }
}
