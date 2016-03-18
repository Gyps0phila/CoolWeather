package com.example.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gypsophila on 2016/3/18.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;

    private static CoolWeatherDB coolWeatherDB;
    private static SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized CoolWeatherDB getInstance(Context context) {
        //保证单例模式，全局公用则要static
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    /**
     * 将province实例保存在数据库
     */

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("province", null, values);
        }

    }

    /**
     * 从数据库中读书全国所有省份信息
     */

    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Province province = new Province();
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 将City实例存储在数据库
     */

    public void addCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_code", city.getProvinceId());
            db.insert("city", null, values);
        }
    }


    /**
     * 从数据库读书省份剩下对应的所有城市
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city", null, "province_id= ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToFirst()) {
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 将County实例储存到数据库
     */

    public void addCounty(County county) {

        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("county", null, values);
        }
    }

    /**
     * 将从数据库中读出某个城市对应的所有的county
     */

    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("county", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToFirst()) {
                County county = new County();
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }
            cursor.close();
        }
        return list;
    }
}
