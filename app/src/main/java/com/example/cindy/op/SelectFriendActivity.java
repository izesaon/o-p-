package com.example.cindy.op;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SelectFriendActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    Typeface face;

    ArrayList<Integer> mSelectedItems;
    ArrayList<String> foodItems = new ArrayList<>();
    ArrayList<String> contactName = new ArrayList<>();
    HashMap<String, Double> friendsPayment;
    ArrayList<String> friendlist = new ArrayList();
    ArrayList<String> friendsselected = new ArrayList<>();
    ArrayList temp = new ArrayList();
    public static HashMap<String, Double> friendsrequesting;
    ArrayList<String> savedfood = new ArrayList<>();
    int savednumber = 0;
    ArrayList<String> friendpast = new ArrayList<>();
    LinearLayout friendslist;
    String foodjson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
        face = ResourcesCompat.getFont(this, R.font.geosanslight);
        String myJsonData = ContactsActivity.foodjson;

      //  String myJsonData = getIntent().getExtras().getString("foodjson");
        System.out.println("myJSON"+myJsonData);

        //String jsonlist = readTxt(R.raw.receipts);
        try{parseJson(myJsonData);}
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i("JW", friendsPayment.toString() + "DICTIONARY");
        friendlist=ContactsActivity.contact_name_list;
        friendsrequesting = new HashMap<>();
        parentLinearLayout = (LinearLayout) findViewById(R.id.selectedfriendslist);




        for (int i = 0; i < foodItems.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.selectfriends2, null);
            // Add the new row before the add field button.
            TextView textView1 = (TextView) rowView.findViewById(R.id.food_name);
            textView1.setText(foodItems.get(i));
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        }

        Button continuebutton = new Button(this);
        continuebutton = findViewById(R.id.continuebutton);
        //move to new activity
        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RequestingPage.class);
                startActivity(intent);

            }
        });
    }

    public void pickFriend(View v) {
        // where we will store or remove selected items
        mSelectedItems = new ArrayList<Integer>();
        final View rowView = (View) v.getParent();


        AlertDialog.Builder builder = new AlertDialog.Builder(SelectFriendActivity.this);

        // set the dialog title

        builder.setTitle("Choose One or More")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // R.array.choices were set in the resources res/values/strings.xml

                .setMultiChoiceItems(friendlist.toArray(new String[friendlist.size()]), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            // if the user checked the item, add it to the selected items
                            mSelectedItems.add(which);
                            temp.add(friendlist.get(which));
                        } else if (mSelectedItems.contains(which)) {
                            // else if the item is already in the array, remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                            temp.remove(friendlist.get(which));

                        }

                    }

                })

                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // user clicked OK, so save the mSelectedItems results somewhere
                        // here we are trying to retrieve the selected items indices
                        String selectedIndex = "";
                        friendsselected.clear();
                        for (Integer i : mSelectedItems) {
                            selectedIndex += friendlist.get(i) + ", ";
                            friendsselected.add(friendlist.get(i));
                        }
                        System.out.println("FRIENDSELECTED" + friendsselected.toString());


                        Log.i("JW", "Selected index: " + selectedIndex);

                        LinearLayout ll = (LinearLayout) rowView.getParent();

                        TextView textView1 = (TextView) ll.findViewById(R.id.selected_friends);
                        //textView1.setText(selectedIndex);
                        friendslist = ll.findViewById(R.id.linearlayout);
                        friendslist.removeAllViews();

                        for (int i = 0; i < friendsselected.size(); i++) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View selectedfriendsview = inflater.inflate(R.layout.selectedfriendslist, null);
                            // Add the new row before the add field button.
                            TextView friendsname = (TextView) selectedfriendsview.findViewById(R.id.friendname);
                            friendsname.setText(friendsselected.get(i));
                            friendsname.setTypeface(face);
                            friendslist.addView(selectedfriendsview, friendslist.getChildCount() - 1);
                        }

                        TextView nameprice = (TextView) rowView.findViewById(R.id.food_name);
                        String[] namepricelist = nameprice.getText().toString().split("-");
                        System.out.println(nameprice.getText().toString() + "food name");
                        String name = namepricelist[0];
                        Double price = Double.parseDouble(namepricelist[1]);
                        for (String k : friendsselected) {
                            if (friendsrequesting.containsKey(k) == false) {
                                friendsrequesting.put((String) k, round(price / friendsselected.size(),2));
                            } else if (friendsrequesting.containsKey(k)) {
                                Double initialprice = friendsrequesting.get(k);
                                friendsrequesting.remove(k);
                                friendsrequesting.put(k, (double) round(initialprice + price / friendsselected.size(),2));

                            }
                        }
                        System.out.println("FRIENDSREQUESTING" + friendsrequesting.toString());

                        if (savedfood.contains(name)) {
                            for (String k : friendpast) {
                                Double initialprice = friendsrequesting.get(k);
                                friendsrequesting.remove(k);
                                friendsrequesting.put(k, (double) round(initialprice - price / friendpast.size(),2));
                            }
                            System.out.println("friendspast" + friendpast);
                        }


                        if (!savedfood.contains(name)) {
                            savedfood.add(name);
                        }

                        friendpast.clear();
                        friendpast.addAll(friendsselected);
                        savednumber = friendpast.size();

                        System.out.println(friendsrequesting + "FRIENDS REQUEST");

                    }


                })

                .

                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // removes the AlertDialog in the screen
                            }
                        })

                .

                        show();

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
            friendsPayment = new HashMap<String, Double>();
            JSONObject jsonObject = new JSONObject(jsonString.trim());
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                foodItems.add(key + " - " + jsonObject.get(key));
                String value = (String)jsonObject.get(key);
                friendsPayment.put(key, (Double) Double.parseDouble(value));
              /*  if (jsonObject.get(key) instanceof JSONObject) {
                    textArray.add(key + " - " + jsonObject.get(key));
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
