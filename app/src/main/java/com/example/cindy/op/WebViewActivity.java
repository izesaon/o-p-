package com.example.cindy.op;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    final String uri_BASE = "api.ocbc.com";
    final String uri_PATH1 = "ocbcauthentication";
    final String uri_PATH2 = "api";
    final String uri_PATH3 = "oauth2";
    final String uri_PATH4 = "authorize";
    final String uri_SCHEME = "https";

    String clientID = "mhHTfs4s9sP8cOforSZFEOptTPAa";
    //String redirect = "http://www.mydeeplink.com/semester";
    String redirect = "https://www.google.com";

    String name;
    String amount;
    String contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        name = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEENAME);
        amount = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEEOWED);
        contact = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEECONTACT);

        //set in shared preference here
        //i should implement in shared preference instead of passing through intent in each activities
//        SharedPreferences sharedPref = getSharedPreferences("OWED_INFO", MODE_PRIVATE);
//        SharedPreferences.Editor editor= sharedPref.edit();
//        editor.putString("name", name);
//        editor.putString("amount", amount);
//        editor.putString("contact", contact);
//        editor.commit();
        //String redirectURL ="https://<redirect_uri host>/<redirect_uri path>#access_token=3f86faa0f22934789b71b79ac39ec34f&token_type=Bearer&expires_in=3600";
        //Log.i("check", Boolean.toString(redirectURL.contains("access_token")));

        URL url = buildURL(clientID, redirect);
        Log.i("uri", url.toString());

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String Url){
                if (Url.contains("error")){
                    Log.i("myurl", Url);
                    finish();
                    Context context = webview.getContext();
                    Intent intent = new Intent(context, WaitTransactionActivity.class);
                    intent.putExtra(RecyclerViewHolders.KEY_PAYEENAME, name);
                    intent.putExtra(RecyclerViewHolders.KEY_PAYEEOWED, amount);
                    intent.putExtra(RecyclerViewHolders.KEY_PAYEECONTACT, contact);

                    context.startActivity(intent);
                    return true;
                }
//                Log.i("myurl", Url);
//                finish();
//                Context context = webview.getContext();
//                Intent intent = new Intent(context, WaitTransactionActivity.class);
//                context.startActivity(intent);
//                return true;
                return false;
            }
//            @Override
//            public void onPageFinished(WebView view, String url){
//                super.onPageFinished(view, url);
//
//                if (url.contains("success-url"))
//                {
//                    //call intent to navigate to activity
//                    setResult(RESULT_OK, bundle);
//                    WebViewActivity.this.finish();
//                }
//            }
        });
        //insert url to redirect customers to the weblink to key in their access code and password
        webView.loadUrl(url.toString());
    }

    private URL buildURL(String client_id, String redirect){
        URL loginURL = null;

        Uri.Builder myUriBuilder = new Uri.Builder();
        myUriBuilder.scheme(uri_SCHEME)
                .authority(uri_BASE)
                .appendPath(uri_PATH1)
                .appendPath(uri_PATH2)
                .appendPath(uri_PATH3)
                .appendPath(uri_PATH4)
                .appendQueryParameter("client_id",client_id)
                .appendQueryParameter("redirect_uri", redirect);
        Uri myUri = myUriBuilder.build();

        try{
            loginURL = new URL(myUri.toString());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return loginURL;
    }
}
