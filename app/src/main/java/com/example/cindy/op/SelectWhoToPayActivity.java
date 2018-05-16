package com.example.cindy.op;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectWhoToPayActivity extends AppCompatActivity {

    TextView myname;
    TextView mycontact;
    TextView myowed;
    Button clicktopay;

    private DatabaseReference mDatabase;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<People> allPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_who_to_pay);

        myname = (TextView) findViewById(R.id.nameTitle);
        mycontact = (TextView) findViewById(R.id.contactTitle);
        myowed = (TextView) findViewById(R.id.owedTitle);
//        clicktopay = (Button) findViewById(R.id.clickToPayTitle);
//
//        clicktopay.setOnClickListener(
//                new View.OnClickListener() {
//
//
//                    @Override
//                    public void onClick(View v) {
//                        Context context = v.getContext();
//                        Intent intent = new Intent(context, WebViewActivity.class);
//                        startActivity(intent);
//                    }
//                }
//
//        );

        allPeople = new ArrayList<>();
        //extract the phone number of the person who signed in
        SharedPreferences sharedPref = this.getSharedPreferences("LOGIN", MODE_PRIVATE);
        String phone = sharedPref.getString("phone", null);
        Log.i("phone number", phone);
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl("https://osps-2b8fa.firebaseio.com/").child(phone);
        Log.i("mDatabase", mDatabase.toString());
        Log.i("testDatabase", FirebaseDatabase.getInstance().getReferenceFromUrl("https://osps-2b8fa.firebaseio.com/").toString());
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(SelectWhoToPayActivity.this, allPeople);
        recyclerView.setAdapter(recyclerViewAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                    People people = singleSnapShot.getValue(People.class);
                    Log.i("Singlesnapshot", singleSnapShot.toString());
                    Log.i("People", people.toString());
                    //Log.i("yilong", people.Name);
                    //Log.i("yilong", Integer.toString(people.Contact));
                    allPeople.add(people);
                }
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



}
