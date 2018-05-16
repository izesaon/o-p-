package com.example.cindy.op;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestingPage extends AppCompatActivity {
    ArrayList<String> friendsselected;
    HashMap<String, Double > paymentrequest;
    LinearLayout linearLayout;
    Button continuebutton;
    Button requestbutton;

    String firebaseURL;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        friendsselected = new ArrayList<>();

        firebaseURL = "https://osps-2b8fa.firebaseio.com/";
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesting_page);
        paymentrequest = SelectFriendActivity.friendsrequesting;
        for(String i: paymentrequest.keySet()){
            friendsselected.add(i);
           // friendsselected.add(i+ "     " + paymentrequest.get(i).toString());

        }
        continuebutton = new Button(this);
        requestbutton = new Button(this);


        System.out.println(paymentrequest.toString()+"PAYMENTREQUEST");
        linearLayout = findViewById(R.id.requestinglinear);
        continuebutton = findViewById(R.id.continuingrequest);

      continuebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OptionsPage.class);
                startActivity(intent);

            }
        });

        for (int i = 0; i < friendsselected.size(); i++) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View requestlist = inflater.inflate(R.layout.requestinglist, null);
            // Add the new row before the add field button.
            TextView friendsname = (TextView) requestlist.findViewById(R.id.requestfriend);
            friendsname.setText(friendsselected.get(i));

            TextView price = (TextView) requestlist.findViewById(R.id.price);
            price.setText(paymentrequest.get(friendsselected.get(i)).toString());
            linearLayout.addView(requestlist, linearLayout.getChildCount() - 1);
            requestbutton = requestlist.findViewById(R.id.requestbutton);
            final LinearLayout rowView = (LinearLayout) requestbutton.getParent();


            SharedPreferences sharedPref = this.getSharedPreferences("LOGIN", MODE_PRIVATE);
            String currentName = sharedPref.getString("name", null);
            final String currentPhone = sharedPref.getString("phone", null);
            double currentOwed = Double.parseDouble(paymentrequest.get(friendsselected.get(i)).toString());
            final People toBeAdded = new People(currentOwed, currentName, currentPhone);

            final String owedMe = ContactsActivity.contact_name_hash.get(friendsselected.get(i));

            requestbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("owedme", owedMe);
                    Log.i("currentphone", currentPhone);
                    Log.i("tobeadded", toBeAdded.toString());
                    mDatabase.child(owedMe).child(currentPhone).setValue(toBeAdded);
                    if(((LinearLayout) rowView).getChildCount() > 0)
                        ((LinearLayout) rowView).removeAllViews();
                    int index = ContactsActivity.contact_number_list.indexOf(owedMe);
                    ContactsActivity.contact_number_list.remove(owedMe);
                    String name = ContactsActivity.contact_name_list.get(index);
                    ContactsActivity.contact_name_list.remove(index);
                    ContactsActivity.contact_name_hash.remove(name);
                }
            });

            /*
            TextView textView = new TextView(this);
            textView.setText(friendsselected.get(i));
            textView.setPadding(20, 0, 20, 20);
            textView.setTextSize(13);
            RelativeLayout relativelayout = new RelativeLayout(this);
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            relativelayout.addView(textView);

            Button request = new Button(this);
            request.setText("Request");
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativelayout.addView(request, lp);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            linearLayout.addView(relativelayout);
            */
        }


    }
}
