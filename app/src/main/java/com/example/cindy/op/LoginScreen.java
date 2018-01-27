package com.example.cindy.op;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button ioubutton = new Button(this);
        Button uombutton = new Button(this);
        EditText userid = new EditText(this);
        EditText password= new EditText(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ioubutton = (Button) findViewById(R.id.ioubutton);
        uombutton = (Button) findViewById(R.id.uombutton);
        userid = findViewById(R.id.useridedittext);
        password = findViewById(R.id.passwordedittext);
        String useridstring = userid.getText().toString();

        SharedPreferences sharedPref = getSharedPreferences("LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString("name", "dorette");
        editor.putString("phone", "82880222");
        editor.commit();


        ioubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectWhoToPayActivity.class);
                startActivity(intent);
            }
        });
        uombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContactsActivity.class);
                startActivity(intent);

            }
        });


    }
}
