package com.hrishibakshi.hungerbird;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class HttpClient extends AsyncTask<String, Void, String> {
	
	private static final String TAG = "RestJsonClient";
    private AsyncTaskCompleteListener<String> callback;
    private Context context;
    private ProgressDialog dialog;
    
    public HttpClient(AsyncTaskCompleteListener<String> cb, Context context) {
        this.callback = cb;
        this.context = context;
    }
    
	public static String getUrl(String url)
	{
	    String response = null;
	    try {
	    	URL urlObj = new URL(url);
        	HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
        	urlConnection.setConnectTimeout(R.integer.timeout);
        	InputStream inStream = new BufferedInputStream(urlConnection.getInputStream());
        	StringBuilder total = new StringBuilder(inStream.available());
        	BufferedReader r = new BufferedReader(new InputStreamReader(inStream));
        	String line;
        	while ((line = r.readLine()) != null) {
        	    total.append(line);
        	}
        	inStream.close();
        	response = total.toString();
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage());
        }
	    return response;
	}

	@Override
	protected String doInBackground(String... params) {
		return getUrl(params[0]);
	}
	
	protected void onPreExecute() {
		dialog = ProgressDialog.show(context, "Loading", "Getting trends.");
	}
	
	protected void onPostExecute(String result) {
		dialog.dismiss();
		callback.onTaskComplete(result);
	}
}