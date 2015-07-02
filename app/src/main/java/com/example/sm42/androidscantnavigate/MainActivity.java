package com.example.sm42.androidscantnavigate;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Muh webview!

        final String address = "http://a-chan.nl/ggd/map.html";
        WebView wv = (WebView)findViewById(R.id.webView);

        // Fit page on initial load, allow JS, and enable zooming.

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setLoadWithOverviewMode(true);

        // Fixes some caching issue where the old page was still used.

        wv.clearCache(true);

        // Start browserinstance, load external page (see address).

        final Activity activity = this;
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        // Add interface between android and javascript.
        wv.addJavascriptInterface(new JavascriptInterface(), "Android");

        // Assuming floor plans would be loaded by an external API anyway (as mentioned in the spec).
        wv.loadUrl(address);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // We do nothing here. We're only handling this to keep orientation
        // or keyboard hiding from causing the WebView activity to restart.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Interface between WebView and Android.
     * */
    public class JavascriptInterface{

        /**
         * Handler for the maps clickable area selection.
         * This method gets called from the webview.
         */
        @android.webkit.JavascriptInterface
        public void onSelect(String value){
            Toast.makeText(getBaseContext(), "Selected " + value, Toast.LENGTH_SHORT).show();
        }
    }

}
