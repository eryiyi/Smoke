package com.xiaolong.Smoke.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.*;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.*;
import com.amap.api.navi.view.RouteOverLay;
import com.xiaolong.Smoke.R;
import com.xiaolong.Smoke.SmokeApplication;
import com.xiaolong.Smoke.base.BaseActivity;
import com.xiaolong.Smoke.module.DocObj;
import com.xiaolong.Smoke.util.Constants;
import com.xiaolong.Smoke.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 路径规划页面
 * */
public class SimpleNaviRouteActivity extends BaseActivity implements
		OnClickListener, AMapNaviListener, AMap.OnMarkerClickListener,
		AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter {
	// 起点、终点坐标显示
	// 驾车线路：路线规划、模拟导航、实时导航按钮
	private TextView mDriveRouteButton;
	private TextView mDriveEmulatorButton;
	private TextView mDriveNaviButton;
	// 步行线路：路线规划、模拟导航、实时导航按钮
	private TextView mFootRouteButton;
	private TextView mFootEmulatorButton;
	private TextView mFootNaviButton;
	// 地图和导航资源
	private MapView mMapView;
	private AMap mAMap;
	private AMapNavi mAMapNavi;

	// 起点终点坐标
	private NaviLatLng mNaviStart = new NaviLatLng(39.989614, 116.481763);
	private NaviLatLng mNaviEnd = new NaviLatLng(39.983456, 116.3154950);
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// 规划线路
	private RouteOverLay mRouteOverLay;
	// 是否驾车和是否计算成功的标志
	private boolean mIsDriveMode = true;
	private boolean mIsCalculateRouteSuccess = false;
	private String lat;
	private String lon;

	private Marker marker2;// 有跳动效果的marker对象

	private LatLng latlng = new LatLng(36.061, 103.834);
	private LatLng latlngMine = new LatLng(36.061, 103.834);
	private MarkerOptions markerOption;

	DocObj docObj;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_route);
		lat = getIntent().getExtras().getString("lat");
		lon = getIntent().getExtras().getString("lon");
		docObj = (DocObj) getIntent().getExtras().get("docObj");
		mNaviStart =  new NaviLatLng(SmokeApplication.lat, SmokeApplication.lon);
		latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
		latlngMine = new LatLng(SmokeApplication.lat, SmokeApplication.lon);
		mNaviEnd =   new NaviLatLng(Double.valueOf(lat), Double.valueOf(lon));
		initView(savedInstanceState);

//		mMapView.onCreate(savedInstanceState); // 此方法必须重写

	}

	public void back(View view){
		finish();
	}

	// 初始化View
	private void initView(Bundle savedInstanceState) {
		mAMapNavi = AMapNavi.getInstance(this);
		mAMapNavi.setAMapNaviListener(this);
//		mStartPoints.clear();
//		mEndPoints.clear();
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);

		mDriveNaviButton = (TextView) findViewById(R.id.car_navi_navi);
		mDriveEmulatorButton = (TextView) findViewById(R.id.car_navi_emulator);
		mDriveRouteButton = (TextView) findViewById(R.id.car_navi_route);

		mFootRouteButton = (TextView) findViewById(R.id.foot_navi_route);
		mFootEmulatorButton = (TextView) findViewById(R.id.foot_navi_emulator);
		mFootNaviButton = (TextView) findViewById(R.id.foot_navi_navi);

		mDriveNaviButton.setOnClickListener(this);
		mDriveEmulatorButton.setOnClickListener(this);
		mDriveRouteButton.setOnClickListener(this);

		mFootRouteButton.setOnClickListener(this);
		mFootEmulatorButton.setOnClickListener(this);
		mFootNaviButton.setOnClickListener(this);

		mMapView = (MapView) findViewById(R.id.simple_route_map);
		mMapView.onCreate(savedInstanceState);
		mAMap = mMapView.getMap();
		mRouteOverLay = new RouteOverLay(mAMap, null);
		setUpMap();
	}
	private void setUpMap() {
		mAMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		mAMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		mAMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		mAMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		mAMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		addMarkersToMap();// 往地图上添加marker
	}



	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}




	//计算驾车路线
	private void calculateDriveRoute() {
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			showToast("路线计算失败,检查参数情况");
		}

	}
	//计算步行路线
	private void calculateFootRoute() {
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (!isSuccess) {
			showToast("路线计算失败,检查参数情况");
		}
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void startEmulatorNavi(boolean isDrive) {
		if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
				|| (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
			Intent emulatorIntent = new Intent(SimpleNaviRouteActivity.this,
					SimpleNaviActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Utils.ISEMULATOR, true);
			bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEROUTENAVI);
			emulatorIntent.putExtras(bundle);
			emulatorIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(emulatorIntent);

		} else {
			showToast("请先进行相对应的路径规划，再进行导航");
		}
	}

	private void startGPSNavi(boolean isDrive) {
		if ((isDrive && mIsDriveMode && mIsCalculateRouteSuccess)
				|| (!isDrive && !mIsDriveMode && mIsCalculateRouteSuccess)) {
			Intent gpsIntent = new Intent(SimpleNaviRouteActivity.this,
					SimpleNaviActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean(Utils.ISEMULATOR, false);
			bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEROUTENAVI);
			gpsIntent.putExtras(bundle);
			gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(gpsIntent);
		} else {
			showToast("请先进行相对应的路径规划，再进行导航");
		}
	}
