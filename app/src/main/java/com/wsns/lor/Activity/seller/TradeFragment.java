package com.wsns.lor.Activity.seller;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.view.FlowTagLayout;
import com.view.OnTagSelectListener;

import com.wsns.lor.Activity.order.ProgressActivity;
import com.wsns.lor.Adapter.TypeTagAdapter;
import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.entity.Goods;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.HttpMethods;
import com.wsns.lor.http.subscribers.ProgressSubscriber;
import com.wsns.lor.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.utils.StringDivide;


import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.UserInfo;
import im.sdk.debug.activity.setting.RegisterActivity;


public class TradeFragment extends Fragment {
    private TextView tvstorename;
    private EditText etdescribe;
    private EditText etworktime;
    private EditText etname;
    private EditText etaddress;
    private EditText ettel;
    private FlowTagLayout mBrandFlowTagLayout;
    private FlowTagLayout mTypeFlowTagLayout;
    private FlowTagLayout mChoiceFlowTagLayout;
    private TypeTagAdapter<String> mBrandTagAdapter;
    private TypeTagAdapter<String> mTypeTagAdapter;
    private TypeTagAdapter<String> mChoiceTagAdapter;
    private List<Goods> allDataSource = new ArrayList<>();
    String selectbrand;
    String selecttype;
    String orderID;
    private int change = 1;
    private Button submit;
    private List<String> dataSource = new ArrayList<>();
    View view;
    SubscriberOnNextListener<String> getTradeResult;
    SubscriberOnNextListener<List<Goods>> getGoodsResult;
    Seller seller;

    public TradeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trade, container, false);
        mBrandFlowTagLayout = (FlowTagLayout) view.findViewById(R.id.brand_flow_layout);
        mTypeFlowTagLayout = (FlowTagLayout) view.findViewById(R.id.type_flow_layout);
        mChoiceFlowTagLayout = (FlowTagLayout) view.findViewById(R.id.choice_flow_layout);
        tvstorename = (TextView) view.findViewById(R.id.tv_storename);
        etdescribe = (EditText) view.findViewById(R.id.et_describe);
        etworktime = (EditText) view.findViewById(R.id.et_worktime);
        etname = (EditText) view.findViewById(R.id.et_name);
        etaddress = (EditText) view.findViewById(R.id.et_address);
        ettel = (EditText) view.findViewById(R.id.et_tel);
        submit = (Button) view.findViewById(R.id.bt_submit);
        tvstorename.setText(seller.getNick());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit.getText().equals("去看看")) {
                    Intent intent = new Intent(getActivity(), ProgressActivity.class);

                    intent.putExtra("orderID", orderID);
                    startActivity(intent);
                } else
                    UploadOrder();
            }
        });

        //尺寸
        mBrandTagAdapter = new TypeTagAdapter<>(getActivity());
        mBrandFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mBrandFlowTagLayout.setAdapter(mBrandTagAdapter);
        mBrandFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {

                        sb.append(parent.getAdapter().getItem(i));
                        selectbrand = parent.getAdapter().getItem(i).toString();
                        initChoiceData(selectbrand);
                        initTypeData(i);
                    }

                } else {
                    initChoiceData("");
                }
            }
        });

        //移动研发标签
        mTypeTagAdapter = new TypeTagAdapter<>(getActivity());
        mTypeFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mTypeFlowTagLayout.setAdapter(mTypeTagAdapter);
        mTypeFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                submit.setText("提交");
                submit.setClickable(true);
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        selecttype = parent.getAdapter().getItem(i).toString();
                        initChoiceData(selectbrand + selecttype);
                    }

                } else {
                    initChoiceData("");
                }
            }
        });
        mChoiceTagAdapter = new TypeTagAdapter<>(getActivity());
        mChoiceFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        mChoiceFlowTagLayout.setAdapter(mChoiceTagAdapter);

        getTradeResult = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                orderID=s;
                Intent intent = new Intent(getActivity(), ProgressActivity.class);
                intent.putExtra("orderID", orderID);
                startActivity(intent);
                submit.setText("去看看");

                //发送推送
            }
        };
        getGoodsResult = new SubscriberOnNextListener<List<Goods>>() {
            @Override
            public void onNext(List<Goods> list) {
                getGoodsList(list);

            }
        };

        initChoiceData("");

        LoadGoodsData();
        return view;
    }


    private void UploadOrder() {
        String type;
        if (selectbrand != null && selecttype != null)
            type = selectbrand + selecttype;
        else
            type = "其他";

        HttpMethods.getInstance().getTradeResult(new ProgressSubscriber(getTradeResult, getActivity(), true),
                "Upload", seller.getID(),
                OnlineUserInfo.myInfo.getUserName(), ettel.getText().toString(),
                etname.getText().toString(),
                etworktime.getText().toString(),
                etdescribe.getText().toString(), type, etaddress.getText().toString());

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void initChoiceData(String choice) {
        List<String> dataSource = new ArrayList<>();
        if (!choice.equals("")) {
            dataSource.add(choice.toString());

            mChoiceTagAdapter.clearAndAddAll(dataSource);
        }

    }

    private void initTypeData(int i) {
        List<String> dataSource = new ArrayList<>();
        if (i < allDataSource.size()) {
            StringDivide dd = new StringDivide(allDataSource.get(i).getMatName());
            for (int j = 0; j < dd.getCount(); j++) {
                dataSource.add(dd.getItem(j));
            }
            dataSource.add("其他");
        }
        mTypeTagAdapter.clearAndAddAll(dataSource);
    }


    /**
     * 初始化数据
     */
    private void initBrandData() {
        dataSource.clear();
        for (int i = 0; i < allDataSource.size(); i++) {

            dataSource.add(allDataSource.get(i).getManufacturers());
        }
        for (int i = 0; i < allDataSource.size(); i++) {
            System.out.println(dataSource.get(i) + "~" + allDataSource.get(i).getManufacturers());
        }
        dataSource.add("其他");
        mBrandTagAdapter.clearAndAddAll(dataSource);
    }


    public void LoadGoodsData() {
        HttpMethods.getInstance().getGoodsResult(new ProgressSubscriber(getGoodsResult, getActivity(), false),
                "get", seller.getID());
    }

    private void getGoodsList(List<Goods> list) {
        allDataSource.clear();
        for (int i = 0; i < list.size(); i++) {
            allDataSource.add(list.get(i));
        }
        initBrandData();
    }


    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
