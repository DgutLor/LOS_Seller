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

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liukun on 16/3/9.
 */
public interface LORService {

    @FormUrlEncoded
    @POST("api/seller/login")
    Observable<ServerDate<User>> getUserDate(@Field("account") String username, @Field("passwordHash") String password);

    @FormUrlEncoded
    @POST("api/seller/register")
    Observable<ServerDate<User>> getRegisterResult(@Field("account") String account, @Field("passwordHash") String passwordHash);

    @POST("api/seller/me")
    Observable<ServerDate<Poi>> getCurrentUser();

    @GET("api/repairGoods/bySellerID")
    Observable<ServerDate<List<RepairGoods>>> getGoodsResult(@Query("seller_account") String seller_account);

    @FormUrlEncoded
    @POST("api/orders/progress/byOrdersId")
    Observable<ServerDate<Page<OrdersProgress>>> getOrdersProgressPage(@Field("page") int page, @Field("orders_id") int orders_id);

    @FormUrlEncoded
    @POST("api/orders/progress/add")
    Observable<ServerDate<OrdersProgress>> addOrdersProgress(@Field("content") String content,
                                                             @Field("title") String title, @Field("orders_id") int orders_id,
                                                             @Field("state") int state);

    @FormUrlEncoded
    @POST("api/orders/my")
    Observable<ServerDate<Page<Orders>>> getMyOrderPage(@Field("page") int page);

    @FormUrlEncoded
    @POST("api/rec/records")
    Observable<ServerDate<Page<Records>>> getMyRecordsPage(@Field("page") int page);

    @Multipart
    @POST("api/seller/update/avatar")
    Observable<ServerDate<User>> updateAvatar(@Part("avatar\"; filename=\"avatar.png") RequestBody  avatar);

    @FormUrlEncoded
    @POST("api/seller/update/password")
    Observable<ServerDate<User>> updatePassword(@Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("api/seller/update/userName")
    Observable<ServerDate<User>> updateName(@Field("userName") String userName);

    @FormUrlEncoded
    @POST("api/seller/forget/password")
    Observable<ServerDate<User>> forgetPassword(@Field("account") String account, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("api/seller/finduser")
    Observable<ServerDate<User>> findUser(@Field("account") String account);

    @POST("api/seller/create/poi")
    Observable<ServerDate<PoiCreate>> createSeller(@Body Poi poi);

    @FormUrlEncoded
    @POST("api/repairGoods/add")
    Observable<ServerDate<List<RepairGoods>>> addRepairGoods(@Body List<RepairGoods> goodsList);

    @POST("api/seller/update/poi")
    Observable<ServerDate<Poi>> updatePoi(@Body Poi poi);


}
