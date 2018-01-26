package com.example.cindy.op;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestingPage extends AppCompatActivity {
    ArrayList<String> friendsselected;
    HashMap<String, Double > paymentrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        friendsselected = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesting_page);
        paymentrequest = Main2Activity.friendsrequesting;
        for(String i: paymentrequest.keySet()){
            friendsselected.add(i+ "     " + paymentrequest.get(i).toString());
        }
        System.out.println(paymentrequest.toString()+"PAYMENTREQUEST");

        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(this);
        title.setText("Send Requests");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(30);
        title.setPadding(0, 30, 0, 30);
        linearLayout.addView(title);

        for (int i = 0; i < friendsselected.size(); i++) {
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

        }


    }
}