//-------------------------Button点击事件和返回键监听事件---------------------------------
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.car_navi_route:
			mIsCalculateRouteSuccess = false;
			mIsDriveMode = true;
			calculateDriveRoute();
			break;
		case R.id.car_navi_emulator:
			startEmulatorNavi(true);
			break;
		case R.id.car_navi_navi:
			startGPSNavi(true);
			break;
		case R.id.foot_navi_route:
			mIsCalculateRouteSuccess = false;
			mIsDriveMode = false;
			calculateFootRoute();
			break;
		case R.id.foot_navi_emulator:
			startEmulatorNavi(false);
			break;
		case R.id.foot_navi_navi:
			startGPSNavi(false);
			break;

		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {


		return super.onKeyDown(keyCode, event);
	}
	
	//--------------------导航监听回调事件-----------------------------
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		showToast("路径规划出错" + arg0);
		mIsCalculateRouteSuccess = false;
	}

	@Override
	public void onCalculateRouteSuccess() {
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		mIsCalculateRouteSuccess = true;
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

//------------------生命周期重写函数---------------------------	

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		//删除监听 
		AMapNavi.getInstance(this).removeAMapNaviListener(this);
	 
	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		  
		// TODO Auto-generated method stub  
		
	}


	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {

		//文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
		TextOptions textOptions = new TextOptions().position(Constants.BEIJING)
				.text("Text").fontColor(Color.BLACK)
				.backgroundColor(Color.BLUE).fontSize(30).rotate(20).align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
				.zIndex(1.f).typeface(Typeface.DEFAULT_BOLD)
				;
		mAMap.addText(textOptions);

		mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(latlng).title(docObj.getsName()==null?"":docObj.getsName())
				.snippet(docObj.getsAddress()==null?"":docObj.getsAddress()).draggable(true));
		mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.position(latlngMine).title("我的位置")
				.snippet(SmokeApplication.desc).draggable(true));

//		markerOption = new MarkerOptions();
//		markerOption.position(Constants.XIAN);
//		markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//		markerOption.draggable(true);
//		markerOption.icon(BitmapDescriptorFactory
//				.fromResource(R.drawable.arrow));
//		marker2 = mAMap.addMarker(markerOption);
		// marker旋转90度
//		marker2.setRotateAngle(90);

		// 动画效果
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//		mAMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//				.position(Constants.ZHENGZHOU).title("郑州市").icons(giflist)
//				.draggable(true).period(10));

//		drawMarkers();// 添加10个带有系统默认icon的marker
	}

	/**
	 * 绘制系统默认的1种marker背景图片
	 */
	public void drawMarkers() {
		Marker marker = mAMap.addMarker(new MarkerOptions()
				.position(latlng)
				.title("好好学习")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				.draggable(true));
		marker.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.equals(marker2)) {
			if (mAMap != null) {
				jumpPoint(marker);
			}
		}
//		markerText.setText("你点击的是" + marker.getTitle());
		return false;
	}

	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = mAMap.getProjection();
		Point startPoint = proj.toScreenLocation(Constants.XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * Constants.XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * Constants.XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
//				aMap.invalidate();// 刷新地图
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});

	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
//		ToastUtil.show(this, "你点击了infoWindow窗口" + marker.getTitle());
	}

	/**
	 * 监听拖动marker时事件回调
	 */
	@Override
	public void onMarkerDrag(Marker marker) {
		String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
				+ marker.getPosition().latitude + ","
				+ marker.getPosition().longitude + ")";
//		markerText.setText(curDes);
	}

	/**
	 * 监听拖动marker结束事件回调
	 */
	@Override
	public void onMarkerDragEnd(Marker marker) {
//		markerText.setText(marker.getTitle() + "停止拖动");
	}

	/**
	 * 监听开始拖动marker事件回调
	 */
	@Override
	public void onMarkerDragStart(Marker marker) {
//		markerText.setText(marker.getTitle() + "开始拖动");
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(Constants.XIAN).include(Constants.CHENGDU)
				.include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
		mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
//		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//			return null;
//		}
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
//		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
//			return null;
//		}
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
//		if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
//			((ImageView) view.findViewById(R.id.badge))
//					.setImageResource(R.drawable.badge_sa);
//		} else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
//			ImageView imageView = (ImageView) view.findViewById(R.id.badge);
//			imageView.setImageResource(R.drawable.badge_wa);
//		}
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}


}
