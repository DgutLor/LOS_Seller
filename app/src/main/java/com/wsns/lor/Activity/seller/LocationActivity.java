package com.wsns.lor.Activity.seller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.wsns.lor.App.OnlineUserInfo;
import com.wsns.lor.R;
import com.wsns.lor.utils.AMapUtil;
import com.wsns.lor.utils.ToastUtil;



/**
 * AMapV2地图中简单介绍一些Marker的用法.
 * Marker动画功能介绍
 */
public class LocationActivity extends Activity implements OnClickListener, LocationSource,
		AMapLocationListener,
		GeocodeSearch.OnGeocodeSearchListener {
	private AMap aMap;
	private MapView mapView;
	private GeocodeSearch geocoderSearch;
	Marker screenMarker = null;
	Boolean flag=true;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private TextView mAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写

		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		
		mAddress = (TextView)findViewById(R.id.tv_address);
		mAddress.setOnClickListener(this);

		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
			@Override
			public void onMapLoaded() {
				addMarkerInScreenCenter();
			}
		});


		aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {

			}

			@Override
			public void onCameraChangeFinish(CameraPosition postion) {
				mAddress.setText("正在定位...");
					getAddress(AMapUtil.convertToLatLonPoint(screenMarker.getPosition()));
			}
		});
	}
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {

		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		etupLocationStyle();
	}



	/**
	 * 在屏幕中心添加一个Marker
	 */
	private void addMarkerInScreenCenter() {
		LatLng latLng = aMap.getCameraPosition().target;
		Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
		screenMarker = aMap.addMarker(new MarkerOptions()
				.anchor(0.5f,0.5f)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
		//设置Marker在屏幕上,不跟随地图移动
	 	screenMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
	}


	/**
	 * 屏幕中心marker 跳动
	 */
	public void startJumpAnimation() {

		if (screenMarker != null ) {
			//根据屏幕距离计算需要移动的目标点
			final LatLng latLng = screenMarker.getPosition();
			Point point =  aMap.getProjection().toScreenLocation(latLng);
			point.y -= dip2px(this,125);
			LatLng target = aMap.getProjection()
					.fromScreenLocation(point);
			//使用TranslateAnimation,填写一个需要移动的目标点
			Animation animation = new TranslateAnimation(target);
			animation.setInterpolator(new Interpolator() {
				@Override
				public float getInterpolation(float input) {
					// 模拟重加速度的interpolator
					if(input <= 0.5) {
						return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
					} else {
						return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
					}
				}
			});
			//整个移动所需要的时间
			animation.setDuration(600);
			//设置动画
			screenMarker.setAnimation(animation);
			//开始动画
			screenMarker.startAnimation();

		} else {
			Log.e("ama","screenMarker is null");
		}
	}
	private void etupLocationStyle(){
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
				fromResource(R.drawable.gps_point));

		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
	}
	//dip和px转换
	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_address:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (mListener != null && aMapLocation != null) {
			if (aMapLocation != null
					&& aMapLocation.getErrorCode() == 0) {
				mAddress.setVisibility(View.VISIBLE);
				mAddress.setText(aMapLocation.getPoiName());
				OnlineUserInfo.myInfo.setAddress(aMapLocation.getPoiName());
				mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

			} else {
				String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
				mAddress.setVisibility(View.VISIBLE);
				mAddress.setText(errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
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

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}


	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getPois() != null) {
				String addressName = result.getRegeocodeAddress().getPois().get(0).toString();
				if (flag) {
					aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
							screenMarker.getPosition(), 18));
					flag=false;
				}
				OnlineUserInfo.latLonPoint=AMapUtil.convertToLatLonPoint(screenMarker.getPosition());
				OnlineUserInfo.myInfo.setAddress(addressName);
				mAddress.setText(addressName);
				//屏幕中心的Marker跳动
				startJumpAnimation();
				deactivate();
			} else {
				mAddress.setText("当前位置");
			}
		} else {
			ToastUtil.showerror(this, rCode);
		}
	}

	@Override
	public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
