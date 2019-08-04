package com.cannon.hy.api;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cannon.hy.model.ResultModel;
import com.cannon.hy.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wendu.dsbridge.CompletionHandler;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * 定位API
 */
public class LocationApi {

    private Context mContext;

    public LocationApi(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取位置信息
     */
    @JavascriptInterface
    public void getLocationInfo(Object obj, CompletionHandler<String> completionHandler){
        //判断是否有权限
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else{
            startLocation(completionHandler);
        }
    }

    /**
     * 开始定位
     * @param completionHandler
     */
    private void startLocation(CompletionHandler<String> completionHandler){
        Criteria c = new Criteria();
        c.setPowerRequirement(Criteria.POWER_LOW);//设置耗电量为低耗电
        c.setBearingAccuracy(Criteria.ACCURACY_COARSE);//设置精度标准为粗糙
        c.setAltitudeRequired(false);//设置海拔不需要
        c.setBearingRequired(false);//设置导向不需要
        c.setAccuracy(Criteria.ACCURACY_LOW);//设置精度为低
        c.setCostAllowed(false);//设置成本为不需要
        //... Criteria 还有其他属性
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = manager.getBestProvider(c, true);
        //得到定位信息
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = null;
        if (!TextUtils.isEmpty(bestProvider)) {
            location = manager.getLastKnownLocation(bestProvider);
        }
        if (null == location){
            //如果没有最好的定位方案则手动配置
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location==null&& manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location==null&&manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

        ResultModel<List<Address>> resultModel = new ResultModel<>();

        if (null == location){
            resultModel.setCode(-1);
            resultModel.setMsg("获取定位失败!");
            Log.i(TAG, "获取定位失败!");
            completionHandler.complete(JsonUtils.toJson(resultModel));
            return;
        }

        //通过地理编码的到具体位置信息
        Geocoder geocoder = new Geocoder(mContext, Locale.CHINESE);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size()<=0) {
                resultModel.setCode(-1);
                resultModel.setMsg("获取定位失败!");
                Log.i(TAG, "获取地址失败!");
            }else{
                resultModel.setCode(200);
                resultModel.setMsg("获取定位成功!");
                resultModel.setData(addresses);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultModel.setCode(-1);
            resultModel.setMsg("获取定位失败!");
        }finally {
            completionHandler.complete(JsonUtils.toJson(resultModel));
        }
    }

}
