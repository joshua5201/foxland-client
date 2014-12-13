package net.cloudapps.abian2016.foxland;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import java.util.List;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getContents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reload) {
            this.getContents();
            return true;
        }
        else if (id == R.id.action_insert) {
            String requestURL = "http://abian2016.cloudapp.net/regions/new";
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.loadUrl(requestURL);
            return true;
        }
        else if (id == R.id.action_about) {
            String requestURL = "http://abian2016.cloudapp.net/regions/about";
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.setWebViewClient(new WebViewClient());
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.loadUrl(requestURL);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean getContents() {
        WifiManager mainWifiObj;
        mainWifiObj = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        class WifiScanReceiver extends BroadcastReceiver {
            public void onReceive(Context c, Intent intent) {
            }
        }
        WifiScanReceiver wifiReciever = new WifiScanReceiver();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
        String requestURL = "http://abian2016.cloudapp.net/regions/query?";
        if (wifiScanList.isEmpty())
            requestURL += "ssids[]=";
        else
            for (int i = 0; i < wifiScanList.size(); i++) {
                requestURL += (i == 0) ? "ssids[]=" : "&ssids[]=";
                requestURL += wifiScanList.get(i).SSID;
                requestURL += wifiScanList.get(i).BSSID;
            }
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(requestURL);
        return true;
    }
}
