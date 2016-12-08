package im.sdk.debug.application;

import android.app.Application;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by ${chenyn} on 16/3/22.
 *
 * @desc :
 */
public class IMDebugApplication extends Application {

    private static IMDebugApplication instance;
    public static IMDebugApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Log.i("IMDebugApplication", "init");
        JMessageClient.init(getApplicationContext());
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
    }
}

