package com.wsns.lor.seller.fragment.seller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wsns.lor.seller.adapter.TypeTagAdapter;
import com.wsns.lor.seller.view.layout.Listener.OnTagSelectListener;
import com.wsns.lor.seller.R;
import com.wsns.lor.seller.entity.RepairGoods;
import com.wsns.lor.seller.http.HttpMethods;
import com.wsns.lor.seller.http.subscribers.ProgressSubscriber;
import com.wsns.lor.seller.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.seller.utils.StringDivide;
import com.wsns.lor.seller.view.layout.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;


public class TradeEditFragment extends Fragment {

    @Bind(R.id.jmui_commit_btn) Button jmuiCommitBtn;
    private FlowTagLayout mBrandFlowTagLayout;
    private FlowTagLayout mTypeFlowTagLayout;
    private TypeTagAdapter<String> mBrandTagAdapter;
    private TypeTagAdapter<String> mTypeTagAdapter;
    private List<RepairGoods> allDataSource = new ArrayList<>();
    String selectbrand;
    String selecttype;
    private int change = 1;
    private Button delete;
    private Button add;
    private Button modify;
    private EditText edit;
    private List<String> dataSource = new ArrayList<>();
    List<String> typeDataSource = new ArrayList<>();
    int brandItem = 0;
    SubscriberOnNextListener<List<RepairGoods>> getGoodsResult;
    SubscriberOnNextListener<List<RepairGoods>> addRepairGoodsOnNext;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_edit, container, false);
        ButterKnife.bind(this, view);
        mBrandFlowTagLayout = (FlowTagLayout) view.findViewById(R.id.brand_flow_layout);
        mTypeFlowTagLayout = (FlowTagLayout) view.findViewById(R.id.type_flow_layout);
        delete = (Button) view.findViewById(R.id.bt_delete);
        add = (Button) view.findViewById(R.id.bt_add);
        edit = (EditText) view.findViewById(R.id.et_edit);
        modify = (Button) view.findViewById(R.id.bt_modify);
        delete.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        modify.setVisibility(View.GONE);
        jmuiCommitBtn.setVisibility(View.GONE);


        //尺寸
        mBrandTagAdapter = new TypeTagAdapter<>(getActivity());
        mBrandFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mBrandFlowTagLayout.setAdapter(mBrandTagAdapter);
        mBrandFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (final int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        selectbrand = parent.getAdapter().getItem(i).toString();
                        brandItem = i;
                        delete.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        modify.setVisibility(View.VISIBLE);
                        edit.setText(selectbrand);
                        add.setVisibility(View.GONE);
                        modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edit.getText().toString().trim().equals("")) {
                                    allDataSource.get(i).setBrand(edit.getText().toString());
                                    initBrandData();
                                }
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                allDataSource.remove(i);
                                initBrandData();
                                initTypeData(-1);
                            }
                        });
                        if (i == parent.getAdapter().getCount() - 1) {
                            delete.setVisibility(View.GONE);
                            add.setVisibility(View.VISIBLE);
                            modify.setVisibility(View.GONE);
                            edit.setText("");

                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (!edit.getText().toString().trim().equals("")) {
                                        dataSource.remove(i);
                                        allDataSource.add(new RepairGoods(edit.getText().toString(), ""));
                                        initBrandData();
                                        edit.setText("");
                                    }
                                }
                            });
                        }
                        initTypeData(i);
                    }

                } else {
                    edit.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);

                    initTypeData(-1);
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
                jmuiCommitBtn.setVisibility(View.VISIBLE);
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    for (final int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        selecttype = parent.getAdapter().getItem(i).toString();
                        delete.setVisibility(View.VISIBLE);
                        edit.setVisibility(View.VISIBLE);
                        edit.setText(selecttype);
                        add.setVisibility(View.GONE);
                        modify.setVisibility(View.VISIBLE);
                        edit.setText("");
                        edit.setText(selecttype);
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String type = "";
                                typeDataSource.remove(i);
                                for (int m = 0; m < typeDataSource.size() - 1; m++) {


                                    if (m != 0 && m != typeDataSource.size() - 1)
                                        type = type + "," + typeDataSource.get(m);
                                    else
                                        type = typeDataSource.get(m);
                                }

                                allDataSource.get(brandItem).setType(type);
                                initTypeData(brandItem);
                            }
                        });
                        modify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edit.getText().toString().trim().equals("")) {
                                    String type = "";

                                    typeDataSource.set(i, edit.getText().toString());
                                    for (int m = 0; m < typeDataSource.size() - 1; m++) {

                                        if (m != 0 && m != typeDataSource.size() - 1)
                                            type = type + "," + typeDataSource.get(m);
                                        else
                                            type = typeDataSource.get(m);
                                    }
                                    allDataSource.get(brandItem).setType(type);
                                    initTypeData(brandItem);
                                }
                            }
                        });
                        if (i == parent.getAdapter().getCount() - 1) {
                            modify.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);
                            add.setVisibility(View.VISIBLE);
                            edit.setText("");
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!edit.getText().toString().trim().equals("")) {

                                        if (!allDataSource.get(brandItem).getType().equals(""))
                                            allDataSource.get(brandItem).setType(allDataSource.get(brandItem).getType() + "," + edit.getText().toString());
                                        else
                                            allDataSource.get(brandItem).setType(edit.getText().toString());
                                        initTypeData(brandItem);
                                        edit.setText("");
                                    }
                                }
                            });
                        }


                    }

                } else {

                    delete.setVisibility(View.GONE);
                    edit.setText(selectbrand);
                }
            }
        });
        getGoodsResult = new SubscriberOnNextListener<List<RepairGoods>>() {
            @Override
            public void onNext(List<RepairGoods> list) {
                getGoodsList(list);
            }
        };

        addRepairGoodsOnNext = new SubscriberOnNextListener<List<RepairGoods>>() {
            @Override
            public void onNext(List<RepairGoods> list) {
                getGoodsList(list);
            }
        };

        LoadGoodsData();


        ButterKnife.bind(this, view);
        return view;
    }

    private void UploadOrder() {
        String goods = "";
        for (int i = 0; i < allDataSource.size(); i++) {
            if (i == 0)
                goods = allDataSource.get(i).getBrand() + "@" + allDataSource.get(i).getType();
            else
                goods = goods + "#" + allDataSource.get(i).getBrand() + "@" + allDataSource.get(i).getType();


        }
        ;
    }

    private void initTypeData(int i) {
        typeDataSource.clear();
        StringDivide dd = null;
        if (i != -1 && i < allDataSource.size()) {
            if (!allDataSource.get(i).getType().equals("")) {
                dd = new StringDivide(allDataSource.get(i).getType());

                for (int j = 0; j < dd.getCount(); j++) {
                    typeDataSource.add(dd.getItem(j));
                }
            }

            typeDataSource.add("新增");
        }
        mTypeTagAdapter.clearAndAddAll(typeDataSource);
    }


    /**
     * 初始化数据
     */
    private void initBrandData() {
        dataSource.clear();
        for (int i = 0; i < allDataSource.size(); i++) {
            if (!allDataSource.get(i).getBrand().trim().equals(""))
                dataSource.add(allDataSource.get(i).getBrand());
        }
        dataSource.add("新增");
        mBrandTagAdapter.clearAndAddAll(dataSource);
    }


    public void LoadGoodsData() {
        HttpMethods.getInstance().getGoodsResult(new ProgressSubscriber(getGoodsResult, getActivity(), false),
                JMessageClient.getMyInfo().getUserName());

    }

    private void getGoodsList(List<RepairGoods> goodsList) {
        allDataSource.clear();
        allDataSource.addAll(goodsList);
        initBrandData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.jmui_commit_btn)
    public void onClick() {
        HttpMethods.getInstance().addRepairGoods(new ProgressSubscriber(addRepairGoodsOnNext, getActivity(), true),
                allDataSource);
    }
}
