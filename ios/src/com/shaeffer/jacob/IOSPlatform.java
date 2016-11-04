package com.shaeffer.jacob;

import com.shaeffer.jacob.minor.NativePlatform;


public class IOSPlatform implements NativePlatform {


    public IOSPlatform()
    {

    }

    //return values:
    //online
    //offline
    //noWifi
    public String isOnline(boolean wifiOnly)
    {
        return "offline";
    }

    @Override
    public void setBannerPref(boolean onOff) {

    }

    @Override
    public void setInterstitialPrefs(boolean onOff) {

    }

    @Override
    public void setInterstitialPrefs(int rarity) {

    }

    public void run_on_the_ui_thread() {
    }
}
