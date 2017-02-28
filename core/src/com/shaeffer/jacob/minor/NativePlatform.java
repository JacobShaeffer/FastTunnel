package com.shaeffer.jacob.minor;

public interface NativePlatform {

    public String isOnline(boolean wifiOnly);
    public void showMessage(String message);
}
