//package com.example.cindy.op;
//
///**
// * Created by cindy on 26/1/2018.
// */
//
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import org.apache.http.client.HttpClient;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ConnectException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//public class GetTaggunTask extends AsyncTask<URL, Void, Void> {
//    public interface AsyncResponse {
//        void processFinish(Bitmap s);
//    }
//
//    public AsyncResponse delegate = null;
//    private String imgURLStr;
//    Bitmap image;
//    Context context;
//
//    public GetTaggunTask(AsyncResponse delegate, Context context) {
//        this.delegate = delegate;
//        this.context=context;
//    }
//
//
//
//    @Override
//    protected Void doInBackground(URL... urls) {
////        URL url = urls[0];
////        Bitmap xkcdPic = null;
////
////        try{
////            imgURLStr = parseJSON(getHttpURL(url));
////
////            URL imgURL = new URL(imgURLStr);
////            InputStream inputStream = imgURL.openStream();
////            xkcdPic = BitmapFactory.decodeStream(inputStream);
////        }catch(Exception e){
////            e.printStackTrace();
////        }
////
////        return xkcdPic;
//
////
////
////        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.receipt);
////        try {
////            Log.i("Here","No");
//            URL url = urls[0];
////            Log.i("stupid",url.toString());
////            HttpURLConnection c = (HttpURLConnection) url.openConnection();
////            c.setDoInput(true);
////            c.setRequestMethod("POST");
////            c.setRequestProperty("apikey","8ab3e6a0025d11e8ab51516b33cba1b5");
////            c.setDoOutput(true);
////            c.connect();
////            Log.i("Print","Print"+c.getResponseMessage());
////            Log.i("Error","Error Code"+c.getResponseCode());
////            Log.i("Error","Error Code"+c.getErrorStream());
////
////            Log.i("Print","2nd Msg"+c.getRequestProperty("apikey"));
////            OutputStream output = c.getOutputStream();
////            image.compress(Bitmap.CompressFormat.JPEG, 100, output);
////
////            output.close();
////
////            Scanner result = new Scanner(c.getInputStream());
////            String response = result.nextLine();
////            Log.i("Error","Error Code"+c.getResponseCode());
////            Log.e("ImageUploader", "Error uploading image: " + response);
////            result.close();
////        } catch (IOException e) {
////
////            Log.e("ImageUploader", "Error uploading image sian", e);
////        }
////        return null;
//
//        try{
//            connectForMultipart(url);
//        }
//        catch(Exception ex)
//        {
//
//        }
//        return null;
//
//
//    }
//
//    public void connectForMultipart(URL url) throws Exception {
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setDoInput(true);
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Connection", "Keep-Alive");
//        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        conn.connect();
//        OutputStream os = conn.getOutputStream();
//
//
//    }
//
//    public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
//        os.write( (delimiter + boundary + "rn").getBytes());
//        os.write( ("Content-Disposition: form-data; name="" + paramName + ""; filename="" + fileName + ""rn" ).getBytes());
//        os.write( ("Content-Type: application/octet-streamrn" ).getBytes());
//        os.write( ("Content-Transfer-Encoding: binaryrn" ).getBytes());
//        os.write("rn".getBytes());
//
//        os.write(data);
//        os.write("\r\n".getBytes());
//    }
//
//
//    protected void onPostExecute(Bitmap s) {
//        delegate.processFinish(s);
//    }
//
//    private String getHttpURL(URL url) {
//        StringBuilder output = new StringBuilder();
//
//        try {
//            String input;
//            InputStream inputStream = url.openStream();
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//
//            while ((input = reader.readLine()) != null) {
//                output.append(input);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        return output.toString();
//    }
//
//    private String parseJSON(String JSONString) {
//        JSONObject reader;
//        String output = "";
//
//        try {
//            reader = new JSONObject(JSONString);
//            output = reader.getString("img");
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//
//        return output;
//    }
//
//    public String getImgURLStr() {
//        return imgURLStr;
//    }
//}