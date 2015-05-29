package com.cziyeli.project_one;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * METHOD TWO: Retained Frag
 */
public class DetailActivityFragment extends Fragment {

    // data object we want to retain
    private Bundle data;
//    private final String WIFI_DATA = "wifi_data";
    private static final String DEBUG_TAG = "myapp";

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across activity configuration changes
        // can only be used with fragments not in the back stack
        setRetainInstance(true);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "detailFrag onCreateView");
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void setData(Bundle wifiData) {
        this.data = wifiData;
    }

    public Bundle getData() {
        return data;
    }

}
