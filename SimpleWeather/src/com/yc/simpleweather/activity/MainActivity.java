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
	// ���ذ�ť
	private ImageView scBackImg;
	// �����
	private EditText scCityEd;
	// ������
	private Button scCityBtn;
	// �����б��GridView
	private GridView scGridView;
	// GridView��������
	private SimpleAdapter adapter;
	// ������
	private String myCity;
	private List<Map<String, Object>> data;
	private Map<String, Object> map;
	private int cityFlag = 0;
	private String City[] = { "����", "���", "�Ϻ�", "����", "�ӱ�", "ʯ��ׯ", "�żҿ�", "�ȷ�",
			"�Ͼ�", "�人", "�Ϸ�", "����", "����", "����", "����", "������", "����", "����", "�ϲ�",
			"����", "��ľ˹", "��", "����", "����", "����", "����", "����", "����", "����", "ͭ��",
			"����", "����", "����", "������", "����", "���", "����", "̨��", "̩��" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// ��ʼ���ؼ�
		initView();
		// ��ȡ����
		getData();
		// ������ݵ�������
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
		scCityEd.addTextChangedListener(new TextWatcher() {// EditText���ı�����

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
				// ��������û���ݣ���ť�����֡���������ť����
				if (scCityEd.getText().toString().length() == 0) {
					scCityBtn.setVisibility(View.GONE);
				} else {
					scCityBtn.setVisibility(View.VISIBLE);
				}
			}
		});

		scGridView = (GridView) findViewById(R.id.scGridView);
		scGridView.setOnItemClickListener(new OnItemClickListener() {// GridView��item����¼�

					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// ȡ����ǰ��item
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
			// ���ݳ�����ȥ����������Ϣ�����Բο��·���word�ĵ���ApiStoreSDK(Android) ʹ��˵���ĵ� ��
			Parameters para = new Parameters();
			para.put("area", myCity);
			ApiStoreSDK
					.execute(
							"http://apis.baidu.com/showapi_open_bus/weather_showapi/address",
							ApiStoreSDK.GET, para, new ApiCallBack() {
								@Override
								public void onSuccess(int status,
										String responseString) {
									Log.v("r��ֵ", responseString);

									if (responseString.contains("�Ҳ����˵���")) {
										new AlertDialog.Builder(
												MainActivity.this).setMessage(
												"�Ҳ����˵���").show();
									} else {
										// cityFlag��������GridView��������������ת��ʽ
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
