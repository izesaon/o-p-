package com.example.cindy.op;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class WaitTransactionActivity extends AppCompatActivity {

    String name;
    String amount;
    String contact;

    String sandBoxUrl;

    final String accessCode = "ca05df5c478c02241a11a6288aa505b7";

    String firebaseURL;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_transaction);

        Intent intent = getIntent();
        name = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEENAME);
        amount = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEEOWED);
        contact = intent.getStringExtra(RecyclerViewHolders.KEY_PAYEECONTACT);

        sandBoxUrl = "https://api.ocbc.com:8243/transactional/paynow/1.0/sendPayNowMoney";

        firebaseURL = "https://osps-2b8fa.firebaseio.com/";;
        mDatabase = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);;

        GetResponse getResponse = new GetResponse(this);
        getResponse.execute(sandBoxUrl);


        //String action = intent.getAction();
        //Uri data = intent.getData();
        //Log.i("action", action);
        //Log.i("data", data.toString());


    }

    class GetResponse extends AsyncTask<String,Void,String>{

        Context context;

        public GetResponse(Context mContext){
            this.context = mContext;
        }
        @Override
        protected String doInBackground(String... strings) {
            String givenUrl = strings[0];

            String inputLine;
            BufferedReader in;
            StringBuilder body;
            String resultBody=null;
            try{
                URL url = new URL(givenUrl);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                //add request header
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type", "application/json");
                httpCon.setRequestProperty("Accept", "application/json");
                httpCon.setRequestProperty("sessionToken", "ca05df5c478c02241a11a6288aa505b7");
                httpCon.setRequestProperty("Authorization", "Bearer "+accessCode);

                httpCon.setDoOutput(true);

                JSONObject parameters = new JSONObject();
                parameters.put("FromAccountNo", "109875473");
                parameters.put("ProxyType", "MSISDN");
                parameters.put("ProxyValue", "+65"+contact);
                parameters.put("Amount", Double.parseDouble(amount));

                Log.i("parameter", parameters.toString());

                DataOutputStream wr = new DataOutputStream(httpCon.getOutputStream());
                Log.i("yilong", "false");
                wr.writeBytes(parameters.toString());
                wr.flush();
                wr.close();

                int responseCode = httpCon.getResponseCode();
                Log.i("responsecode", responseCode+"");

                in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                body = new StringBuilder();
                while((inputLine = in.readLine())!= null){
                    body.append(inputLine);
                }
                resultBody = body.toString();
                in.close();
                //Log.i("response", body.toString());


            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return resultBody;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("result", s);
            try{
                JSONObject result = new JSONObject(s);
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                if (result.getString("Success").equalsIgnoreCase("true")){
                    builder.setMessage("Your transaction is successful!");
                    builder.setNeutralButton("Return to Main Menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(context, OptionsPage.class);
                            context.startActivity(intent);
                        }
                    });
                    SharedPreferences sharedPref = context.getSharedPreferences("LOGIN", MODE_PRIVATE);
                    String phone = sharedPref.getString("phone", null);
                    mDatabase.child(phone).child(contact).removeValue();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                else{
                    builder.setMessage("Something went wrong.");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }
}
