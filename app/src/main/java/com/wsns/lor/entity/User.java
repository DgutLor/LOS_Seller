package com.wsns.lor.entity;

/**
 * Created by Administrator on 2016/10/2.
 */

public class User {
    private String hxid;//环信id
    private String nickname;//昵称
    private String password; // 密码
    private String avatar;//照片名
    private String sex;//性别
    private String type; // 类型

    public String toString(){
        return "hxid="+hxid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

