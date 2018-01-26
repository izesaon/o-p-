package com.example.cindy.op;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SelectFriendActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);

        String myJsonData = readTxt(R.raw.receipts);
        parseJson(myJsonData);
        Log.i("JW", friendsPayment.toString() + "DICTIONARY");
        friendlist.add("Dorette");
        friendlist.add("Jia Wen");
        friendsrequesting = new HashMap<>();
        parentLinearLayout = (LinearLayout) findViewById(R.id.selectfriend);

        for (int i = 0; i < foodItems.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.food_item, null);
            // Add the new row before the add field button.
            TextView textView1 = (TextView) rowView.findViewById(R.id.food_name);
            textView1.setText(foodItems.get(i));
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        }

        Button continuebutton = new Button(this);
        continuebutton.setText("Continue");
        continuebutton.setId(R.id.continuebutton);
        parentLinearLayout.addView(continuebutton);

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

                        TextView textView1 = (TextView) rowView.findViewById(R.id.selected_friends);
                        textView1.setText(selectedIndex);
                        TextView nameprice = (TextView) rowView.findViewById(R.id.food_name);
                        String[] namepricelist = nameprice.getText().toString().split("-");
                        System.out.println(nameprice.getText().toString()+"food name");
                        String name = namepricelist[0];
                        Double price = Double.parseDouble(namepricelist[1]);
                        for (String k : friendsselected) {
                            if (friendsrequesting.containsKey(k) == false) {
                                friendsrequesting.put((String) k, price / friendsselected.size());
                            } else if (friendsrequesting.containsKey(k)) {
                                Double initialprice = friendsrequesting.get(k);
                                friendsrequesting.remove(k);
                                friendsrequesting.put(k, (double) Math.round((initialprice + price / friendsselected.size())));
                            }
                        }

                        if (savedfood.contains(name)){
                            for (String k: friendpast){
                                Double initialprice = friendsrequesting.get(k);
                                friendsrequesting.remove(k);
                                friendsrequesting.put(k, (double) Math.round(initialprice - price/friendpast.size()));
                            }
                            System.out.println("friendspast" + friendpast);
                        }


                        if(!savedfood.contains(name)){
                        savedfood.add(name);}

                        friendpast.clear();
                        friendpast.addAll(friendsselected);
                        savednumber= friendpast.size();

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
                friendsPayment.put(key, (Double) jsonObject.get(key));
              /*  if (jsonObject.get(key) instanceof JSONObject) {
                    textArray.add(key + " - " + jsonObject.get(key));
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
