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
import java.util.HashMap;
import java.util.UUID;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.LinearLayout.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 65535;
    private LinearLayout parentLinearLayout;
    public static ArrayList<String> contact_name_list = new ArrayList<>();
    public static ArrayList<String> contact_number_list = new ArrayList<>();
    Button button;
    Button continuecontacts;
    FrameLayout frame;
    String sandBoxUrl;
    static String foodjson;

    final String authCode = "9ac4211001c511e8ab51516b33cba1b5";

    //private Uri filePath;

    String capturedImageString;

    public static HashMap<String, String> contact_name_hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        frame = new FrameLayout(this);
        frame = findViewById(R.id.frame);
        button = new Button(this);
        button = findViewById(R.id.button1);
        continuecontacts = new Button(this);
        continuecontacts = findViewById(R.id.continuecontacts);
        continuecontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectFriendActivity.class);
                intent.putExtra("foodjson",foodjson);
                startActivity(intent);
            }
        });

        parentLinearLayout = (LinearLayout) findViewById(R.id.ll_example);
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

        contact_name_hash = new HashMap<>();

    }

    public void pickContact(View v) {
        frame.setVisibility(View.INVISIBLE);
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be using multiple startActivityForResult
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        button.setVisibility(View.VISIBLE);
        continuecontacts.setVisibility(View.VISIBLE);
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_example);
//            // Add textview
//            TextView textView1 = new TextView(this);
//            textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT));
//            textView1.setText(name);
//            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
//            linearLayout.addView(textView1);

            if (!contact_name_list.contains(name)) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.contact_item, null);
                // Add the new row before the add field button.
                TextView textView1 = (TextView) rowView.findViewById(R.id.contact_name);
                textView1.setText(name);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);

                contact_name_list.add(name);
                contact_number_list.add(phoneNo);
                contact_name_hash.put(name, phoneNo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDelete(View v) {
        View rowView = (View) v.getParent();
       TextView textView1 = (TextView) rowView.findViewById(R.id.contact_name);
       String name = textView1.getText().toString();
        int index = contact_name_list.indexOf(name);
        contact_name_list.remove(index);
        contact_number_list.remove(index);
        contact_name_hash.remove(name);


        Log.i("JW", "the contact name deleted is " + name + " at index " + index);


       /* TextView contactname = new TextView(rowView.getContext());
        contactname = rowView.findViewById(R.id.contact_name);
        String contactnamestring = contactname.getText().toString();
        System.out.println(contactnamestring +"contactnamestring");
        contact_name_list.remove(contactnamestring);
        int i = contact_name_list.indexOf(contactnamestring);
        contact_number_list.remove(i);
        contact_name_hash.remove(contactnamestring);*/


        parentLinearLayout.removeView((View) v.getParent());
    }


    class GetResponse extends AsyncTask<String, Void, String> {
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
            Log.i("foodlistjson", parseJson(s));
            foodjson = parseJson(s);
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
                    if (!itemName.equals("")) {
                        foodItemsJSON.put(itemName, amountsData);
                    }
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
