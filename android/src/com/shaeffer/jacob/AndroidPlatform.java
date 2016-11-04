package com.shaeffer.jacob;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

import com.shaeffer.jacob.minor.NativePlatform;

public class AndroidPlatform implements NativePlatform {
    Activity context;

    public AndroidPlatform(Activity context)
    {
        this.context = context;
    }

    public String isOnline(boolean wifiOnly)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(wifiOnly)
        {
            NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!mWifi.isConnected()) {
                return "noWifi";
            }
        }
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return "online";
        }
        return "offline";
    }

    @Override
    public void showMessage(String message) {

    }
}
