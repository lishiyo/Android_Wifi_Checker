package com.cziyeli.project_one;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by connieli on 5/27/15.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {
    public String ssid;
    public int newRSSI;
    public String bssid;
    TextView textSSID;
    TextView textRSSI;
    WifiManager mWifiManager;

    public WifiBroadcastReceiver(TextView textSSID, TextView textRSSI, WifiManager wifiManager) {
        this.textSSID = textSSID;
        this.textRSSI = textRSSI;
        this.mWifiManager = wifiManager;
    }

    // called whenever RSSI changes
    @Override
    public void onReceive(Context context, Intent intent) {

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();

        ssid = wifiInfo.getSSID();
        newRSSI = wifiInfo.getRssi();
        bssid = wifiInfo.getBSSID();

        Log.d("myapp", ssid + " with newRssi: " + newRSSI + " and bssid: " + bssid);

        textSSID.setText(String.valueOf(ssid));
        textRSSI.setText(String.valueOf(newRSSI) + " dBM");
    }

}
