package com.example.cindy.op;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

    String sandBoxUrl;

    final String authCode = "9ac4211001c511e8ab51516b33cba1b5";

    //private Uri filePath;

    String capturedImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        sandBoxUrl= "https://api.taggun.io/api/receipt/v1/verbose/url";
        //POST /api/receipt/v1/verbose/url

        Intent intent = getIntent();
        capturedImageString = intent.getStringExtra(MainActivity.IMAGEKEY);
        Log.i("capturedImageString", capturedImageString);
        //filepath for uploading image into firebase
        //filePath = Uri.parse(capturedImageString);

        //executes async task for POST request and get response
        GetResponse getResponse = new GetResponse();
        getResponse.execute(sandBoxUrl);


    }

    class GetResponse extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... uris) {
            String givenUrl = uris[0];
            String inputLine;
            BufferedReader in;
            StringBuilder body;
            String resultBody = null;

            try{
                URL url = new URL(givenUrl);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                //add request header
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestProperty("apikey", authCode);
                httpCon.setDoOutput(true);

                JSONObject parameters = new JSONObject();
                //URL testImageUrl = new URL("https://firebasestorage.googleapis.com/v0/b/osps-2b8fa.appspot.com/o/photo_2018-01-26_13-54-16.jpg?alt=media&token=356499c1-ec36-4980-9aee-4f2f8cfb876c");
                URL testImageUrl = new URL(capturedImageString);
                parameters.put("url", testImageUrl);
                JSONObject innerParameters = new JSONObject();
                innerParameters.put("x-custom-key", "string");
                parameters.put("headers", innerParameters);
                parameters.put("refresh", "false");
                parameters.put("incognito", "false");
                parameters.put("ipAddress", "");
                parameters.put("language", "en");

                Log.i("parameters", parameters.toString());

                DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
                wr.writeBytes(parameters.toString());
                wr.flush();
                wr.close();

                int responseCode = httpCon.getResponseCode();
                Log.i("repsonsecode", responseCode+"");

                in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                body = new StringBuilder();
                while((inputLine = in.readLine())!=null){
                    body.append(inputLine);
                }
                resultBody = body.toString();
                in.close();

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return resultBody;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            Log.i("preprocessed_result", s);
            Log.i("result", parseJson(s));
        }
    }

    public String parseJson(String jsonString) {

        JSONObject foodItemsJSON = new JSONObject();
        String foodItemsJSONString = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonString.trim());

            JSONArray amounts = jsonObject.getJSONArray("amounts");

            for (int i = 0; i < amounts.length(); i++) {
                JSONObject amountsObject = amounts.getJSONObject(i);
                String amountsData = amountsObject.getString("data");
                Log.i("JW", "amountsData=" + amountsData);
                String amountsText = amountsObject.getString("text");
                Log.i("JW", "amountsText=" + amountsText);

                if (amountsText.toLowerCase().contains("total") || amountsText.toLowerCase().contains("gst")) {
                    break;
                } else {
                    String itemName = amountsText.replaceAll("[0-9.$]", "").trim();
                    Log.i("JW", "itemName=" + itemName);
                    foodItemsJSON.put(itemName, amountsData);
                }
            }

            foodItemsJSONString = foodItemsJSON.toString();
            Log.i("JW","foodItemsJSONString=" + foodItemsJSONString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodItemsJSONString;

    }



}
