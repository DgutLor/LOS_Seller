package com.wsns.lor.Activity.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wsns.lor.R;
import com.wsns.lor.entity.Order;
import com.wsns.lor.view.layout.UnderLineLinearLayout;


public class ProgressFragment extends Fragment {
    private UnderLineLinearLayout mUnderLineLinearLayout;
    public static View view;


    String createTime;
    String confirmTime;
    String finishTime;
    String startTime;
    String canceltime;
    String cancelConfirmTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_progress, null);
        initView();

        return view;
    }


    public void initView() {

        mUnderLineLinearLayout = (UnderLineLinearLayout) view.findViewById(R.id.underline_layout);
        addItem();

    }


    public void addItem() {
        mUnderLineLinearLayout.removeAllViews();
        if (createTime != null)
            addFistItem();
        if (confirmTime != null)
            addConfirmItem();
        if (startTime != null)
            addStartItem();
        if (finishTime != null)
            addFinishItem();
        if (canceltime != null)
            AddCancelItem();
        if (cancelConfirmTime != null)
            AddCancelConfirmItem();


    }

    private void addFistItem() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText("订单请求已发送");
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(createTime.substring(5));
        ((TextView) v.findViewById(R.id.tx_action_status)).setText("等待商家确认");

        mUnderLineLinearLayout.addView(v);


    }

    ;

    private void addConfirmItem() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText("商家已接单");
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(confirmTime.substring(5));
        ((TextView) v.findViewById(R.id.tx_action_status)).setText("等待对方安排上门人员");

        mUnderLineLinearLayout.addView(v);
    }

    ;

    private void addStartItem() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText("正在上门");
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(startTime.substring(5));
        ((TextView) v.findViewById(R.id.tx_action_status)).setText("请留意上门人员电话");

        mUnderLineLinearLayout.addView(v);
    }

    ;

    private void addFinishItem() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText("订单完成");
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(finishTime.substring(5));
        ((TextView) v.findViewById(R.id.tx_action_status)).setText("请根据服务给予评价");

        mUnderLineLinearLayout.addView(v);
    }

    ;


    private void AddCancelItem() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) v.findViewById(R.id.tx_action)).setText("发送取消请求");
        ((TextView) v.findViewById(R.id.tx_action_time)).setText(canceltime.substring(5));
        ((TextView) v.findViewById(R.id.tx_action_status)).setText("请联系商家");

        mUnderLineLinearLayout.addView(v);
    }

    private void AddCancelConfirmItem() {
        View vv = LayoutInflater.from(getContext()).inflate(R.layout.fragment_progress_item, mUnderLineLinearLayout, false);
        ((TextView) vv.findViewById(R.id.tx_action)).setText("订单被取消");
        ((TextView) vv.findViewById(R.id.tx_action_time)).setText(cancelConfirmTime.substring(5));
        ((TextView) vv.findViewById(R.id.tx_action_status)).setText("");

        mUnderLineLinearLayout.addView(vv);
    }


    private void subItem() {
        if (mUnderLineLinearLayout.getChildCount() > 0) {
            mUnderLineLinearLayout.removeViews(mUnderLineLinearLayout.getChildCount() - 1, 1);

        }
    }


    public void setOrder(Order order) {

        createTime = order.getCreatetime();
        confirmTime = order.getConfirmtime();
        finishTime = order.getFinishtime();
        startTime = order.getStarttime();
        canceltime = order.getCanceltime();
        cancelConfirmTime = order.getCancelConfirmTime();
    }
}
