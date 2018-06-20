package pl.niewiel.pracadyplomowa.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.orm.SugarRecord;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.niewiel.pracadyplomowa.R;
import pl.niewiel.pracadyplomowa.database.model.Component;
import pl.niewiel.pracadyplomowa.database.model.Photo;
import pl.niewiel.pracadyplomowa.database.model.PhotoToComponent;
import pl.niewiel.pracadyplomowa.database.service.Service;

public class CameraActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    String imageFileName;
    Button okButton;
    Service<Photo> service;
    private Uri mCurrentPhotoURI;
    private ImageView mImageView;
    private Component component;
    private Photo photo;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        setContentView(R.layout.activity_camera);
        okButton = findViewById(R.id.okButton);
        mImageView = findViewById(R.id.mImageView);
        dispatchTakePictureIntent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("result code", String.valueOf(requestCode));

        if (requestCode == 1 && resultCode == RESULT_OK) {

//            Log.e("PATH", String.valueOf(mCurrentPhotoURI.getPath()));

            mImageView.setImageURI(mCurrentPhotoURI);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photo = new Photo("aaa" + imageFileName, mCurrentPhotoURI);
                    photo.setmId(SugarRecord.save(photo));
                    SugarRecord.save(photo);
                    if (getIntent().hasExtra("component")) {
                        component = SugarRecord.findById(Component.class, getIntent().getExtras().getLong("component"));
                        SugarRecord.save(new PhotoToComponent(photo, component));
                        Log.e("OK", "OK");
                    } else
                        Log.e("OK", "NOK");
                    finish();
                }
            });

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        imageFileName = timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Save a file: path for use with ACTION_VIEW intents
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        ).getAbsoluteFile();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "pl.niewiel.pracadyplomowa.activity.CameraActivity",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                if (photoURI != null) {
                    mCurrentPhotoURI = photoURI.normalizeScheme();
                    Log.i("CAMERA", mCurrentPhotoURI.toString());
                }
            }

        }
    }
}
