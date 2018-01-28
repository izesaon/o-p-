package com.example.cindy.op;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends AppCompatActivity {
    Button loginbutton;

    String useridstring;
    String passwordstring;

    EditText userid;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button ioubutton = new Button(this);
        loginbutton = new Button(this);
        userid = new EditText(this);
        password= new EditText(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        loginbutton = (Button) findViewById(R.id.login);

        userid = findViewById(R.id.useridedittext);
        password = findViewById(R.id.passwordedittext);





        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useridstring = userid.getText().toString();
                passwordstring = password.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences("LOGIN", MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPref.edit();
                editor.putString("name", useridstring);
                editor.putString("phone", passwordstring);
                editor.commit();

                Intent intent = new Intent(view.getContext(), OptionsPage.class);
                startActivity(intent);
            }
        });



    }
}
