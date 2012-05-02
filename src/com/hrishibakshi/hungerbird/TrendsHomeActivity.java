package com.hrishibakshi.hungerbird;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
public class TrendsHomeActivity extends Activity implements OnItemClickListener, 
		AsyncTaskCompleteListener<String> {
	
	private static final String TAG = "TrendsHomeActivity";

	private static ArrayList<String> mTrendsList = null;
	private ProgressDialog dialog = null;
	private static String trendUrl = "http://api.twitter.com/1/trends/1.json"; 
	
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        final Action searchAction = new CallbackAction(this, "onSearchRequested", 
        			R.drawable.ic_menu_search);
        actionBar.addAction(searchAction);
        Log.i(TAG, "onCreate called");
        if (mTrendsList == null) {
        	new HttpClient(this,this).execute(trendUrl);
        } else {
        	fillScreenWithTrends(mTrendsList);
        }
        ListView myList=(ListView)findViewById(android.R.id.list);
        myList.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hungerbird, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.refresh:
        	new HttpClient(this, TrendsHomeActivity.this).execute(trendUrl);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void fillScreenWithTrends(ArrayList<String> trends) {
		TextView textView = (TextView) findViewById(R.id.hello);
		textView.setVisibility(View.INVISIBLE);
    	mTrendsList = trends;
    	ListView myList=(ListView)findViewById(android.R.id.list);
    	myList.setAdapter(new ArrayAdapter<String>(this, R.layout.trenditem, trends));
    }

	public void onItemClick(AdapterView<?> aView, View view, int position, long id) {
		TextView tview = (TextView) view;
		Log.d(TAG, "onClick Position:"+position+" id:"+ id +" text:"+tview.getText().toString());
		Intent twitSearchIntent = new Intent(getApplicationContext(),
				TwitSearchActivity.class);
		twitSearchIntent.putExtra("tweetQuery", tview.getText().toString());
		startActivity(twitSearchIntent);
	}
	
	private void connFailedMessage() {
		TextView textView = (TextView) findViewById(R.id.hello);
		textView.setVisibility(View.VISIBLE);
		textView.setText(R.string.connection_failed);
		Log.e(TAG,"trendsList is null");
	}
	
	public void onTaskComplete(String result) {
		if (dialog != null) {
			dialog.dismiss();
		}
		try {
			JSONArray jsonArray = new JSONArray(result);
			ArrayList<String>trends = new ArrayList<String>();
			JSONArray trendsJson = jsonArray.getJSONObject(0).getJSONArray("trends");
			String trend = null;
			if (trendsJson != null) { 
			   for (int i=0;i<trendsJson.length();i++){
				   trend = trendsJson.getJSONObject(i).getString("name");
				   trends.add(trend);
			   }
			   Log.d(TAG,"trends:"+trends.toString());
			} 

			fillScreenWithTrends(trends);

		} catch (JSONException e) {
			connFailedMessage();
			Log.e(TAG,"JSONException:"+e.getMessage());
		} catch (NullPointerException e) {
			connFailedMessage();
			Log.e(TAG,"NullPointerException:"+e.getMessage());
		}
	}
}
