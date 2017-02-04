package com.wsns.lor.Activity.seller;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsns.lor.Adapter.MyItemRecyclerViewAdapter;
import com.wsns.lor.Adapter.SellersListAdapter;
import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.entity.Goods;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.HttpMethods;
import com.wsns.lor.http.subscribers.ProgressSubscriber;
import com.wsns.lor.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.utils.Densityutils;
import com.wsns.lor.utils.ToastUtil;
import com.wsns.lor.view.layout.VRefreshLayout;
import com.wsns.lor.view.widgets.JDHeaderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 商家列表页Fragment
 */
public class SellerFragment extends Fragment implements AMapLocationListener, CloudSearch.OnCloudSearchListener, MyItemClickListener {

    int NOT_MORE_PAGE = -1;

    public int START = 0;
    public int PAGESIZE = 10;//列表一次刷新数目

    public int page = 0;//查询第几页的结果，从0开始
    public int PageCount;//搜索结果的总页数

    public List<Seller> sellers = new ArrayList<>();
    private View mView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private SubscriberOnNextListener getUserDataOnNext;
    private LinearLayout location;
    private TextView addressText;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private String mTableID = "580706aeafdf523f4f0ff73b";
    private String mKeyWord = ""; // 搜索关键字
    private int radiusInMeters = 5000;// 搜索半径 米
    private View mJDHeaderView;
    private CloudSearch.Query mQuery;
    private CloudSearch mCloudSearch;
    private List<CloudItem> mCloudItems;
    private VRefreshLayout mRefreshLayout;
    Activity activity;
    View btnLoadMore;
    TextView textLoadMore;
    boolean firstBuilt = true;
    ListView mListView;
    SellersListAdapter adapter;


    public SellerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_seller, container, false);
        activity = getActivity();
        adapter = new SellersListAdapter(activity, sellers);
        mCloudSearch = new CloudSearch(getActivity());
        mCloudSearch.setOnCloudSearchListener(this);
        mListView = (ListView) mView.findViewById(R.id.listView);
        btnLoadMore = inflater.inflate(R.layout.list_foot, null);
        textLoadMore = (TextView) btnLoadMore.findViewById(R.id.loadmore);
        mListView.addFooterView(btnLoadMore);
        mListView.setAdapter(adapter);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasMore() ){
                    PageLoadData();
                }
            }
        });
        initHeaderView();
        initLoacionSetting();
        initSellerListView();

        return mView;
    }

    private void initHeaderView() {
        mRefreshLayout = (VRefreshLayout) mView.findViewById(R.id.refresh_layout);
        if (mRefreshLayout != null) {
            mJDHeaderView = new JDHeaderView(activity);
            mJDHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(64)));
            mRefreshLayout.setBackgroundColor(Color.DKGRAY);
            mRefreshLayout.setAutoRefreshDuration(400);
            mRefreshLayout.setRatioOfHeaderHeightToReach(1.5f);
            mRefreshLayout.addOnRefreshListener(new VRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    page = 0;
                    sellers.clear();
                    PageLoadData();
                }
            });
        }

        mRefreshLayout.setHeaderView(mJDHeaderView);
        mRefreshLayout.setBackgroundColor(Color.WHITE);


    }

    //#################1.初始化商家列表#####################
    private void initSellerListView() {

        //#################2.声明网络请求回调接口#####################
        getUserDataOnNext = new SubscriberOnNextListener<List<Seller>>() {
            @Override
            public void onNext(List<Seller> sellers) {
//                sellers = sellers;
//
//                //#################4.得到数据后初始化列表#####################
//                if (recyclerView == null) {
//                    //加载RecyclerView
//                    loadRecyclerView();
//                    //加载swipeRefreshLayout
//                    loadRefreshLayout();
//                } else {  //#################5.上拉刷新数据#####################
//                    myItemRecyclerViewAdapter.addDatas(sellers);
//                    recyclerView.notifyMoreFinish(hasMore());
//                }
//                //#################6.下拉刷新数据#####################
//                if (swipeRefreshLayout != null && START == 0) {
//                    myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(sellers);
//                    recyclerView.setAdapter(myItemRecyclerViewAdapter);
//                    //声明使用图片动画加载
//                    recyclerView.setUsePicture(true);
//                    recyclerView.setAutoLoadMoreEnable(hasMore());
//                    recyclerView.setLoadingMore(false);
//                    myItemRecyclerViewAdapter.notifyDataSetChanged();
//                    swipeRefreshLayout.setRefreshing(false);
//                }
            }
        };


    }


    //################网络请求方法#######################
    public void PageLoadData() {
//        HttpMethods.getInstance().getBusinessData(
//                new ProgressSubscriber(getUserDataOnNext, getActivity(), false)
//                , "GetSellerByLocation", "", START + "", PAGESIZE + "");
        searchByBound();
    }

    public void searchByBound() {
        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(OnlineUserInfo.latLonPoint, radiusInMeters);
        try {
            mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
            mQuery.setPageSize(PAGESIZE);
            mQuery.setPageNum(page++);

            CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules(CloudSearch.Sortingrules.DISTANCE);
            mQuery.setSortingrules(sorting);
            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }


    //################判断是否还有数据#######################
    public boolean hasMore() {
        return page != PageCount;
//        return sellers.size()%PAGESIZE== 0;
    }

    public void initLoacionSetting() {
        location = (LinearLayout) mView.findViewById(R.id.linearLayout);
        addressText = (TextView) mView.findViewById(R.id.tv_title);
        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //#################3.进行第一个网络请求#####################
                if (!s.toString().equals("") && OnlineUserInfo.latLonPoint != null) {
                    System.out.println("位置变更：" + s);
                    page = 0;
                    PageLoadData();
                }
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationOption.setOnceLocation(true);
            mlocationClient.startLocation();
        }
    }
