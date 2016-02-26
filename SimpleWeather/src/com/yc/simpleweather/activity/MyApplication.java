package com.yc.simpleweather.activity;

import com.baidu.apistore.sdk.ApiStoreSDK;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//°Ù¶Èapi StoreµÄapikey
		ApiStoreSDK.init(this, "2c238fba1b1153bf8894d13f0858ccc4");

	}

}
