package com.wsns.lor.Activity.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wsns.lor.R;
import com.wsns.lor.entity.Order;

public class OrderDetailFrament extends Fragment {
    View view;
    TextView orderID;
    TextView createtime;
    TextView worktime;
    TextView type;
    TextView describe;
    TextView name;
    TextView address;
    TextView tel;

Order order;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_order_detail, null);
        initView();
        return view;
    }


    private void initView() {

        orderID = (TextView) view.findViewById(R.id.tv_orderID);
        createtime = (TextView) view.findViewById(R.id.tv_createtime);
        worktime = (TextView) view.findViewById(R.id.tv_worktime);
        type = (TextView) view.findViewById(R.id.tv_type);
        describe = (TextView) view.findViewById(R.id.tv_describe);
        name = (TextView) view.findViewById(R.id.tv_name);
        address = (TextView) view.findViewById(R.id.tv_address);
        tel = (TextView) view.findViewById(R.id.tv_tel);
        orderID.setText(order.getId());
        createtime.setText(order.getCreatetime());
        worktime.setText(order.getTime());
        type.setText(order.getType());
        describe.setText(order.getDescribe());
        name.setText(order.getBuyername());
        address.setText(order.getAddress());
        tel.setText(order.getTel());

    }

    public void setOrder(Order order) {
        this.order = order;
        
    }
}
