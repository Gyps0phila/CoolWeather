package com.example.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

/**
 * Created by Gypsophila on 2016/3/18.
 */
public class WeatherActivity extends BaseAty implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    /**
     * 用于显示发布时间
     */
    private TextView publishText;
    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;
    /**
     * 用于显示气温1
     */
    private TextView temp1Text;
    /**
     * 用于显示气温2
     */
    private TextView temp2Text;
    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;
    /**
     * 切换城市按钮
     */
    private Button switchCity;
    /**
     * 更新天气按钮
     */
    private Button refreshWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化控件
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
//        switchCity = (Button) findViewById(R.id.switch_city);
//        refreshWeather = (Button) findViewById(R.id.refresh_weather);

        String countyCode = getIntent().getStringExtra("county_code");

        if (!TextUtils.isEmpty(countyCode)) {
            //根据县代码查询天气，是先获得天气代码，然后再请求一遍网络获取天气信息
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            publishText.setText("同步中...");
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        } else {
            showWeather();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    private void queryWeatherCode(String countyCode) {

        //根据县代码请求回来的数据包含天气情况代码

        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    private void queryFromServer(String address, final String type) {

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (type.equals("countyCode")) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if (type.equals("weatherCode")) {
                    //解析返回的天气json信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
        //根据天气代码请求天气信息
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address,"weatherCode");
    }

    private void showWeather() {

        //从sharedPreferences中读取
        SharedPreferences spf = getSharedPreferences("weather_info", MODE_PRIVATE);

        cityNameText.setText( spf.getString("city_name", ""));
        temp1Text.setText(spf.getString("temp1", ""));
        temp2Text.setText(spf.getString("temp2", ""));
        weatherDespText.setText(spf.getString("weather_desp", ""));
        publishText.setText("今天" + spf.getString("publish_time", "") + "发布");
        currentDateText.setText(spf.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
