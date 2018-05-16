package com.example.cindy.op;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by YiLong on 24/1/18.
 */

public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    public TextView personName;
    public TextView contact;
    public TextView owed;
    public ImageButton clicktopay;
    private List<People> peopleList;

    public static final String KEY_PAYEENAME = "1";
    public static final String KEY_PAYEECONTACT = "2";
    public static final String KEY_PAYEEOWED = "3";

    public RecyclerViewHolders(View itemView, final List<People> peopleList){
        super(itemView);
        this.peopleList = peopleList;
        personName = (TextView) itemView.findViewById(R.id.name);
        contact = (TextView) itemView.findViewById(R.id.contact);
        owed = (TextView) itemView.findViewById(R.id.owed);
        clicktopay =  itemView.findViewById(R.id.clickToPay);

        clicktopay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String payeeName = personName.getText().toString();
                String payeeContact = contact.getText().toString();
                String payeeOwed = owed.getText().toString();

                Context context = v.getContext();
                Intent intent = new Intent(context, PreviewPayActivity.class);

                intent.putExtra(KEY_PAYEENAME, payeeName);
                intent.putExtra(KEY_PAYEECONTACT, payeeContact);
                intent.putExtra(KEY_PAYEEOWED, payeeOwed);
                context.startActivity(intent);
            }
        });
    }
}
