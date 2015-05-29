package com.cziyeli.project_one;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DEBUG_TAG = "myapp";
    private RSSIBroadcastReceiver rssiChangeReceiver;
    private WifiScanner mWifiScanner;

    private String ssid;
    private int newRSSI;
    private String bssid;
    TextView textRSSI;
    TextView textSSID;
    EditText ssidInput;
    Button lookupBtn;
    private WifiManager mWifiManager;
    private List<ScanResult> mScanResults;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start scanning for wifi networks
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(mWifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi is disabled ... enabling wifi",
                    Toast.LENGTH_LONG).show();

            mWifiManager.setWifiEnabled(true);
        }

        mWifiManager.startScan();

        /** BROADCAST RECEIVERS **/
        textRSSI = (TextView) findViewById(R.id.main_rssi);
        textSSID = (TextView) findViewById(R.id.main_ssid);
        // Display SSID and RSSI whenever RSSI changes
        rssiChangeReceiver = new RSSIBroadcastReceiver();
        registerReceiver(rssiChangeReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        // Store scan results when scan finished
        mWifiScanner = new WifiScanner();
        registerReceiver(mWifiScanner, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Listen for either soft keyboard enter or click 'Lookup'
        ssidInput = (EditText) findViewById(R.id.main_enter_ssid);
        setupInputListeners();

        // MainActivity is an object that responds to click listener - must have onClick method
        lookupBtn = (Button) findViewById(R.id.button_lookup);
        if (lookupBtn != null) {
            lookupBtn.setOnClickListener(this);
        }
    }

    public class WifiScanner extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                if (pd != null) {
                    pd.dismiss();
                }
                // store mScanResults and launch query
                mScanResults = mWifiManager.getScanResults();
                Log.d(DEBUG_TAG, "received scanresults!");

                if (mScanResults.size() == 0) {
                    Toast.makeText(getApplicationContext(), "no networks found :(", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class RSSIBroadcastReceiver extends BroadcastReceiver {
        // called whenever RSSI changes
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                ssid = wifiInfo.getSSID();
                newRSSI = wifiInfo.getRssi();
                bssid = wifiInfo.getBSSID();

                textSSID.setText(String.valueOf(ssid));
                textRSSI.setText(String.valueOf(newRSSI) + " dBM");
            }
        }
    }


    // find matching SSID in mScanResults
    private boolean lookupSSID(String searchString){
        for (ScanResult scanRes : mScanResults) {
            String currSSID = scanRes.SSID.toLowerCase();
            if (searchString.equals(currSSID)) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                detailIntent.putExtra("ssid", scanRes.SSID);
                detailIntent.putExtra("rssi", scanRes.level);
                detailIntent.putExtra("bssid", scanRes.BSSID);

                startActivity(detailIntent);
                return true;
            }
        }

        // didn't find Anything
        Toast.makeText(getApplicationContext(), "Sorry, couldn't find " + searchString, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onClick(View selectedView) {
        if (selectedView.getId() == R.id.button_lookup) {
            // only run query if we have scanResults
            if (mScanResults.size() > 0) {
                String searchString = ssidInput.getText().toString();
                if (searchString.length() == 0) { return; }
                lookupSSID(searchString.toLowerCase());
            } else {
                mWifiManager.startScan();
                this.pd = ProgressDialog.show(this, "Wifi Scan", "...scanning for networks!");
            }
        }
    }

    private void setupInputListeners(){
        ssidInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // callOnClick only calls attached listener vs performClick (does everything)
                lookupBtn.callOnClick();
            }

            return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.demo_reddit) {
            Intent redditIntent = new Intent(this, RedditActivity.class);
            startActivity(redditIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(rssiChangeReceiver);
        unregisterReceiver(mWifiScanner);
    }

    @Override
    protected void onResume() {
        registerReceiver(rssiChangeReceiver, new IntentFilter("android.net.wifi.RSSI_CHANGED"));
        registerReceiver(mWifiScanner, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
