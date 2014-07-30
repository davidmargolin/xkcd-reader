package com.tod.android.xkcdreader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        web.setWebViewClient(new AppWebViewClient());
        web.loadUrl(url);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public class AppWebViewClient extends WebViewClient {

        public AppWebViewClient() {
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
    @Override
    public void onBackPressed() {
        WebViewActivity.this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.readability:
                String readabilityjs = "javascript:((function(){window.baseUrl='//www.readability.com';window.readabilityToken='';var s=document.createElement('script');s.setAttribute('type','text/javascript');s.setAttribute('charset','UTF-8');s.setAttribute('src',baseUrl+'/bookmarklet/read.js');document.documentElement.appendChild(s);})())";
                web.loadUrl(readabilityjs);
            case R.id.action_back:
                if (web.canGoBack()){
                    web.goBack();
                }
                return true;
            case R.id.action_forward:
                if (web.canGoForward()){
                    web.goForward();
                }
                return true;
            case R.id.action_reload:
                web.reload();
                return true;
            case R.id.browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(web.getUrl()));
                startActivity(intent);
            default:
                WebViewActivity.this.finish();
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.webview,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

