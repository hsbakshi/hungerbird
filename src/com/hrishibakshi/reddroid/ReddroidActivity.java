package com.hrishibakshi.reddroid;


import java.util.ArrayList;

import android.app.ListActivity;
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
import android.widget.TextView;
public class ReddroidActivity extends ListActivity implements OnItemClickListener {
    
	private static final String TAG = "ReddroidActivity";
	//private static final Object THREAD_ADAPTER_LOCK = new Object();
	
	private static ArrayList<String> mTrendsList = null;
	
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "onCreate called");
        if (mTrendsList == null) {
        	new MyTrendsGetterTask().execute();
        } else {
        	fillScreenWithTrends(mTrendsList);
        }
        
        this.getListView().setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reddroid, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.refresh:
        	new MyTrendsGetterTask().execute();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void fillScreenWithTrends(ArrayList<String> trends) {
    	mTrendsList = trends;
    	setListAdapter(new ArrayAdapter<String>(this, R.layout.trenditem, trends));
    }
    
    private class MyTrendsGetterTask extends TrendsGetterTask {

    	ProgressDialog dialog;
		@Override
		public void onPreExecute() {
			dialog = ProgressDialog.show(ReddroidActivity.this, "Loading", "Getting trends.");
		}

		@Override
		public void onPostExecute(ArrayList<String> trends) {
			dialog.dismiss();
			if (trends != null) {
				fillScreenWithTrends(trends);
			} else {
				setContentView(R.layout.main);
				TextView textView = (TextView) findViewById(R.id.hello);
				textView.setText(R.string.connection_failed);
				Log.e(TAG,"trendsList is null");
			}
		}
	}


	public void onItemClick(AdapterView<?> aView, View view, int position, long id) {
		TextView tview = (TextView) view;
		Log.d(TAG, "onClick Position:"+position+" id:"+ id +" text:"+tview.getText().toString());
		Intent twitSearchIntent = new Intent(getApplicationContext(),
				TwitSearchActivity.class);
		twitSearchIntent.putExtra("tweetQuery", tview.getText().toString());
		startActivity(twitSearchIntent);
	}
  
    
}  // End of ReddroidActivity
