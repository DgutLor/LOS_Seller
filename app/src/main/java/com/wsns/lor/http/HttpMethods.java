package com.wsns.lor.http;


import com.google.gson.Gson;
import com.wsns.lor.entity.Goods;
import com.wsns.lor.entity.GoodsHttpResult;
import com.wsns.lor.entity.Order;
import com.wsns.lor.entity.OrderHttpResult;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.entity.SellerHttpResult;
import com.wsns.lor.entity.TradeHttpResult;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
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

//    public static final String BASE_URL = "http://192.168.191.1:8080/LORServer/";
//    public static final String BASE_URL = "http://172.27.151.152:8080/LORServer/";
//    public static final String BASE_URL = "http://192.168.43.135:8080/LORServer/";
//    public static final String BASE_URL = "http://115.28.58.198/";
//    public static final String BASE_URL = "http://119.29.52.160/LORServer/";
    public static final String BASE_URL = "http://192.168.1.101:8080/Lor/";
    private static final int DEFAULT_TIMEOUT = 5;

    private  static Retrofit retrofit;
    public   static LORService lorService;
    private static OkHttpClient client;

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
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    public static OkHttpClient getSharedClient(){
        return client;
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
        System.out.println("访问了:"+BASE_URL);
        return SingletonHolder.INSTANCE;
    }




    /**
     * 用于获取商家列表的数据
     *
     * @param subscriber  由调用者传过来的观察者对象
     * @param operation   标识服务器操作
     * @param NowLocation 定位
     * @param start       起始值
     * @param num         检索数目
     */
    public void getBusinessData(Subscriber<List<Seller>> subscriber, String operation, String NowLocation, String start, String num) {

        Observable observable = lorService.getSellerListDate(operation, NowLocation, start, num)
                .map(new SellerHttpResultFunc<List<Seller>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用于订单创建的结果
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @Query("operation") String operation,
     * @Query("sellerid") String sellerid,
     * @Query("userid") String userid,
     * @Query("tel") String tel,
     * @Query("name") String name,
     * @Query("time") String time,
     * @Query("describe") String describe,
     * @Query("type") String type,
     * @Query("address") String address
     */
    public void getTradeResult(Subscriber<String> subscriber, String operation, String sellerid
            , String userid, String tel, String name, String time
            , String describe, String type, String address) {

        Observable observable = lorService.getTradeResult(operation,sellerid
                , userid,  tel,  name,  time
                ,  describe,  type, address)
                .map(new TradeHttpResultFunc());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用于维修商品的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param operation  服务器的操作
     * @param sellerid   商家id
     */
    public void getGoodsResult(Subscriber<List<Goods>> subscriber, String operation, String sellerid) {

        Observable observable = lorService.getGoodsResult(operation, sellerid)
                .map(new GoodsHttpResultFunc<List<Goods>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用于订单的数据
     *
     * @param subscriber 由调用者传过来的观察者对象
     * @param operation  服务器的操作
     * @param orderID   订单编号
     */
    public void getOrderResult(Subscriber<List<Order>> subscriber, String operation, String orderID) {

        Observable observable = lorService.getOrderResult(operation, orderID)
                .map(new OrderHttpResultFunc<List<Order>>());

        toSubscribe(observable, subscriber);
    }




    public static <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }



    private class SellerHttpResultFunc<T> implements Func1<SellerHttpResult<T>, T> {

        @Override
        public T call(SellerHttpResult<T> httpResult) {

            System.out.println(httpResult.toString());
            if (httpResult.getCode().equals("2")) {
                throw new ApiException(100);
            }
            return httpResult.getSeller();
        }
    }


    private class TradeHttpResultFunc<T> implements Func1<TradeHttpResult<T>, T> {

        @Override
        public T call(TradeHttpResult<T> httpResult) {

            System.out.println("TradeHttpResult"+httpResult.toString());
            if (httpResult.getCode().equals("2")) {
                throw new ApiException(100);
            }
            return httpResult.getOrderID();
        }
    }
    private class GoodsHttpResultFunc<T> implements Func1<GoodsHttpResult<T>, T> {


        @Override
        public T call(GoodsHttpResult<T> httpResult) {
            System.out.println("GoodsHttpResult"+httpResult.getgoods());
            if (httpResult.getCode().equals("2")) {
                throw new ApiException(100);
            }
            return httpResult.getgoods();
        }
    }
    private class OrderHttpResultFunc<T> implements Func1<OrderHttpResult<T>, T> {

        @Override
        public T call(OrderHttpResult<T> httpResult) {

            System.out.println(httpResult.toString());
            if (httpResult.getCode().equals("2")) {
                throw new ApiException(100);
            }
            System.out.println( httpResult.getOrder());
            return httpResult.getOrder();
        }
    }
}
