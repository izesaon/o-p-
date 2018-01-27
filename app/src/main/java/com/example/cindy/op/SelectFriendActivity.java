package com.example.cindy.op;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SelectFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        String myJsonData = readTxt(R.raw.tiantianreceipt);
        parseJson(myJsonData);
    }

    private String readTxt(int resource) {
        String output = "";

        InputStream inputStream = getResources().openRawResource(resource);

        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            while ((line = reader.readLine()) != null) {
                output = output + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void parseJson(String jsonString) {

        JSONObject foodItemsJSON = new JSONObject();

        try {
            JSONObject jsonObject = new JSONObject(jsonString.trim());

//            JSONObject totalAmount = jsonObject.getJSONObject("totalAmount");
//            String totalAmountData = totalAmount.getString("data");
//            Log.i("JW","totalAmountData=" + totalAmountData);

//            JSONObject taxAmount = jsonObject.getJSONObject("taxAmount");
//            String taxAmountData = taxAmount.getString("data");
//            Log.i("JW","taxAmountData=" + taxAmountData);

//            JSONObject date = jsonObject.getJSONObject("date");
//            String dateData = date.getString("data");
//            Log.i("JW","dateData=" + dateData);

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

//            JSONObject merchantName = jsonObject.getJSONObject("merchantName");
//            String merchantNameData = merchantName.getString("text");
//            Log.i("JW","merchantNameData=" + merchantNameData);

            String foodItemsJSONString = foodItemsJSON.toString();
            Log.i("JW","foodItemsJSONString=" + foodItemsJSONString);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
