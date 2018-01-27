package com.example.cindy.op;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 85500;
    private LinearLayout parentLinearLayout;
    ArrayList<String> contact_name_list = new ArrayList<>();
    ArrayList<String> contact_number_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        parentLinearLayout = (LinearLayout) findViewById(R.id.ll_example);
    }

    public void pickContact(View v)
    {
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
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDelete(View v) {
        View rowView = (View) v.getParent();
//        TextView textView1 = (TextView) rowView.findViewById(R.id.contact_name);
//        String name = textView1.getText().toString();
//        int index = contact_name_list.indexOf(name);
//        contact_name_list.remove(index);
//        contact_number_list.remove(index);
//        Log.i("JW", "the contact name deleted is " + name + " at index " + index);
        parentLinearLayout.removeView((View) v.getParent());
    }
}