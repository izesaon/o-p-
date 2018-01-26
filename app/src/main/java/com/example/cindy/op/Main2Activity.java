package com.example.cindy.op;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main2Activity extends AppCompatActivity {

    ArrayList<String> textArray = new ArrayList<>();
    Button selectfriends;
    ArrayList mSelectedItems;
    HashMap<String, Double> friendspayment;
    public static HashMap<String, Double> friendsrequesting;
    //taken from dialog
    ArrayList<String> friendsselected;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText("Food Buddies");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(30);
        title.setPadding(0, 30, 0, 30);
        linearLayout.addView(title);
        String myJsonData = readTxt(R.raw.receipts);
        parseJson(myJsonData);
        System.out.println(friendspayment.toString() + "DICTIONARY");
        //textArray.add(myJsonData);
        friendsrequesting = new HashMap<>();
        //to be changed
        friendsselected = new ArrayList<String>();
        friendsselected.add("Dorette");



        for (int i = 0; i < textArray.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(textArray.get(i));
            textView.setPadding(20, 0, 20, 20);
            textView.setTextSize(13);
            textView.setId(1000);
            final TextView friendsnames = new TextView(this);

            RelativeLayout relativelayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            relativelayout.addView(textView);
            selectfriends = new Button(this);
            selectfriends.setText("Select Friends");
            selectfriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogBox dialog = new DialogBox();
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    dialog.show(fragmentManager, "dialogbox");
                    //System.out.println("FRIENDSELECTED" + dialog.friendsselected.toString());
                }
            });
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativelayout.addView(selectfriends, lp);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            linearLayout.addView(relativelayout);
            linearLayout.addView(friendsnames);

            String nameprice = textArray.get(i);
            String[] namepricelist = nameprice.split("-");
            String name = namepricelist[0];
            Double price = Double.parseDouble(namepricelist[1]);


            for (String k : friendsselected) {
                if (friendsrequesting.containsKey(k) == false) {
                    friendsrequesting.put((String) k, price / friendsselected.size());
                } else if (friendsrequesting.containsKey(k)) {
                    Double initialprice = friendsrequesting.get(k);
                    friendsrequesting.remove(k);
                    friendsrequesting.put(k, (double) Math.round((initialprice+price/friendsselected.size())*100.0/100.0));
                }
            }
            System.out.println("FRIENDSPAYMENT" + friendsrequesting.toString());
        }

        Button continuebutton = new Button(this);
        continuebutton.setText("Continue");
        continuebutton.setId(R.id.continuebutton);
        linearLayout.addView(continuebutton);

        //move to new activity
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestingPage.class);
                startActivity(intent);

            }
        });

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

        try {
            friendspayment = new HashMap<String, Double>();
            JSONObject jsonObject = new JSONObject(jsonString.trim());
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                textArray.add(key + " - " + jsonObject.get(key));
                friendspayment.put(key, (Double) jsonObject.get(key));
              /*  if (jsonObject.get(key) instanceof JSONObject) {
                    textArray.add(key + " - " + jsonObject.get(key));
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
