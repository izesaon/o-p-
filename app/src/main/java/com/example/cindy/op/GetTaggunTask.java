package com.example.cindy.op;

/**
 * Created by cindy on 26/1/2018.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetTaggunTask extends AsyncTask<URL, Void, Void> {
    private static final String API_KEY = "YOUR API KEY";
    private static final String LINE_FEED = "\r\n";

    public interface AsyncResponse{
        void processfinish();
    }

    public AsyncResponse delegate = null;
    Context context;

    public GetTaggunTask(com.example.cindy.op.GetTaggunTask.AsyncResponse delegate, Context context) {
            this.delegate = delegate;
            this.context=context;
        }

//    public class GetTaggunTask extends AsyncTask<URL, Void, Void> {
//        public interface AsyncResponse {
//            void processFinish(Bitmap s);
//        }
//
//        public com.example.cindy.op.GetTaggunTask.AsyncResponse delegate = null;
//        private String imgURLStr;
//        Bitmap image;
//        Context context;
//
//        public GetTaggunTask(com.example.cindy.op.GetTaggunTask.AsyncResponse delegate, Context context) {
//            this.delegate = delegate;
//            this.context=context;
//        }
//
//
//
        @Override
        protected Void doInBackground(URL... urls) {
        Log.i("doInBackground","do in background");
            URL urlObtained=urls[0];
            String charset = StandardCharsets.UTF_8.name();

            // Your image file to upload
//            File uploadFile = new File("../../../data/tomato_nutrient/iron1.jpg");
            String filePath = context.getFilesDir().getPath().toString() + "/fileName.txt";
            File g = new File(filePath);
//            File f=new File("fileName.txt");
            try
            {

                InputStream inputStream1 = context.getResources().openRawResource(R.raw.receipt); // id drawable
                OutputStream out=new FileOutputStream(g);
                byte buf[]=new byte[1024];
                int len;
                while((len=inputStream1.read(buf))>0)
                    out.write(buf,0,len);
                out.close();
                inputStream1.close();

                String requestURL = urlObtained.toString();

                // Creates a unique boundary based on time stamp
                String boundary = String.valueOf(System.currentTimeMillis());

                // Setup http connection
                try{
                    URL url = new URL(requestURL);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setUseCaches(false);
                    httpConn.setDoOutput(true); // POST method
                    httpConn.setRequestProperty("User-Agent", "API Java example");
                    httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    httpConn.setRequestProperty("apikey", "8ab3e6a0025d11e8ab51516b33cba1b5");

                    httpConn.connect();
                    // Setup OutputStream and PrintWriter
                    OutputStream outputStream = httpConn.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
                    // Add image file to POST request
                    String fieldName = "picture";
                    String fileName = g.getName();
                    writer.append("--" + boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
                    writer.append(LINE_FEED);
                    writer.flush();

                    FileInputStream inputStream = new FileInputStream(g);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                    inputStream.close();

                    writer.append(LINE_FEED);
                    writer.flush();

                    // Send POST request and get response
                    List<String> response = new ArrayList<>();
                    writer.append(LINE_FEED).flush();
                    writer.append("--" + boundary + "--").append(LINE_FEED);
                    writer.close();

                    // Check server status message
                    int status = httpConn.getResponseCode();
                    Log.i("status",httpConn.getContent().toString());
                    Log.i("status",httpConn.getResponseMessage());
                    if (status == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                httpConn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.add(line);
                        }
                        reader.close();
                        httpConn.disconnect();
                    } else {
                        throw new IOException("Server returned non-OK status: " + status);
                    }

                    // Read response
                    Log.i("length","length"+response.size());
                    for (String line : response) {
                        System.out.println(line);
                        Log.i("Response","Response"+line);
                    }
                }
                catch(Exception ex)
                {
                    Log.i("Error",ex.toString());
                }
                Log.i("FINISHED","HAHAHAHAHA");

            }
            catch (IOException e){Log.i("1st Error",e.toString());}


    //            String requestURL = "https://api.peat-cloud.com/v1/image_analysis";

            return null;

        }


//    public static void main(String[] args) throws Exception {
//
//        String charset = StandardCharsets.UTF_8.name();
//
//        // Your image file to upload
//        File uploadFile = new File("../../../data/tomato_nutrient/iron1.jpg");
//        String requestURL = "https://api.peat-cloud.com/v1/image_analysis";
//
//        // Creates a unique boundary based on time stamp
//        String boundary = String.valueOf(System.currentTimeMillis());
//
//        // Setup http connection
//        URL url = new URL(requestURL);
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setUseCaches(false);
//        httpConn.setDoOutput(true); // POST method
//        httpConn.setRequestProperty("User-Agent", "API Java example");
//        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        httpConn.setRequestProperty("api_key", API_KEY);
//
//        // Setup OutputStream and PrintWriter
//        OutputStream outputStream = httpConn.getOutputStream();
//        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
//
//        // Add image file to POST request
//        String fieldName = "picture";
//        String fileName = uploadFile.getName();
//        writer.append("--" + boundary).append(LINE_FEED);
//        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
//        writer.append(LINE_FEED);
//        writer.flush();
//
//        FileInputStream inputStream = new FileInputStream(uploadFile);
//        byte[] buffer = new byte[4096];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//        outputStream.flush();
//        inputStream.close();
//
//        writer.append(LINE_FEED);
//        writer.flush();
//
//        // Send POST request and get response
//        List<String> response = new ArrayList<>();
//        writer.append(LINE_FEED).flush();
//        writer.append("--" + boundary + "--").append(LINE_FEED);
//        writer.close();
//
//        // Check server status message
//        int status = httpConn.getResponseCode();
//        if (status == HttpURLConnection.HTTP_OK) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    httpConn.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.add(line);
//            }
//            reader.close();
//            httpConn.disconnect();
//        } else {
//            throw new IOException("Server returned non-OK status: " + status);
//        }
//
//        // Read response
//        for (String line : response) {
//            System.out.println(line);
//        }


}
