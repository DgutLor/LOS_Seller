package com.wsns.lor.http;



import com.wsns.lor.entity.Goods;
import com.wsns.lor.entity.GoodsHttpResult;
import com.wsns.lor.entity.Order;
import com.wsns.lor.entity.OrderHttpResult;
import com.wsns.lor.entity.RegisterResult;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.entity.SellerHttpResult;
import com.wsns.lor.entity.TradeHttpResult;
import com.wsns.lor.entity.UserHttpResult;
import com.wsns.lor.entity.User;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface LORService {

    @POST("LoginServlet")
    Observable<UserHttpResult<List<User>>> getUserDate(@Query("hxid") String username, @Query("password") String password);

    @POST("SellerDateServlet")
    Observable<SellerHttpResult<List<Seller>>>
    getSellerListDate(@Query("operation") String operation, @Query("NowLocation") String NowLocation,
                      @Query("start") String start, @Query("num") String num);

    @POST("RegisterServlet")
    Observable<RegisterResult<String>> getRegisterResult(@Query("username") String username, @Query("password") String password);

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
