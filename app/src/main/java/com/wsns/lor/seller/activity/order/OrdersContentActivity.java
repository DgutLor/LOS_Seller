package com.wsns.lor.seller.activity.order;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.Orders;
import com.wsns.lor.seller.fragment.orders.OrdersDetailFrament;
import com.wsns.lor.seller.fragment.orders.OrdersProgressFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 订单界面（点击订单列表进入）
 */
public class OrdersContentActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private final List fragmentList = new ArrayList<>();
    private RelativeLayout ly_page1;
    private TextView tv_page1;
    private RelativeLayout ly_page2;
    private TextView tv_page2;
    private ViewPager vp_scroll;
    public Orders orders;

    CommonFragementPagerAdapter commonFragementPagerAdapter;
    OrdersDetailFrament ordersDetailFrament = new OrdersDetailFrament();
    OrdersProgressFragment ordersProgressFragment = new OrdersProgressFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orders = (Orders) getIntent().getSerializableExtra("orders");
        setContentView(R.layout.activity_orders_content);

        ly_page1 = (RelativeLayout) findViewById(R.id.ly_page1);
        tv_page1 = (TextView) findViewById(R.id.tv_page1);
        ly_page2 = (RelativeLayout) findViewById(R.id.ly_page2);
        tv_page2 = (TextView) findViewById(R.id.tv_page2);
        vp_scroll = (ViewPager) findViewById(R.id.vp_scroll);

        ly_page1.setOnClickListener(this);
        ly_page2.setOnClickListener(this);
        fragmentList.add(ordersProgressFragment);
        fragmentList.add(ordersDetailFrament);
        commonFragementPagerAdapter = new CommonFragementPagerAdapter(getFragmentManager());
        vp_scroll.setAdapter(commonFragementPagerAdapter);
        vp_scroll.addOnPageChangeListener(OrdersContentActivity.this);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left_select);
            tv_page1.setTextColor(Color.parseColor("#ffffff"));
            ly_page2.setBackgroundResource(R.drawable.rectangle_right);
            tv_page2.setTextColor(Color.parseColor("#3c9aff"));

        } else {
            ly_page1.setBackgroundResource(R.drawable.rectangle_left);
            tv_page1.setTextColor(Color.parseColor("#3c9aff"));
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

    private Handler scaleHandler = new Handler();
    private Runnable scaleRunnable = new Runnable() {

        @Override
        public void run() {
            ordersDetailFrament.setOrder(orders);
            ordersProgressFragment.setOrder(orders);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {
                scaleHandler.post(scaleRunnable);
            }
        });
    }

}
