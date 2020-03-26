package com.example.listapp2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.listapp2.data.Item;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class NewItemActivity extends Activity {

    EditText desc;
    EditText name;
    String imagelink ="";
    String imagepath="";
    static int countfile=1;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference imagesRef = storage.getReference().child("images/");
    UploadTask uploadTask;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newitem);
         name = (EditText) findViewById(R.id.editname);
         desc = (EditText) findViewById(R.id.editdescription);
    }


    public void addimage(View v){
        ImagePicker.Companion.with(this)
                .cropSquare()	//Crop image with Square aspect ratio
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                imagepath=data.getData().getPath();
                try {
                    upload();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.Companion.getError(getIntent()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23)
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                boolean b = false;
                return b;
            }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public void upload() throws FileNotFoundException {
        if(isReadStoragePermissionGranted()==true&&isWriteStoragePermissionGranted()==true){
            Uri file = Uri.fromFile(new File(imagepath));
            imagesRef.child("1");
            uploadTask = imagesRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
    }   }

    public void save(View v){
        setContentView(R.layout.activity_newitem);
        if(imagelink.matches("")) {
            String namestr = name.getText().toString();
            String descstr = desc.getText().toString();
            String itemid=("item"+Item.countitem);
            Item item = new Item(Item.countitem,namestr,descstr);
            usersTable.child("users").child("+972545838529").child("groups").child("items").child(itemid).setValue(item);
            Item.countitem++;
        }
        else
        {
            String namestr = name.getText().toString();
            String descstr = desc.getText().toString();
            String itemid=("item"+Item.countitem);

            Item item = new Item(Item.countitem,namestr, descstr,imagelink);
            usersTable.child("users").child("+972545838529").child("groups").child("items").child(itemid).setValue(item);
            Item.countitem++;
        }
    }
}
