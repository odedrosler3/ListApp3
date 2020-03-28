package com.example.listapp2.item;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.listapp2.R;
import com.example.listapp2.data.Item;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class EditItem extends AppCompatActivity {
    EditText desc;
    EditText name;
    ImageView img1;
    Button delbtn;
    String imagelink ="";
    String imagepath="";
    String itemid = "item43";
    Item item;



    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    UploadTask uploadTask;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        name =  findViewById(R.id.editname);
        desc = findViewById(R.id.editdescription);
        img1 = findViewById(R.id.imageView);
        delbtn = findViewById(R.id.delbtn);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            item = dataSnapshot.child("users").child("+972545838529").child("groups").child("items").child(itemid).getValue(Item.class);
            name.setText(item.getName());
            desc.setText(item.getDesc());
            if (item.getImagelimk()!=null)
            {
                delbtn.setVisibility(View.VISIBLE);
                img1.setVisibility(View.VISIBLE);
                String imgid=item.getImagelimk();
                StorageReference imageRef = storageRef.child("/images/"+imgid);
                String ext = imgid.substring(imgid.lastIndexOf("."));
                final long ONE_MEGABYTE = 1024 * 1024;
                try {
                    File localFile = File.createTempFile("images", ext);
                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            img1.setImageBitmap(myBitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; usersTable.addListenerForSingleValueEvent(postListener);



}
    public void delimage(View v){
        delbtn.setVisibility(View.INVISIBLE);
        img1.setVisibility(View.INVISIBLE);
        imagepath="";
        item.setImagelimk(null);


    }

    public void addimage(View v){
        ImagePicker.Companion.with(this)
                .cropSquare()	//Crop image with Square aspect ratio
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();


    }

    File img;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            delbtn.setVisibility(View.VISIBLE);
            img1.setVisibility(View.VISIBLE);
            img1.setImageBitmap(null);
            imagepath = data.getData().getPath();
            img = new File(imagepath);
            Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);

            myImage.setImageBitmap(myBitmap);

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

    String imgID;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void upload(final Item item) throws FileNotFoundException {
        if(isReadStoragePermissionGranted()==true&&isWriteStoragePermissionGranted()==true){
            final DocumentReference docRef = db.collection("help").document("help");
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        Uri file = Uri.fromFile(img);
                        String ext = file.getPath().substring(file.getPath().lastIndexOf("."));
                        StorageReference imagesRef = storage.getReference().child("/images");
                        imgID = ("IMG"+task.getResult().getLong("imageid")+ext);
                        StorageReference thisimg = imagesRef.child(imgID);
                        uploadTask = thisimg.putFile(file);

                        docRef.update("imageid",task.getResult().getLong("imageid")+1);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                item.setImagelimk(imgID);
                                usersTable.child("users").child("+972545838529").child("groups").child("items").child(itemid).setValue(item);

                            }
                        });}

                }});
        }


// Register observers to listen for when the download is done or if it fails

    }

    String descstr;
    String namestr;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void save(View v) throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_newitem);
        if(imagepath.matches("")) {
            namestr = name.getText().toString();
            descstr = desc.getText().toString();

                        item.setName(namestr);
                        item.setDesc(descstr);
            usersTable.child("users").child("+972545838529").child("groups").child("items").child(itemid).setValue(item);


        }
        else
        {
            namestr = name.getText().toString();
            descstr = desc.getText().toString();

                        item.setName(namestr);
                        item.setDesc(descstr);

                        try {
                            upload(item);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

        }





    }

}
