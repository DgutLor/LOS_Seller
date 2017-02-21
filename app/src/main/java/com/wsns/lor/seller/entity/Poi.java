package com.wsns.lor.seller.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 *商家在百度地图数据平台的信息
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Poi implements Serializable {

    String title;
    int turnover;
    String repairsTypes;
    String address;
    List location;
    int tradeTypes;
    double service;
    double coin;
    String worktime;
    String hotline;
    String notice;


    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public double getService() {
        return service;
    }

    public void setService(double service) {
        this.service = service;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    String account;
    String avatar;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public String getRepairsTypes() {
        return repairsTypes;
    }

    public void setRepairsTypes(String repairsTypes) {
        this.repairsTypes = repairsTypes;
    }

    public int getTradeTypes() {
        return tradeTypes;
    }

    public void setTradeTypes(int tradeTypes) {
        this.tradeTypes = tradeTypes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List getLocation() {
        return location;
    }

    public void setLocation(List location) {
        this.location = location;
    }

}
