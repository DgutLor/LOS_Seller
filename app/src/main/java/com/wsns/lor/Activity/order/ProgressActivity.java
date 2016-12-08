package com.wsns.lor.Activity.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.entity.Order;
import com.wsns.lor.http.HttpMethods;
import com.wsns.lor.http.subscribers.ProgressSubscriber;
import com.wsns.lor.http.subscribers.SubscriberOnNextListener;


import java.util.ArrayList;
import java.util.List;


public class ProgressActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private final List fragmentList = new ArrayList<>();
    private RelativeLayout ly_page1;
    private TextView tv_page1;
    private RelativeLayout ly_page2;
    private TextView tv_page2;
    private TextView tvstorename;
    private ViewPager vp_scroll;

    public Order order;
    private String orderID;

    SubscriberOnNextListener<List<Order>> getOrderResult;
    CommonFragementPagerAdapter commonFragementPagerAdapter;
    OrderDetailFrament orderDetailFrament;
    ProgressFragment progressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        orderID = getIntent().getStringExtra("orderID");
        getOrderResult = new SubscriberOnNextListener<List<Order>>() {
            @Override
            public void onNext(List<Order> orders) {
                System.out.println("onNext");
                if (orders.get(0) != null)
                    setDate(orders.get(0));
            }
        };

        LoadOrderDate();






        ly_page1 = (RelativeLayout) findViewById(R.id.ly_page1);
        tv_page1 = (TextView) findViewById(R.id.tv_page1);
        ly_page2 = (RelativeLayout) findViewById(R.id.ly_page2);
        tv_page2 = (TextView) findViewById(R.id.tv_page2);
        vp_scroll = (ViewPager) findViewById(R.id.vp_scroll);
        tvstorename = (TextView) findViewById(R.id.tv_title);


        ly_page1.setOnClickListener(this);
        ly_page2.setOnClickListener(this);

    }

    private void LoadOrderDate() {
        System.out.println("LoadOrderDate"+orderID);
        HttpMethods.getInstance().getOrderResult(new ProgressSubscriber(getOrderResult, this, false),
                "Get", orderID);
    }

    private void setDate(Order o) {
        order = new Order();
        order.setBuyername(o.getBuyername());
        order.setId(o.getId());
        order.setSeller(o.getSeller());
        order.setState(o.getState());
        order.setType(o.getType());
        order.setDescribe(o.getDescribe());
        order.setTime(o.getTime());
        order.setTel(o.getTel());
        order.setAddress(o.getAddress());
        order.setCreatetime(o.getCreatetime());
        order.setConfirmtime(o.getConfirmtime());
        order.setStarttime(o.getStarttime());
        order.setFinishtime(o.getFinishtime());
        order.setCanceltime(o.getCanceltime());
        order.setCancelConfirmTime(o.getCancelConfirmTime());


        InitFragementPagerView();

        tvstorename.setText(OnlineUserInfo.myInfo.getNickname());
    }

    public void InitFragementPagerView() {
        if (orderDetailFrament == null) {
            orderDetailFrament = new OrderDetailFrament();
        }
        if (progressFragment == null)
        {
            progressFragment = new ProgressFragment();
        }
        System.out.println("1");
        orderDetailFrament.setOrder(order);
        progressFragment.setOrder(order);
        if (fragmentList.size() == 0) {
            fragmentList.add(orderDetailFrament);
            fragmentList.add(progressFragment);
            System.out.println("2");
            commonFragementPagerAdapter = new CommonFragementPagerAdapter(getSupportFragmentManager());
            vp_scroll.setAdapter(commonFragementPagerAdapter);
            vp_scroll.addOnPageChangeListener(ProgressActivity.this);
        }
        System.out.println("3");
        System.out.println("4");
        commonFragementPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_page1:
                vp_scroll.setCurrentItem(0);
                break;
            case R.id.ly_page2:
                vp_scroll.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left_select);
            tv_page1.setTextColor(Color.parseColor("#ffffff"));
            ly_page2.setBackgroundResource(R.drawable.rectangle_right);
            tv_page2.setTextColor(Color.parseColor("#435356"));

        } else {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left);
            tv_page1.setTextColor(Color.parseColor("#435356"));
            ly_page2.setBackgroundResource(R.drawable.rectangle_right_select);
            tv_page2.setTextColor(Color.parseColor("#ffffff"));

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class CommonFragementPagerAdapter extends FragmentPagerAdapter {


        public CommonFragementPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getCount() > position ? (Fragment) fragmentList.get(position) : null;
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.POSITION_NONE;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
