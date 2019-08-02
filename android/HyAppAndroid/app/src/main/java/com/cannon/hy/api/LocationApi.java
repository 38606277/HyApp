package com.cannon.hy.api;

import android.content.Context;
import android.location.LocationManager;

public class LocationApi {

    private Context mContext;

    public LocationApi(Context mContext) {
        this.mContext = mContext;
    }

    public void startLocation(){
        //获取LocationManager
        LocationManager lManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);




    }


}
