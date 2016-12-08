package com.wsns.lor.Activity.seller;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.brooks.loadmorerecyclerview.LoadMoreRecyclerView;
import com.wsns.lor.Adapter.MyItemRecyclerViewAdapter;
import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.entity.Seller;
import com.wsns.lor.http.HttpMethods;
import com.wsns.lor.http.subscribers.ProgressSubscriber;
import com.wsns.lor.http.subscribers.SubscriberOnNextListener;
import com.wsns.lor.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 商家列表页Fragment
 */
public class SellerFragment extends Fragment implements AMapLocationListener, CloudSearch.OnCloudSearchListener ,MyItemClickListener{


    public int START = 0;
    public int PAGESIZE = 10;//列表一次刷新数目

    public int PAGENUM = 0;//查询第几页的结果，从0开始
    public int PageCount;//搜索结果的总页数

    public List<Seller> moreSeller = new ArrayList<>();
    private View mView;
    private LoadMoreRecyclerView recyclerView;
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

    private CloudSearch.Query mQuery;
    private CloudSearch mCloudSearch;
    private List<CloudItem> mCloudItems;

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
        mCloudSearch = new CloudSearch(getActivity());
        mCloudSearch.setOnCloudSearchListener(this);
        initLoacionSetting();
        initSellerListView();

        return mView;
    }

    //#################1.初始化商家列表#####################
    private void initSellerListView() {

        //#################2.声明网络请求回调接口#####################
        getUserDataOnNext = new SubscriberOnNextListener<List<Seller>>() {
            @Override
            public void onNext(List<Seller> sellers) {
//                moreSeller = sellers;
//
//                //#################4.得到数据后初始化列表#####################
//                if (recyclerView == null) {
//                    //加载RecyclerView
//                    loadRecyclerView();
//                    //加载swipeRefreshLayout
//                    loadRefreshLayout();
//                } else {  //#################5.上拉刷新数据#####################
//                    myItemRecyclerViewAdapter.addDatas(moreSeller);
//                    recyclerView.notifyMoreFinish(hasMore());
//                }
//                //#################6.下拉刷新数据#####################
//                if (swipeRefreshLayout != null && START == 0) {
//                    myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(moreSeller);
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

    private void loadRecyclerView() {
        recyclerView = (LoadMoreRecyclerView) mView.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(moreSeller,getActivity());
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //声明使用图片动画加载
        recyclerView.setUsePicture(true);
        recyclerView.setAutoLoadMoreEnable(hasMore());
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
//                        START += moreSeller.size();
                        PageLoadData();

                    }
                }, 1000);
            }
        });
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void loadRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_color1, R.color.refresh_color2,
                R.color.refresh_color3, R.color.refresh_color4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                START = 0;
                PAGENUM = 0;
                PageLoadData();
            }
        });
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
            mQuery.setPageNum(PAGENUM++);

            CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules(CloudSearch.Sortingrules.DISTANCE);
            mQuery.setSortingrules(sorting);
            mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }


    //################判断是否还有数据#######################
    public boolean hasMore() {
        return  PAGENUM!=PageCount;
//        return moreSeller.size()%PAGESIZE== 0;
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
                    System.out.println("位置变更："+s);
                    PAGENUM=0;
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


    @Override
    public void onResume() {
        if (!addressText.getText().toString().equals(OnlineUserInfo.myInfo.getAddress())&&OnlineUserInfo.myInfo.getAddress()!=null&&!OnlineUserInfo.myInfo.getAddress().equals(""))
            addressText.setText(OnlineUserInfo.myInfo.getAddress());
        super.onResume();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                System.out.println(aMapLocation.getAddress()+"  onLocationChanged");
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
                        moreSeller=new ArrayList<>();
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
                            seller.setID( item.getID());
                            seller.setNick(item.getTitle());
                            seller.setDistance(item.getDistance() + "m");
                            seller.setStar(item.getCustomfield().get("star"));
                            seller.setType(item.getCustomfield().get("type"));
                            seller.setEvents(item.getCustomfield().get("events"));
                            seller.setMonth(item.getCustomfield().get("month"));
                            seller.setAvatar(item.getCloudImage().get(0).getPreurl());
                            System.out.println(item.getCloudImage().get(0).getPreurl()+"a"+item.getCloudImage().get(0).getUrl());
                            moreSeller.add(seller);
                        }
                        //#################4.得到数据后初始化列表#####################
                        if (recyclerView == null) {
                            //加载RecyclerView
                            loadRecyclerView();
                            //加载swipeRefreshLayout
                            loadRefreshLayout();
                        } else {  //#################5.上拉刷新数据#####################
                            myItemRecyclerViewAdapter.addDatas(moreSeller);
                            recyclerView.notifyMoreFinish(hasMore());
                        }
                        //#################6.下拉刷新数据#####################
                        if (swipeRefreshLayout != null && PAGENUM == 1) {
                            myItemRecyclerViewAdapter.setData(moreSeller);

                            //声明使用图片动画加载
                            recyclerView.setUsePicture(true);
                            recyclerView.setAutoLoadMoreEnable(hasMore());
                            recyclerView.setLoadingMore(false);
                            myItemRecyclerViewAdapter.setOnItemClickListener(this);
                            myItemRecyclerViewAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }

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
        Seller seller=myItemRecyclerViewAdapter.getDataItem(postion);
        Intent intent=new Intent(getActivity(),SellerDetailsActivity.class);
        intent.putExtra("ID",seller.getID());
        startActivity(intent);
    }
}
