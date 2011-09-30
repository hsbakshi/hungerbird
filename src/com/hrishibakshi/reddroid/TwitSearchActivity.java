package com.hrishibakshi.reddroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import 	java.net.URLEncoder;

public class TwitSearchActivity extends ListActivity {
	private static final String TAG = "TwitSearchActivity";
	
	static String mQuery = null; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	        
	    Log.i(TAG, "onCreate called");
	    String input = getIntent().getExtras().getString("tweetQuery");
        Log.i(TAG,"gotInput:"+input);
        mQuery = input;
        new TweetGetterTask().execute();
    }
	 
	public void fillScreenWithTweets(ArrayList<String> tweets) {
	   	//mTrendsList = trends;
	 	setListAdapter(new ArrayAdapter<String>(this, R.layout.trenditem, tweets));
	}
	
	private class TweetGetterTask extends AsyncTask<String, Integer, ArrayList<String>> {
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			ArrayList<String> tweets = null;
			try {
				String url = "http://search.twitter.com/search.json?rpp=50&q=" + URLEncoder.encode(mQuery);
				Log.i (TAG, "url:"+url);
				JSONObject jsonObj = RestJsonClient.getObject(url);
				if (jsonObj == null) {
					Log.d(TAG,"json:"+jsonObj);
					return null;
				}
	
				tweets = new ArrayList<String>();
				JSONArray trendsJson = jsonObj.getJSONArray("results");
				
				JSONObject trend = null;
				if (trendsJson != null) { 
				   for (int i=0;i<trendsJson.length();i++){
					   trend = trendsJson.getJSONObject(i);
					   tweets.add(trend.getString("from_user")+": "+trend.getString("text"));
				   }
				   Log.d(TAG,"trends:"+tweets.toString());
				} 
	        	
	            return tweets;    
	        } catch (Exception e) {
	        	Log.e(TAG, "failed", e);
	        }
			return null;
		}
		
		@Override
		public void onPostExecute(ArrayList<String> tweets) {
			//dialog.dismiss();
			if (tweets != null) {
				fillScreenWithTweets (tweets);
			} else {
				setContentView(R.layout.main);
				TextView textView = (TextView) findViewById(R.id.hello);
				textView.setText(R.string.connection_failed);
				Log.e(TAG,"trendsList is null");
			}
		}
	}
}
