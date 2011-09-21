package com.hrishibakshi.reddroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.util.Log;

public class TwitSearchActivity extends ListActivity {
	private static final String TAG = "TwitSearchActivity";
	
	private class TweetGetterTask extends AsyncTask<String, Integer, ArrayList<JSONObject>> {
		@Override
		protected ArrayList<JSONObject> doInBackground(String... params) {
			ArrayList<JSONObject> trends = null;
			try {
				String url = "http://api.twitter.com/1/trends/1.json";
				JSONArray jsonArray = RestJsonClient.connect(url);
				if (jsonArray == null)
					return null;
	
				trends = new ArrayList<JSONObject>();
				JSONArray trendsJson = jsonArray.getJSONObject(0).getJSONArray("trends");
				JSONObject trend = null;
				if (trendsJson != null) { 
				   for (int i=0;i<trendsJson.length();i++){
					   trend = trendsJson.getJSONObject(i);
					   trends.add(trend);
				   }
				   Log.d(TAG,"trends:"+trends.toString());
				} 
	        	
	            return trends;    
	        } catch (Exception e) {
	        	Log.e(TAG, "failed", e);
	        }
			return null;
		}
	}
}
