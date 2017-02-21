package com.wsns.lor.seller.fragment.orders;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.Orders;
import com.wsns.lor.seller.entity.OrdersProgress;
import com.wsns.lor.seller.entity.Page;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.view.layout.UnderLineLinearLayout;
import com.wsns.lor.seller.view.layout.VRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class OrdersProgressFragment extends Fragment implements View.OnClickListener {
    private SubscriberOnNextListener getOrdersProgressOnNext;
    private SubscriberOnNextListener addOrdersProgressOnNext;
    private UnderLineLinearLayout mUnderLineLinearLayout;
    public View view;
    Orders orders;
    Activity activity;
    List<OrdersProgress> mProgress = new ArrayList<>();
    Page<OrdersProgress> progressPage;
    LinearLayout ll_handle;
    Button leftBtn, rightBtn;
    VRefreshLayout mRefreshLayout;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            activity = getActivity();
            view = inflater.inflate(R.layout.fragment_orders_progress, null);
            initHeaderView();
        }
        return view;
    }

    public void initView() {
        ll_handle = (LinearLayout) view.findViewById(R.id.ll_handle);
        mUnderLineLinearLayout = (UnderLineLinearLayout) view.findViewById(R.id.underline_layout);
        mUnderLineLinearLayout.removeAllViews();
        leftBtn = (Button) view.findViewById(R.id.btn_left);
        rightBtn = (Button) view.findViewById(R.id.btn_right);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);

        leftBtn.setText("私信");
        switch (orders.getState()) {
            case 1:
                rightBtn.setText("接单");
                break;
            case 2:
                rightBtn.setText("发货");
                break;
            case 3:
                rightBtn.setText("待确认");
                break;
            case 4:
                rightBtn.setText("待评价");
                break;
            case 5:
                rightBtn.setText("查看评价");
                break;
            case 6:
                rightBtn.setText("同意退款");
                leftBtn.setText("拒绝退款");
                break;
            case 7:
                rightBtn.setText("已退款");
                break;
            case 8:
                rightBtn.setText("已拒绝");
                break;
        }

        addOrdersProgressOnNext = new SubscriberOnNextListener<OrdersProgress>() {

            @Override
            public void onNext(OrdersProgress ordersProgress) {
                addProgressItem(ordersProgress);
            }
        };
        getOrdersProgressOnNext = new SubscriberOnNextListener<Page<OrdersProgress>>() {
            @Override
            public void onNext(Page<OrdersProgress> progressPage) {
                mRefreshLayout.refreshComplete();
                final List<OrdersProgress> datas = progressPage.getContent();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgress.clear();
                        mProgress.addAll(datas);
                        mUnderLineLinearLayout.removeAllViews();
                        for (int i = 0; i < mProgress.size(); i++) {
                            addProgressItem(i);
                        }
                    }
                });
            }
        };
        loadOrdersProgress();
    }

    private void initHeaderView() {
        mRefreshLayout = (VRefreshLayout) view.findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {

            mRefreshLayout.setBackgroundColor(Color.DKGRAY);
            mRefreshLayout.setAutoRefreshDuration(400);
            mRefreshLayout.setRatioOfHeaderHeightToReach(1.5f);
            mRefreshLayout.addOnRefreshListener(new VRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadOrdersProgress();
                }
            });
        }

        mRefreshLayout.setHeaderView(mRefreshLayout.getDefaultHeaderView());
        mRefreshLayout.setBackgroundColor(Color.WHITE);

    }

    private void loadOrdersProgress() {
        HttpMethods.getInstance().getOrdersProgressPage(new ProgressSubscriber(getOrdersProgressOnNext, activity, false), 0, orders.getId());
    }

    private void addProgressItem(int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.fragment_orders_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText(mProgress.get(i).getContent());
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(mProgress.get(i).getCreateDate());
        ((TextView) v.findViewById(R.id.tx_action_status)).setText(mProgress.get(i).getTitle());
        mUnderLineLinearLayout.addView(v);
    }

    private void addProgressItem(OrdersProgress progress) {
        View v = LayoutInflater.from(activity).inflate(R.layout.fragment_orders_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText(progress.getContent());
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(progress.getCreateDate());
        ((TextView) v.findViewById(R.id.tx_action_status)).setText(progress.getTitle());
        mUnderLineLinearLayout.addView(v);

        if (rightBtn.getText().toString().equals("接单")) {
            rightBtn.setText("发货");
            rightBtn.setEnabled(true);
        } else if (rightBtn.getText().toString().equals("发货")) {
            rightBtn.setEnabled(true);
            rightBtn.setText("待确认");
        } else if (rightBtn.getText().toString().equals("同意退款")) {
            leftBtn.setText("私信");
            rightBtn.setEnabled(true);
            rightBtn.setText("已退款");
        } else if (leftBtn.getText().toString().equals("拒绝退款")) {
            leftBtn.setText("私信");
            leftBtn.setEnabled(true);
            rightBtn.setText("已拒绝");
        }
    }

    public void setOrder(Orders orders) {
        this.orders = orders;
        initView();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_left:
             if (leftBtn.getText().toString().equals("私信")) {
//                    Intent intent = new Intent(activity, SendMessageActivity.class);
//                    if ( orders.getGoods().getPublishers().getId()== CurrentUserInfo.user_id) //判断订单是不是当前用户购买的
//                    {
//                        intent.putExtra("account", orders.getBuyer().getAccount());
//
//                    } else {
//                        intent.putExtra("account", orders.getGoods().getPublishers().getAccount());
//                    }
//                    startActivity(intent);
                } else if (leftBtn.getText().toString().equals("拒绝退款")) {
                    leftBtn.setEnabled(false);
                    changeState("拒绝退款", "如有疑问请联系客服", 8);
                }
                break;
            case R.id.btn_right:
                if (rightBtn.getText().toString().equals("接单")) {
                    changeState("已接单", "请卖方尽快发货", 2);
                    rightBtn.setEnabled(false);
                } else if (rightBtn.getText().toString().equals("发货")) {
                    rightBtn.setEnabled(false);
                    changeState("已发货", "请保证联系方式有效", 3);
                }
                else if (rightBtn.getText().toString().equals("查看评价")) {

//                    Intent itnt = new Intent(activity,OrdersCommentListActivity.class);
//                    itnt.putExtra("goodsId",orders.getGoods().getId());
//                    activity.startActivity(itnt);
                }
                else if (rightBtn.getText().toString().equals("同意退款")) {
                    rightBtn.setEnabled(false);
                    changeState("同意退款", "金币已退回买方", 7);
                }

                break;
        }


    }


    private void changeState(String content, String title, final int state) {


        HttpMethods.getInstance().addOrdersProgress(
                new ProgressSubscriber(addOrdersProgressOnNext, activity, false),
                content, title, orders.getId(), state);
    }
}