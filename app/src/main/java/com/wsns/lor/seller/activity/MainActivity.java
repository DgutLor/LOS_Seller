package com.wsns.lor.seller.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsns.lor.seller.activity.seller.FixProfileActivity;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.fragment.ConversationListFragment;
import com.wsns.lor.seller.fragment.orders.OrderListFragment;
import com.wsns.lor.seller.fragment.seller.MyProfileFragment;

import static com.wsns.lor.seller.application.OnlineUserInfo.myInfo;

/**
 * 登录成功后的客户端主页 主要功能页面都在这Activity之上
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String CREATE_GROUP_CUSTOM_KEY = "create_group_custom_key";
    public static final String SET_DOWNLOAD_PROGRESS = "set_download_progress";
    public static final String IS_DOWNLOAD_PROGRESS_EXISTS = "is_download_progress_exists";
    public static final String CUSTOM_MESSAGE_STRING = "custom_message_string";
    public static final String CUSTOM_FROM_NAME = "custom_from_name";
    public static final String DOWNLOAD_INFO = "download_info";
    public static final String DOWNLOAD_ORIGIN_IMAGE = "download_origin_image";
    public static final String DOWNLOAD_THUMBNAIL_IMAGE = "download_thumbnail_image";
    public static final String IS_UPLOAD = "is_upload";
    public static final String LOGOUT_REASON = "logout_reason";

    private LinearLayout btnSeller, btnNotice, btnOrder;
    private ImageView ivSeller, ivNotice, ivOrder;
    private TextView tvSeller, tvNotice, tvOrder;
    private ConversationListFragment messageFragment;
    private OrderListFragment orderFragment;
    private MyProfileFragment moreFragment;

    private Fragment flag_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (myInfo.getNickname().equals("")) {
            Intent intent = new Intent();
            intent.setClass(this, FixProfileActivity.class);
            startActivity(intent);
            finish();
        }

        initView();
        if (savedInstanceState == null) {
            // 设置默认的Fragment
            setDefaultFragment();
        }
    }

    private void setDefaultFragment() {

        moreFragment = new MyProfileFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //add（）方法：在当前显示时，点击Back键不出现白板。是正确的相应Back键，即退出我们的Activity
        transaction.add(R.id.fragment_content, moreFragment);
        transaction.commit();
        flag_fragment = moreFragment;
    }

    public void initView() {
        btnSeller = (LinearLayout) findViewById(R.id.btn_seller);
        btnNotice = (LinearLayout) findViewById(R.id.btn_notice);
        btnOrder = (LinearLayout) findViewById(R.id.btn_order);
        ivSeller = (ImageView) findViewById(R.id.iv_seller);
        ivNotice = (ImageView) findViewById(R.id.iv_notice);
        ivOrder = (ImageView) findViewById(R.id.iv_order);
        tvSeller = (TextView) findViewById(R.id.tv_seller);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        tvOrder = (TextView) findViewById(R.id.tv_order);


        btnSeller.setOnClickListener(this);
        btnNotice.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
    }

    /**
     * 初始化控件的颜色
     */
    private void initBottemBtn() {
        ivSeller.setImageResource(R.drawable.main_bottom_seller);
        ivNotice.setImageResource(R.drawable.main_bottom_notice);
        ivOrder.setImageResource(R.drawable.main_bottom_order);
        tvSeller.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_normal));
        tvNotice.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_normal));
        tvOrder.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_normal));
    }

    @Override
    public void onClick(View v) {
        int mBtnid = v.getId();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(flag_fragment);
        initBottemBtn();
        switch (mBtnid) {
            case R.id.btn_seller:
                if (moreFragment == null) {
                    moreFragment = new MyProfileFragment();
                    transaction.add(R.id.fragment_content, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                ivSeller.setImageResource(R.drawable.main_index_seller_pressed);
                tvSeller.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_pressed));
                flag_fragment = moreFragment;
                break;
            case R.id.btn_notice:
                if (messageFragment == null) {
                    messageFragment = new ConversationListFragment();
                    transaction.add(R.id.fragment_content, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                ivNotice.setImageResource(R.drawable.main_index_notice_pressed);
                tvNotice.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_pressed));
                flag_fragment = messageFragment;
                break;
            case R.id.btn_order:
                if (orderFragment == null) {
                    orderFragment = new OrderListFragment();
                    transaction.add(R.id.fragment_content, orderFragment);
                } else {
                    transaction.show(orderFragment);
                }
                ivOrder.setImageResource(R.drawable.main_index_order_pressed);
                tvOrder.setTextColor(getResources().getColor(R.color.main_bottom_textcolor_pressed));
                flag_fragment = orderFragment;
                break;

        }
        transaction.commit();

    }


}
