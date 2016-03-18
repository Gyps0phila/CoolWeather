package com.example.coolweather.util;

/**
 * Created by Gypsophila on 2016/3/18.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
