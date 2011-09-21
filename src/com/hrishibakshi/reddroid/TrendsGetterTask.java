package com.hrishibakshi.reddroid;

import java.util.ArrayList;

import org.json.JSONArray;


import android.os.AsyncTask;
import android.util.Log;

public abstract class TrendsGetterTask extends AsyncTask<Void, Long, ArrayList<String>> {

	private static final String TAG = "TrendsGetterTask";
	
	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		ArrayList<String> trends = null;
		try {
			String url = "http://api.twitter.com/1/trends/1.json";
			JSONArray jsonArray = RestJsonClient.connect(url);
			if (jsonArray == null)
				return null;

			trends = new ArrayList<String>();
			JSONArray trendsJson = jsonArray.getJSONObject(0).getJSONArray("trends");
			String trend = null;
			if (trendsJson != null) { 
			   for (int i=0;i<trendsJson.length();i++){
				   trend = trendsJson.getJSONObject(i).getString("name");
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
