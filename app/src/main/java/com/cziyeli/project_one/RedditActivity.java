package com.cziyeli.project_one;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by connieli on 5/29/15.
 */
public class RedditActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private static final String DEBUG_TAG = "myapp";
    private static final String POPULAR_URL = "http://www.reddit.com/subreddits/popular.json";
    private static final String REDDIT_BASE = "http://www.reddit.com";

    RedditAsyncTask mTask;
    RedditAdapter mRedditAdapter;
    ListView mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit);

        // What the user will see while list is loading
        ProgressBar progress = (ProgressBar) findViewById(R.id.empty_view);
        getListView().setEmptyView(progress);

        mainListView = getListView();
        mRedditAdapter = new RedditAdapter(this, getLayoutInflater());
        mainListView.setAdapter(mRedditAdapter);
        // click on item to go to webview
        mainListView.setOnItemClickListener(this);

        connectToReddit();
    }

    // METHOD ONE - AsyncTask: set up network task and run
    // see: http://developer.android.com/training/basics/network-ops/connecting.html
    protected void connectToReddit() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mTask = new RedditAsyncTask();
            mTask.execute(POPULAR_URL);
        } else {
            Log.d(DEBUG_TAG, "no network info available");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PopularSubreddit subreddit = (PopularSubreddit) mRedditAdapter.getItem(position);

        if (subreddit.data.mUrl != null) {
            String url = REDDIT_BASE + subreddit.data.mUrl;
            Intent subredditIntent = new Intent(this, WebviewSubreddit.class);
            subredditIntent.putExtra("url", url);
            startActivity(subredditIntent);
        }
    }

    private class RedditAsyncTask extends AsyncTask<String, Integer, PopularSubreddit[]> {

        @Override
        protected PopularSubreddit[] doInBackground(String... params) {
            try {
                String stringUrl = params[0];
                return getData(new URL(stringUrl));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private PopularSubreddit[] getData(URL url) {
            HttpURLConnection conn = null;
            BufferedReader reader;
            StringBuilder sb;
            String line;
            String jsonString;

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);

                // Starts the query
                conn.connect();

                // Convert the InputStream into a string
                reader  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + '\n');
                }
                jsonString = sb.toString();

                // OPTION ONE: massage full results jsonObject
                JSONObject fullJsonData = new JSONObject(jsonString);
                JSONObject jsonData = fullJsonData.optJSONObject("data");
//                JSONArray subreddits = jsonData.optJSONArray("children");

                // OPTION TWO: GSON!
                Gson gson = new Gson();
                String list = jsonData.getString("children");
                PopularSubreddit[] subreddits = gson.fromJson(list, PopularSubreddit[].class);

                return subreddits;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }

        // runs on main thread
        protected void onPostExecute(PopularSubreddit[] subreddits) {
            if (subreddits != null) {
                mRedditAdapter.updateData(subreddits);
//                mRedditAdapter = new RedditAdapter(getBaseContext(), subreddits);
//                setListAdapter(mRedditAdapter);
            }
        }
    }

}
