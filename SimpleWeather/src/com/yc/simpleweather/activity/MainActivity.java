package com.yc.simpleweather.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

public class MainActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView scBackImg;
	// 输入框
	private EditText scCityEd;
	// 搜索框
	private Button scCityBtn;
	// 城市列表的GridView
	private GridView scGridView;
	// GridView的适配器
	private SimpleAdapter adapter;
	// 城市名
	private String myCity;
	private List<Map<String, Object>> data;
	private Map<String, Object> map;
	private int cityFlag = 0;
	private String City[] = { "北京", "天津", "上海", "重庆", "河北", "石家庄", "张家口", "廊坊",
			"南京", "武汉", "合肥", "安庆", "菏泽", "临沂", "沈阳", "哈尔滨", "杭州", "六安", "南昌",
			"大连", "佳木斯", "镇江", "温州", "宿州", "亳州", "阜阳", "蚌埠", "淮南", "滁州", "铜陵",
			"池州", "巢湖", "厦门", "景德镇", "济南", "香港", "澳门", "台湾", "泰州" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 初始化控件
		initView();
		// 获取数据
		getData();
		// 添加数据到适配器
		addData();
	}

	private void addData() {
		adapter = new SimpleAdapter(this, data, R.layout.city_item,
				new String[] { "city" }, new int[] { R.id.city });
		scGridView.setAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		for (int i = 0; i < City.length; i++) {
			map = new HashMap<String, Object>();
			map.put("city", City[i]);
			data.add(map);
		}
		return data;
	}

	private void initView() {
		scBackImg = (ImageView) findViewById(R.id.scBackImg);
		scBackImg.setOnClickListener(this);
		scCityBtn = (Button) findViewById(R.id.scCityBtn);
		scCityBtn.setOnClickListener(this);
		scCityEd = (EditText) findViewById(R.id.scCityEd);
		scCityEd.addTextChangedListener(new TextWatcher() {// EditText的文本监听

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// 如果输入框没内容，按钮不出现。有内容则按钮出现
				if (scCityEd.getText().toString().length() == 0) {
					scCityBtn.setVisibility(View.GONE);
				} else {
					scCityBtn.setVisibility(View.VISIBLE);
				}
			}
		});

		scGridView = (GridView) findViewById(R.id.scGridView);
		scGridView.setOnItemClickListener(new OnItemClickListener() {// GridView的item点击事件

					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 取出当前的item
						map = (Map<String, Object>) adapter.getItem(position);
						String r = map.get("city").toString();
						Intent i = new Intent(MainActivity.this,
								WeatherActivity.class);
						i.putExtra("city", r);
						i.putExtra("cityFlag", cityFlag);
						startActivity(i);

					}
				});

		data = new ArrayList<Map<String, Object>>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scBackImg:
			MainActivity.this.finish();
			break;
		case R.id.scCityBtn:
			myCity = scCityEd.getText().toString();
			// 根据城市名去查找天气信息，可以参考下方的word文档（ApiStoreSDK(Android) 使用说明文档 ）
			Parameters para = new Parameters();
			para.put("area", myCity);
			ApiStoreSDK
					.execute(
							"http://apis.baidu.com/showapi_open_bus/weather_showapi/address",
							ApiStoreSDK.GET, para, new ApiCallBack() {
								@Override
								public void onSuccess(int status,
										String responseString) {
									Log.v("r的值", responseString);

									if (responseString.contains("找不到此地名")) {
										new AlertDialog.Builder(
												MainActivity.this).setMessage(
												"找不到此地名").show();
									} else {
										// cityFlag用来区别GridView和搜索的两种跳转方式
										cityFlag = 1;
										Intent z = new Intent(
												MainActivity.this,
												WeatherActivity.class);
										z.putExtra("mycity", myCity);
										z.putExtra("cityFlag", cityFlag);
										startActivity(z);
									}
								}

								@Override
								public void onComplete() {
								}

								@Override
								public void onError(int status,
										String responseString, Exception e) {

								}
							});
			break;

		default:
			break;
		}
	}

}
