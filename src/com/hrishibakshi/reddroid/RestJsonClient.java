package com.hrishibakshi.reddroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;


import android.util.Log;


public class RestJsonClient {
	
	private static final String TAG = "RestJsonClient";

    public static JSONArray connect(String url)
    {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url); 

        // Execute the request
        HttpResponse response;

        JSONArray jsonArray = null;
        try {
            response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                jsonArray = new JSONArray(result);
                Log.d(TAG,"JsonArray="+jsonArray.toString());
                instream.close();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        	Log.e(TAG, e.getStackTrace().toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	Log.e(TAG, e.getStackTrace().toString());
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.getStackTrace().toString());
		}

        return jsonArray;
    }
    /**
     *
     * @param is
     * @return String
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}