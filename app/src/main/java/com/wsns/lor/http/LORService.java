package com.wsns.lor.http;



import com.wsns.lor.entity.Goods;
import com.wsns.lor.entity.GoodsHttpResult;
import com.wsns.lor.entity.Order;
import com.wsns.lor.entity.OrderHttpResult;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.entity.SellerHttpResult;
import com.wsns.lor.entity.TradeHttpResult;
import com.wsns.lor.entity.DataAndCodeBean;
import com.wsns.lor.entity.User;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface LORService {

    @POST("api/user/login")
    Observable<DataAndCodeBean<User>> getUserDate(@Query("account") String username, @Query("passwordHash") String password);

    @POST("SellerDateServlet")
    Observable<SellerHttpResult<List<Seller>>>
    getSellerListDate(@Query("operation") String operation, @Query("NowLocation") String NowLocation,
                      @Query("start") String start, @Query("num") String num);

    @POST("api/user/register")
    Observable<DataAndCodeBean<User>> getRegisterResult(@Query("account") String account, @Query("passwordHash") String passwordHash);

    @POST("OrderDateServlet")
    Observable<TradeHttpResult<String>> getTradeResult(
            @Query("operation") String operation,
            @Query("sellerid") String sellerid,
            @Query("userid") String userid,
            @Query("tel") String tel,
            @Query("name") String name,
            @Query("time") String time,
            @Query("describe") String describe,
            @Query("type") String type,
            @Query("address") String address
    );

    @POST("GoodsDateServlet")
    Observable<GoodsHttpResult<List<Goods>>> getGoodsResult(@Query("operation") String operation,@Query("hxid") String sellerid);


    @POST("OrderDateServlet")
    Observable<OrderHttpResult<List<Order>>> getOrderResult(@Query("operation") String operation, @Query("id") String orderID);


}
