package com.wsns.lor.seller.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wsns.lor.seller.application.OnlineUserInfo;
import com.wsns.lor.seller.R;

import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;

import static com.wsns.lor.seller.application.OnlineUserInfo.address;
import static com.wsns.lor.seller.application.OnlineUserInfo.poi;


/**
 * 定位Fragment，根据定位选择具体位置，edittext可以修改地字名字
 */
public class LocationFragment extends Fragment implements OnGetGeoCoderResultListener {
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    EditText etInput;
    private LocationMode mCurrentMode;

    Marker marker;
    MapView mMapView;
    BaiduMap mBaiduMap;
    View view;
    Activity activity;
    // UI相关
    OnCheckedChangeListener radioButtonListener;
    boolean isFirstLoc = true; // 是否首次定位
    boolean isFirstSearch = true; // 是否首次定位

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_location, null);

            activity = getActivity();
        }
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mCurrentMode = LocationMode.NORMAL;
        etInput = (EditText) view.findViewById(R.id.et_input);


        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(activity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setScanSpan(0);
        mLocClient.setLocOption(option);


        //调用BaiduMap对象的setOnMarkerDragListener方法设置marker拖拽的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
                //拖拽中
            }

            public void onMarkerDragEnd(Marker marker) {
                //拖拽结束
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(marker.getPosition()));
            }

            public void onMarkerDragStart(Marker marker) {
                //开始拖拽

            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                        address=editable.toString();
            }
        });



        mLocClient.start();

        return view;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(activity, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        OnlineUserInfo.latitude = result.getLocation().latitude;
        OnlineUserInfo.longitude = result.getLocation().longitude;
        etInput.setText(result.getAddress());
        address = result.getAddress();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            System.out.println("onReceiveLocation" + location.getLocationDescribe());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            JMessageClient.getMyInfo().setAddress(location.getAddress().address);
            OnlineUserInfo.latitude = location.getLatitude();
            OnlineUserInfo.longitude = location.getLongitude();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll;
                if (poi != null) {
                     ll = new LatLng((double) poi.getLocation().get(1), (double) poi.getLocation().get(0));
                    etInput.setText(poi.getAddress());
                    System.out.println("" + poi.getLocation().get(0) + "," + poi.getLocation().get(1) + poi.getAddress());

                } else {
                     ll = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    etInput.setText(location.getAddress().address);
                }


                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


//构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.marker);
//构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(ll)
                        .title("长按拖动")
                        .icon(bitmap)
                        .draggable(true);  //设置手势拖拽
//将marker添加到地图上
                marker = (Marker) (mBaiduMap.addOverlay(option));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mSearch.destroy();
        mMapView = null;
        super.onDestroy();
    }


}

