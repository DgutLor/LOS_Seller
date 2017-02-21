package com.wsns.lor.seller.fragment.orders;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.Orders;
import com.wsns.lor.seller.http.HttpMethods;


public class OrdersDetailFrament extends Fragment {


    View view;
    TextView contactNameText;
    TextView titleText;
    ImageView avatarImg;
    TextView quantityText;
    TextView priceText;
    TextView sumText;
    TextView paywayText;
    TextView nameText, phoneText, addressText;
    TextView ordersIdText, ordersCreateTimeText;

    Orders orders;
    Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            activity = getActivity();
            view = inflater.inflate(R.layout.fragment_orders_detail, null);

        }
        return view;
    }


    private void initView() {
        nameText = (TextView) view.findViewById(R.id.tv_name);
        avatarImg = (ImageView) view.findViewById(R.id.img_avatar);
        quantityText = (TextView) view.findViewById(R.id.tv_quantity);
        priceText = (TextView) view.findViewById(R.id.tv_price);
        sumText = (TextView) view.findViewById(R.id.tv_sum_1);
        titleText = (TextView) view.findViewById(R.id.tv_title);
        paywayText = (TextView) view.findViewById(R.id.tv_orders_payway);
        ordersIdText = (TextView) view.findViewById(R.id.tv_orders_id);
        ordersCreateTimeText = (TextView) view.findViewById(R.id.tv_orders_createtime);

        contactNameText = (TextView) view.findViewById(R.id.tv_contact_name);
        phoneText = (TextView) view.findViewById(R.id.tv_contact_phone);
        addressText = (TextView) view.findViewById(R.id.tv_contact_address);

        nameText.setText(orders.getBuyer().getName());
        Picasso.with(activity).load(HttpMethods.BASE_URL + orders.getBuyer().getAvatar()).resize(30, 30).centerInside().error(R.drawable.unknow_avatar).into(avatarImg);
        quantityText.setText(orders.getWorkTime());
        priceText.setText(orders.getPrice()+"");
        sumText.setText(orders.getPrice()+ "元");
        titleText.setText(orders.getGoods());
        if (orders.isPayOnline())
            paywayText.setText("在线支付");
        else
            paywayText.setText("货到付款");

        contactNameText.setText(orders.getSeller().getName());
        phoneText.setText(orders.getPhone());
        addressText.setText(orders.getAddress());
        ordersIdText.setText(orders.getId()+"");
        ordersCreateTimeText.setText(orders.getCreateDate());
    }

    public void setOrder(Orders orders) {
        this.orders = orders;
        initView();
    }
}