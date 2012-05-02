package com.hrishibakshi.hungerbird;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.markupartist.android.widget.ActionBar;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import 	java.net.URLEncoder;

public class TwitSearchActivity extends ListActivity implements AsyncTaskCompleteListener<String> {
	private static final String TAG = "TwitSearchActivity";
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.i(TAG, "onCreate called");
	    setContentView(R.layout.main);
	    Intent intent = getIntent();
	    String query = null;
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	query = intent.getStringExtra(SearchManager.QUERY);
	    } else {
	    	Bundle bundle = intent.getExtras();
	    	if (bundle != null) {
	    		query = bundle.getString("tweetQuery");
	    	}
	    }
	    Log.i(TAG,"gotInput:"+query);
	    if (query != null) {
	    	final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
	    	actionBar.setTitle(query);
	    	String url = "http://search.twitter.com/search.json?rpp=50&q="+URLEncoder.encode(query);
	    	new HttpClient(this, this).execute(url);
	    } else {
	    	// TODO: show error on screen
	    }
    }
	 
	public void fillScreenWithTweets(ArrayList<String> tweets) {
		ListView myList=(ListView)findViewById(android.R.id.list);
    	myList.setAdapter(new ArrayAdapter<String>(this, R.layout.trenditem, tweets));
	}
	

	public void onTaskComplete(String result) {
		try {
			JSONObject jsonObj = new JSONObject(result);
			JSONArray tweetsJson = jsonObj.getJSONArray("results");
			
			JSONObject tweet = null;
			ArrayList<String> tweets = new ArrayList<String>();
			if (tweetsJson != null) { 
			   for (int i=0;i<tweetsJson.length();i++) {
				   tweet = tweetsJson.getJSONObject(i);
				   tweets.add(tweet.getString("from_user")+": "+tweet.getString("text"));
				   //String imgUrl = tweet.getString("profile_image_url");
			   }
			} 
			
			fillScreenWithTweets(tweets);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
