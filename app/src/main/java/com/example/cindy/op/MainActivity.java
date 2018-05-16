package com.example.cindy.op;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button iouButton;
    Button uomButton;

    String mCurrentPhotoPath;

    ImageView testImage;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //function that invokes an intent to capture a photo
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Create the File where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            if (photoFile!=null){
                Log.i("photoFile","true");
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                Log.i("photoUri", photoUri.toString());
                Log.i("mCurrentPhotoPath", mCurrentPhotoPath);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri.toString());
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity mythis = this;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        iouButton = (Button) findViewById(R.id.iouButton);
        uomButton = (Button) findViewById(R.id.uomButton);

        //testImage = (ImageView)findViewById(R.id.testImage);

        iouButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Context context = view.getContext();

                //Intent intent = new Intent(context, NextActivity.class);
                //startActivity(intent);
            }
        });

        uomButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Context context = view.getContext();
                //Intent intent = new Intent(context, CameraActivity.class);
                //startActivity(intent);
                Context context = view.getContext();
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(mythis , new String[]{Manifest.permission.CAMERA}, MainActivity.REQUEST_IMAGE_CAPTURE);
                }
                else{
                    dispatchTakePictureIntent();
                }

            }
        });

    }

    public static final String IMAGEKEY = "myImageKey";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            UploadImageAsync uploadImageAsync = new UploadImageAsync(this);
            uploadImageAsync.execute(Uri.parse(mCurrentPhotoPath));

//            try{
//
//
//                ContentResolver cr = this.getContentResolver();
//                Bitmap bitmap;
//                try
//                {
//                    bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, Uri.parse(mCurrentPhotoPath));
//                    testImage.setImageBitmap(bitmap);
//                }
//                catch (Exception e)
//                {
//                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
//                    Log.d("nothing", "Failed to load", e);
//                }
//
//            }
//            catch (Exception ex){
//                ex.printStackTrace();
//            }

        }
    }

    //when user responds to dialog box when requesting permission, the system invokes the app's onRequestPermissionResult,
    //passing it the user response
    //note that the callback is passed the same request code you passed to requestPermissions()
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case REQUEST_IMAGE_CAPTURE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        Log.i("directoryForImage", Environment.DIRECTORY_PICTURES);
        Log.i("fulldirectoryforimage", getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //creates the path for the actual file
        mCurrentPhotoPath = "file:"+image.getAbsolutePath();
        //creates the actual file
        return image;
    }


    class UploadImageAsync extends AsyncTask<Uri, Void, Void>{
        Context context;
        String downloadURL;

        public UploadImageAsync(Context mContext){
            this.context = mContext;
        }

        @Override
        protected Void doInBackground(Uri... uris) {

            Uri filePath = uris[0];

            StorageReference ref = storageReference.child("current");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                        }
                    });
        return null;
        }

        @Override
        protected void onPostExecute(Void v){
            //get the new url for the image

            //String downloadURL;
            storageReference.child("current").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloadURL = uri.toString();
                    Log.i("JW", "uri is = " + uri);
                    Intent intent = new Intent(context, CameraActivity.class);
                    intent.putExtra(IMAGEKEY, downloadURL);
                    Log.i("downloadURL", downloadURL);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.i("JW", "uri is = error");

                }
            });


        }
    }


}
