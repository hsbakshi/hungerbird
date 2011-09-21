package com.hrishibakshi.reddroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

public class BernieActivity extends Activity {
	
	WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bernie);
        
        webview = (WebView) findViewById(R.id.bernie_webview);
        
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.setWebViewClient(new HelloWebViewClient()); 
        webview.loadUrl("http://i.reddit.com/r/programming");

        WebSettings settings = webview.getSettings(); 
        settings.setJavaScriptEnabled(true); 
        settings.setDatabaseEnabled(true);
        settings.setBuiltInZoomControls(true);
        
        String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
        settings.setDatabasePath(databasePath);
        webview.setWebChromeClient(new WebChromeClient() { 
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) { 
                quotaUpdater.updateQuota(5 * 1024 * 1024); 
            } 
        }); 
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
	        webview.goBack(); 
	        return true; 
	    } 
	    return super.onKeyDown(keyCode, event); 
	} 
	private class HelloWebViewClient extends WebViewClient { 
	    public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	        view.loadUrl(url); 
	        return true; 
	    } 
	}
}
