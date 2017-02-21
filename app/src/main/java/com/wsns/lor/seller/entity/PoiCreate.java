package com.wsns.lor.seller.entity;

/**
 * 百度地图数据创建结果封装
 */
public class PoiCreate {

    private int status;
    private String message;
    private String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
