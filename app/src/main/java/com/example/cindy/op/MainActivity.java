package com.example.cindy.op;

/**
 * Created by cindy on 26/1/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements GetTaggunTask.AsyncResponse {
    EditText editTextComicNo;
    ImageView imageViewComic;
    TextView textViewURL;
    TextView textViewJSON;
    GetTaggunTask getComicTask;

    final String xkcd_BASE = "://api.taggun.io/api/receipt/v1/verbose/file";
//    final String xkcd_TAIL = "info.0.json";
    final String xkcd_SCHEME = "https";
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextComicNo = findViewById(R.id.comic_number);
        imageViewComic = findViewById(R.id.comic_placeholder);
        textViewURL = findViewById(R.id.comic_url);
        image = BitmapFactory.decodeResource(getResources(), R.drawable.receipt);
        textViewJSON = findViewById(R.id.comic_json);
    }

    public void onClickGetComic(View view) {
        Log.i("onClickGetComic","onClickGetComic");
        URL xkcdURL = buildURL();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = (cm == null) ? null : cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            textViewURL.setText(xkcdURL.toString());


            getComicTask = new GetTaggunTask(this,this);
            getComicTask.execute(xkcdURL);
        } else {
            Toast.makeText(this, "Not connected to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processfinish() {
//        textViewJSON.setText(getComicTask.getImgURLStr());
//        imageViewComic.setImageBitmap(s);
        Log.i("Done","Done");
    }

    private URL buildURL() {
//        URL xkcdURL = null;
//        String comicNo = editTextComicNo.getText().toString();
//
//        Uri.Builder myUriBuilder = new Uri.Builder();
//        myUriBuilder.scheme(xkcd_SCHEME)
//                .authority(xkcd_BASE)
//                .appendPath(comicNo);
////                .appendPath(xkcd_TAIL);
//        Uri xkcdUri = myUriBuilder.build();
//
//        try {
//            xkcdURL = new URL(xkcdUri.toString());
//        } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//        }
//
//        return xkcdURL;
        URL xkcdURL=null;
        try{
            xkcdURL = new URL("https://api.taggun.io/api/receipt/v1/verbose/file");

        }

        catch(Exception ex)
        {

        }

        return xkcdURL;
    }


}