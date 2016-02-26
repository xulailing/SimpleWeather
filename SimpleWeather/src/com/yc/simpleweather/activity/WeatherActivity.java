package com.yc.simpleweather.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yc.simpleweather.activity.GsonBean.ShowapiResBodyEntity.F1Entity;
import com.yc.simpleweather.activity.GsonBean.ShowapiResBodyEntity.F2Entity;
import com.yc.simpleweather.activity.GsonBean.ShowapiResBodyEntity.F3Entity;

public class WeatherActivity extends Activity {
	// ���ذ�ť
	private ImageView weBackImg;
	// ��������
	private TextView datey;
	// ����
	private TextView cityField;
	// ��������
	private TextView currentTemp;
	// ��������ͼ��
	private ImageView weather_icon01;
	// ��������
	private TextView currentWeather;
	// ���շ���
	private TextView currentWind;
	// ����
	private TextView currentWindPower;
	// ��������ͼ��
	private ImageView weather_icon02;
	// ��������
	private TextView weather02;
	// ��������
	private TextView temp02;
	// ���շ���
	private TextView wind02;
	// ��������ͼ��
	private ImageView weather_icon03;
	// ��������
	private TextView weather03;
	// ��������
	private TextView temp03;
	// �������
	private TextView wind03;
	// ���հ���������ͼƬ��ַ
	private String weather_icon01UrlDay;
	// ���հ���������ͼƬ��ַ
	private String weather_icon02UrlDay;
	// ���հ���������ͼƬ��ַ
	private String weather_icon03UrlDay;
	// ��������������ͼƬ��ַ
	private String weather_icon01UrlNight;
	// ��������������ͼƬ��ַ
	private String weather_icon02UrlNight;
	// ��������������ͼƬ��ַ
	private String weather_icon03UrlNight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		initView();
		getWeather();
	}

	/**
	 * �ؼ��ĳ�ʼ��
	 */
	private void initView() {
		weBackImg = (ImageView) findViewById(R.id.weBackImg);
		weBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WeatherActivity.this.finish();
			}
		});
		weather_icon01 = (ImageView) findViewById(R.id.weather_icon01);
		weather_icon02 = (ImageView) findViewById(R.id.weather_icon02);
		weather_icon03 = (ImageView) findViewById(R.id.weather_icon03);

		datey = (TextView) findViewById(R.id.date_y);
		cityField = (TextView) findViewById(R.id.cityField);
		currentTemp = (TextView) findViewById(R.id.currentTemp);
		currentWeather = (TextView) findViewById(R.id.currentWeather);
		currentWind = (TextView) findViewById(R.id.currentWind);
		currentWindPower = (TextView) findViewById(R.id.currentWindPower);
		weather02 = (TextView) findViewById(R.id.weather02);
		temp02 = (TextView) findViewById(R.id.temp02);
		wind02 = (TextView) findViewById(R.id.wind02);
		weather03 = (TextView) findViewById(R.id.weather03);
		temp03 = (TextView) findViewById(R.id.temp03);
		wind03 = (TextView) findViewById(R.id.wind03);
	}

	@SuppressWarnings("unchecked")
	private void getWeather() {
		String thisCity;
		// ���ݳ�����ȥ����������Ϣ�����Բο��·���word�ĵ���ApiStoreSDK(Android) ʹ��˵���ĵ� ��
		Parameters para = new Parameters();
		Intent i = super.getIntent();
		int cityFlag = i.getExtras().getInt("cityFlag");
		if (cityFlag == 0) {
			thisCity = i.getStringExtra("city");
		} else {
			thisCity = i.getStringExtra("mycity");
		}
		para.put("area", thisCity);
		ApiStoreSDK
				.execute(
						"http://apis.baidu.com/showapi_open_bus/weather_showapi/address",
						ApiStoreSDK.GET, para, new ApiCallBack() {
							@Override
							public void onSuccess(int status,
									String responseString) {
								// ʹ��Gson��������
								Gson gson = new Gson();
								java.lang.reflect.Type type = new TypeToken<GsonBean>() {
								}.getType();
								GsonBean gsonBean = gson.fromJson(
										responseString, type);
								// ����
								datey.setText(getDate());
								// ����
								cityField.setText(gsonBean
										.getShowapi_res_body().getCityInfo()
										.getC3());
								F1Entity f1 = gsonBean.getShowapi_res_body()
										.getF1();
								F2Entity f2 = gsonBean.getShowapi_res_body()
										.getF2();
								F3Entity f3 = gsonBean.getShowapi_res_body()
										.getF3();
								weather_icon01UrlDay = f1.getDay_weather_pic();
								weather_icon02UrlDay = f2.getDay_weather_pic();
								weather_icon03UrlDay = f3.getDay_weather_pic();
								Log.v("Log", weather_icon03UrlDay);

								weather_icon01UrlNight = f1
										.getNight_weather_pic();
								weather_icon02UrlNight = f2
										.getNight_weather_pic();
								weather_icon03UrlNight = f3
										.getNight_weather_pic();
								currentTemp.setText(f1
										.getNight_air_temperature()
										+ "��~"
										+ f1.getDay_air_temperature() + "��");
								temp02.setText(f2.getNight_air_temperature()
										+ "��~" + f2.getDay_air_temperature()
										+ "��");
								temp03.setText(f3.getNight_air_temperature()
										+ "��~" + f3.getDay_air_temperature()
										+ "��");
								Calendar c = Calendar.getInstance();
								c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
								// ��ȡ��ǰ��Сʱֵ
								int hours = c.get(Calendar.HOUR_OF_DAY);
								if (hours >= 6 && hours <= 18) {
									// ����ʱ������ʾͼƬ
									weather_icon01
											.setImageBitmap(getHttpBitmap(weather_icon01UrlDay));
									weather_icon02
											.setImageBitmap(getHttpBitmap(weather_icon02UrlDay));
									weather_icon03
											.setImageBitmap(getHttpBitmap(weather_icon03UrlDay));

									weather03.setText(f3.getDay_weather());
									wind03.setText(f3.getDay_wind_direction());
									weather02.setText(f2.getDay_weather());
									wind02.setText(f2.getDay_wind_direction());
									currentWeather.setText(f1.getDay_weather());
									currentWind.setText(f1
											.getDay_wind_direction());
									currentWindPower.setText(f1
											.getDay_wind_power());
								} else {
									weather_icon01
											.setImageBitmap(getHttpBitmap(weather_icon01UrlNight));
									weather_icon02
											.setImageBitmap(getHttpBitmap(weather_icon02UrlNight));
									weather_icon03
											.setImageBitmap(getHttpBitmap(weather_icon03UrlNight));

									weather03.setText(f3.getNight_weather());
									wind03.setText(f3.getNight_wind_direction());
									weather02.setText(f2.getNight_weather());
									wind02.setText(f2.getNight_wind_direction());
									currentWeather.setText(f1
											.getNight_weather());
									currentWind.setText(f1
											.getNight_wind_direction());
									currentWindPower.setText(f1
											.getNight_wind_power());
								}

							}

							@Override
							public void onComplete() {
								Log.v("Log", "����ɹ�");
							}

							@Override
							public void onError(int status,
									String responseString, Exception e) {
								Log.v("Log", "����ʧ��");

							}

						});

	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @return
	 */
	private String getDate() {
		final Calendar c = Calendar.getInstance();
		// ����ʱ��
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mYear = String.valueOf(c.get(Calendar.YEAR));
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if ("1".equals(mWay)) {
			mWay = "��";
		} else if ("2".equals(mWay)) {
			mWay = "һ";
		} else if ("3".equals(mWay)) {
			mWay = "��";
		} else if ("4".equals(mWay)) {
			mWay = "��";
		} else if ("5".equals(mWay)) {
			mWay = "��";
		} else if ("6".equals(mWay)) {
			mWay = "��";
		} else if ("7".equals(mWay)) {
			mWay = "��";
		}
		return mYear + "��" + mMonth + "��" + mDay + "��" + "(����" + mWay + ")";
	}

	/**
	 * ͨ��Url��������˵�ͼƬ
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL picUrl;
		Bitmap bitmap = null;
		try {
			picUrl = new URL(url);
			// ����������
			HttpURLConnection conn = (HttpURLConnection) picUrl
					.openConnection();
			// ��ȡ����
			InputStream is = conn.getInputStream();
			// ���ݽ���
			bitmap = BitmapFactory.decodeStream(is);
			// �ر�������
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
