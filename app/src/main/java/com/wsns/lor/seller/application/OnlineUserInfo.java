package com.wsns.lor.seller.application;

import com.wsns.lor.seller.entity.Poi;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2016/10/23.
 */

public class OnlineUserInfo {
    public static UserInfo myInfo= JMessageClient.getMyInfo() ;
    public static double latitude ;
    public static double longitude ;
    public static String address;
    public static Poi poi;

}
