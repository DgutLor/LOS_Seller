package com.wsns.lor.App;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by Administrator on 2016/10/23.
 */

public class OnlineUserInfo {
    public static UserInfo myInfo= JMessageClient.getMyInfo() ;
    public static LatLonPoint latLonPoint;

}
