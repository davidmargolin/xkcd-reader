package com.tod.android.xkcdreader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Margolin on 7/21/2014.
 */
public class WebViewActivity extends Activity {
    private String url;
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPreferences.getString("themepref", "LIGHT");
        if (theme.contains("DARK")){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserlayout);
        web = (WebView)findViewById(R.id.webView);
        //progressBar=(ProgressBar)findViewById(R.id.progress);
        Bundle bundle = getIntent().getExtras();
        getActionBar().setTitle(bundle.getString("TITLE"));
        url = bundle.getString("LINK");
        final Intent linkintent = getIntent();
        final String action = linkintent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            url = linkintent.getDataString();
            Toast.makeText(getBaseContext(), url,
                    Toast.LENGTH_SHORT).show();
        }
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setLoadWithOverviewMode(true);
        web.loadUrl(url);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
        WebViewActivity.this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            default:
                WebViewActivity.this.finish();
                return super.onOptionsItemSelected(item);
        }
    }
}

