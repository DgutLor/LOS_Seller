package com.wsns.lor.seller.http;


import com.wsns.lor.seller.entity.PoiCreate;
import com.wsns.lor.seller.entity.Orders;
import com.wsns.lor.seller.entity.OrdersProgress;
import com.wsns.lor.seller.entity.Page;
import com.wsns.lor.seller.entity.Poi;
import com.wsns.lor.seller.entity.Records;
import com.wsns.lor.seller.entity.RepairGoods;
import com.wsns.lor.seller.entity.ServerDate;
import com.wsns.lor.seller.entity.User;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liukun on 16/3/9.
 */
public class HttpMethods {

    public static final String BASE_URL = "http://192.168.191.1:8080/Lor/";

    private static final int DEFAULT_TIMEOUT = 5;

    private static Retrofit retrofit;
    public static LORService lorService;
    public static OkHttpClient client;

    static {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }



    //构造方法私有
    private HttpMethods() {
        lorService = retrofit.create(LORService.class);
    }



    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        System.out.println("访问了:" + BASE_URL);
        return SingletonHolder.INSTANCE;
    }


    public void getCurrentUser(Subscriber<ServerDate<List<RepairGoods>>> subscriber) {

        Observable observable = lorService.getCurrentUser()
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    public void getMyRecordsPage(Subscriber<ServerDate<List<Records>>> subscriber, int page) {

        Observable observable = lorService.getMyRecordsPage(page)
                .map(new HttpResultFunc());
        toSubscribe(observable, subscriber);
    }

    /**
     * 用于维修商品的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param seller_id  商家id
     */
    public void getGoodsResult(Subscriber<ServerDate<List<RepairGoods>>> subscriber, String seller_id) {

        Observable observable = lorService.getGoodsResult(seller_id)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }


    public void getOrdersProgressPage(Subscriber<ServerDate<Page<OrdersProgress>>> subscriber,
                                      int page, int orders_id
    ) {

        Observable observable = lorService.getOrdersProgressPage(page, orders_id)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void addOrdersProgress(Subscriber<ServerDate<OrdersProgress>> subscriber,
                                  String content, String title, int orders_id, int state) {

        Observable observable = lorService.addOrdersProgress(content, title, orders_id, state)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void getMyOrderPage(Subscriber<ServerDate<Page<Orders>>> subscriber,
                               int page) {

        Observable observable = lorService.getMyOrderPage(page)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void updateAvatar(Subscriber<ServerDate<User>> subscriber,
                             RequestBody avatar) {

        Observable observable = lorService.updateAvatar(avatar)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void updatePassword(Subscriber<ServerDate<User>> subscriber,
                               String newpassword) {

        Observable observable = lorService.updatePassword(newpassword)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void updateName(Subscriber<ServerDate<User>> subscriber,
                           String username) {

        Observable observable = lorService.updateName(username)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void forgetPassword(Subscriber<ServerDate<User>> subscriber,
                               String account, String newpassword) {

        Observable observable = lorService.forgetPassword(account, newpassword)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void findUser(Subscriber<ServerDate<User>> subscriber,
                         String account) {

        Observable observable = lorService.findUser(account)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }


    public void addRepairGoods(Subscriber<ServerDate<List<RepairGoods>>> subscriber,
                               List<RepairGoods> goodsList) {

        Observable observable = lorService.addRepairGoods(goodsList)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    public void updatePoi(Subscriber<ServerDate<Poi>> subscriber,
                          Poi poi) {

        Observable observable = lorService.updatePoi(poi)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }
    public void createSeller(Subscriber<PoiCreate> subscriber, Poi poi) {

        Observable observable = lorService.createSeller(poi)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用于获取登录用户的数据
     */
    public void getUserData(Subscriber<ServerDate<User>> subscriber, String username, String password) {

        Observable observable = lorService.getUserDate(username, password)
                .map(new HttpResultFunc());

        toSubscribe(observable, subscriber);
    }


    public static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    private class HttpResultFunc<T> implements Func1<ServerDate<T>, T> {

        @Override
        public T call(ServerDate<T> httpResult) {
            if (httpResult.getCode() != 1) {
                throw new ApiException(httpResult.getMessege());
            }
            return httpResult.getData();
        }
    }

}