//
//    private void refreshSellerList() {
//        btnLoadMore.setEnabled(false);
//        textLoadMore.setText("加载中");
//
//
//        Server.getSharedClient().newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshLayout.refreshComplete();
//                        textLoadMore.setEnabled(true);
//                        textLoadMore.setText("网络异常");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                final String result = response.body().string();
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        mRefreshLayout.refreshComplete();
//
//                        btnLoadMore.setEnabled(true);
//                        textLoadMore.setText("数据解析中");
//                        try {
//
//                            ObjectMapper mapper = new ObjectMapper();
//                            goodsPage = mapper.readValue(result,
//                                    new TypeReference<Page<Goods>>() {
//                                    });
//
//
//                            if (goodsPage.getTotalPages() != page) {
//                                textLoadMore.setText("加载更多");
//                                mGoods.addAll(goodsPage.getContent());
//                                adpter.notifyDataSetChanged();
//                            } else {
//                                page = NOT_MORE_PAGE;
//                                textLoadMore.setText("没有新内容");
//                                mGoods.addAll(goodsPage.getContent());
//                                adpter.notifyDataSetChanged();
//                            }
//
//
//                        } catch (IOException e) {
//                            textLoadMore.setText("数据解析失败" + e.getLocalizedMessage());
//                        }
//                    }
//                });
//
//            }
//        });
//
//
//    }

    private Handler scaleHandler = new Handler();
    private Runnable scaleRunnable = new Runnable() {

        @Override
        public void run() {

            if (firstBuilt) {
                mRefreshLayout.autoRefresh();
                firstBuilt = false;
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        activity.getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {
                scaleHandler.post(scaleRunnable);
            }
        });
        addressText.setText(OnlineUserInfo.myInfo.getAddress());
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                System.out.println(aMapLocation.getAddress() + "  onLocationChanged");
                OnlineUserInfo.latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                addressText.setText(aMapLocation.getPoiName());
                mlocationClient.onDestroy();

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                addressText.setText(errText);
            }
        }
    }

    @Override
    public void onCloudSearched(CloudResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(mQuery)) {
                    PageCount = result.getPageCount();
                    mCloudItems = result.getClouds();
                    if (mCloudItems != null && mCloudItems.size() > 0) {
                        sellers = new ArrayList<>();
                        for (CloudItem item : mCloudItems) {
//                            Log.d(TAG, "_id " + item.getID());
//                            Log.d(TAG, "_location "
//                                    + item.getLatLonPoint().toString());
//                            Log.d(TAG, "_name " + item.getTitle());
//                            Log.d(TAG, "_address " + item.getSnippet());
//                            Log.d(TAG, "_caretetime " + item.getCreatetime());
//                            Log.d(TAG, "_updatetime " + item.getUpdatetime());
//                            Log.d(TAG, "_distance " + item.getDistance());
//                            Iterator iter = item.getCustomfield().entrySet()
//                                    .iterator();
//                            while (iter.hasNext()) {
//                                Map.Entry entry = (Map.Entry) iter.next();
//                                Object key = entry.getKey();
//                                Object val = entry.getValue();
//                                Log.d(TAG, key + "   " + val);
//                            }

                            Seller seller = new Seller();
                            seller.setID(item.getID());
                            seller.setNick(item.getTitle());
                            seller.setDistance(item.getDistance() + "m");
                            seller.setStar(item.getCustomfield().get("star"));
                            seller.setType(item.getCustomfield().get("type"));
                            seller.setEvents(item.getCustomfield().get("events"));
                            seller.setMonth(item.getCustomfield().get("month"));
                            seller.setAvatar(item.getCloudImage().get(0).getPreurl());
                            System.out.println(item.getCloudImage().get(0).getPreurl() + "a" + item.getCloudImage().get(0).getUrl());
                            sellers.add(seller);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.show(getActivity(), "对不起，没有搜索到相关数据！");
                    }
                }
            }
        }
    }

    @Override
    public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {


    }

    @Override
    public void onItemClick(View view, int postion) {
        Seller seller = myItemRecyclerViewAdapter.getDataItem(postion);
        Intent intent = new Intent(getActivity(), SellerDetailsActivity.class);
        intent.putExtra("ID", seller.getID());
        startActivity(intent);
    }

    protected int dp2px(float dp) {
        return Densityutils.dp2px(activity, dp);
    }
}
