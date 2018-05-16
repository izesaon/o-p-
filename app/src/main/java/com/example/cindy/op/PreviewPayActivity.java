package com.example.cindy.op;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PreviewPayActivity extends AppCompatActivity {

    TextView nameView;
    TextView amountView;
    Button confirmButton;

    //public static final String KEY_PAYEENAME = "1";
    //public static final String KEY_PAYEEOWED = "2";
    //public static final String KEY_PAYEECONTACT = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pay);

        Intent intent = getIntent();

        final String name = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEENAME);
        nameView = (TextView)findViewById(R.id.nametext);
        nameView.setText(name);

        final String amount = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEEOWED);
        amountView = (TextView)findViewById(R.id.amounttext);
        amountView.setText(amount);

        final String contact = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEECONTACT);

        confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra(RecyclerViewHolders.KEY_PAYEENAME, name);
                        intent.putExtra(RecyclerViewHolders.KEY_PAYEECONTACT, contact);
                        intent.putExtra(RecyclerViewHolders.KEY_PAYEEOWED, amount);
                        startActivity(intent);
                    }
                }
        );
    }
}
