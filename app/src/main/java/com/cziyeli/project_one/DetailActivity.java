package com.cziyeli.project_one;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by connieli on 5/27/15.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "myapp";
    private DetailActivityFragment detailFrag;
    private final String DETAIL_FRAGMENT_TAG = "detail_fragment";
    private final String WIFI_DATA = "wifi_data";
    Bundle wifiData;
    String ssid;
    String bssid;
    String rssiText;
    TextView mHeading;
    TextView mSubheading;
    TextView mBSSIDview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mHeading = (TextView) findViewById(R.id.heading);
        mSubheading = (TextView) findViewById(R.id.subheading);

        /** METHOD ONE: onSaveInstanceState **/

        // if not null, onRestoreInstanceState will be called
        if (savedInstanceState == null) {
            Log.d(DEBUG_TAG, "no savedInstanceState - creating wifiData");
            this.wifiData = getIntent().getExtras();
            getAllData(wifiData);
        }

        /** METHOD TWO: Retained UI-less Frag - onCreate/onDestroy not called on config change **/

//        FragmentManager fm = getFragmentManager();
//        // Only create fragment if it hasn't been instantiated already
//        detailFrag = (DetailActivityFragment) fm.findFragmentByTag(DETAIL_FRAGMENT_TAG);
//
//        if (detailFrag == null) {
//            wifiData = getIntent().getExtras();
//            detailFrag = new DetailActivityFragment();
//            // For non-UI frags, use add(Fragment, String tag) to avoid calling onCreateView
//            fm.beginTransaction().add(detailFrag, DETAIL_FRAGMENT_TAG).commit();
//            detailFrag.setData(wifiData);
//        }
//
//        wifiData = detailFrag.getData();
//        getAllData(wifiData);

        // If fragment has UI AND we need to programmatically add it (don't need to here)
        // Don't add to back stack or we won't be able to use setRetainInstance
//        if (!detailFrag.isInLayout()) {
//            fm.beginTransaction()
//              .replace(R.id.activity_detail, detailFrag, DETAIL_FRAGMENT_TAG)
//              .commit();
//        }


        renderTexts();
    }

    private void getAllData(Bundle data){
        this.ssid = data.getString("ssid");
        this.rssiText = String.valueOf(data.getInt("rssi") + " dBm");
        this.bssid = data.getString("bssid");
        Log.d(DEBUG_TAG, "getAllData for: " + this.ssid + " and rssi: " + this.rssiText);
    }

    private void renderTexts(){
        mHeading.setText(this.ssid);
        mSubheading.setText(this.rssiText);

        mBSSIDview = (TextView) findViewById(R.id.subheading_bssid);
        // Faster than checking orientation
        if (mBSSIDview != null) {
            mBSSIDview.setText(this.bssid);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBundle(WIFI_DATA, wifiData);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "restoring instance state");
        super.onRestoreInstanceState(savedInstanceState);
        this.wifiData = savedInstanceState.getBundle(WIFI_DATA);
        getAllData(wifiData);
        renderTexts();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // store data in the fragment
//        detailFrag.setData(wifiData);
    }

}
