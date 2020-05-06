package com.example.listapp2.item;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.listapp2.R;
import com.example.listapp2.data.Item;
import com.example.listapp2.itemlist.itemlist;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class NewItemActivity extends Activity {

    EditText desc;
    EditText name;
    String imagepath="";
    ImageView img1;
    Button delbtn;
    String groupid;
    String liststring;
    String extiditem;
    String itemstring;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    UploadTask uploadTask;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static <T> T convertStringToObj(String strObj, Class<T> classOfT) {
        //convert string json to object
        return new Gson().fromJson(strObj, (Type) classOfT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newitem);
         name = (EditText) findViewById(R.id.editname);
         desc = (EditText) findViewById(R.id.editdescription);
        img1 = findViewById(R.id.imageView111);
        delbtn = findViewById(R.id.delbtn);
        groupid=getIntent().getStringExtra("idgroup");
        extiditem=getIntent().getStringExtra("iditem");
        liststring =getIntent().getStringExtra("liststring");
        itemstring = getIntent().getStringExtra("itemasstring");
        Item extitem  = convertStringToObj(itemstring, Item.class);
        if(extitem!=null){

                    name.setText(extitem.getName());
                    desc.setText(extitem.getDesc());
                    if(extitem.getImagelink()!=null){
                            String imgid = extitem.getImagelink();
                            StorageReference imageRef = storageRef.child("/images/" + imgid);
                            String ext = imgid.substring(imgid.lastIndexOf("."));
                            final long ONE_MEGABYTE = 1024 * 1024;
                            try {
                                File localFile = File.createTempFile("images", ext);
                                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        img1.setImageBitmap(myBitmap);
                                        delbtn.setVisibility(View.VISIBLE);
                                        img1.setVisibility(View.VISIBLE);
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


    }

    public void delimage(View v){
        delbtn.setVisibility(View.INVISIBLE);
        img1.setVisibility(View.INVISIBLE);
        imagepath="";
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
            if (resultCode == RESULT_OK) {
                imagepath=data.getData().getPath();
                img = new File(imagepath);
                Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());

                ImageView myImage = (ImageView) findViewById(R.id.imageView111);

                myImage.setImageBitmap(myBitmap);
                delbtn.setVisibility(View.VISIBLE);
                img1.setVisibility(View.VISIBLE);

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
                        imgID = ("IMG" + task.getResult().getLong("imageid") + ext);
                        StorageReference thisimg = imagesRef.child(imgID);
                        uploadTask = thisimg.putFile(file);

                        if (extiditem != null) {
                            item.setImagelink(imgID);
                            String itemasstring = convertObjToString(item);
                            Intent i = new Intent(getApplicationContext(), itemlist.class);
                            i.putExtra("itemstring", itemasstring);
                            i.putExtra("liststring", liststring);
                            i.putExtra("idgroup", groupid);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i); finish();


                        } else {
                            docRef.update("imageid", task.getResult().getLong("imageid") + 1);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    item.setImagelink(imgID);
                                    String itemasstring = convertObjToString(item);
                                    Intent i = new Intent(getApplicationContext(), itemlist.class);
                                    i.putExtra("itemstring", itemasstring);
                                    i.putExtra("liststring", liststring);
                                    i.putExtra("idgroup", groupid);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i); finish();
                                }
                            });
                        }
                    }
                }});
        }


// Register observers to listen for when the download is done or if it fails

    }

    String descstr;
    String namestr;

    public void save(View v) throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_newitem);
        if(imagepath.matches("")) {
            namestr = name.getText().toString();
            descstr = desc.getText().toString();

            final DocumentReference docRef = db.collection("help").document("help");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            if (extiditem != null) {
                                Item item = new Item(namestr, descstr, task.getResult().getLong("itemid"));
                                String itemasstring = convertObjToString(item);
                                Intent i = new Intent(getApplicationContext(), itemlist.class);
                                i.putExtra("itemstring", itemasstring);
                                i.putExtra("liststring", liststring);
                                i.putExtra("idgroup", groupid);
                                i.putExtra("itemid", "item"+item.getId());
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i); finish();


                            } else {

                            Item item = new Item(namestr, descstr, task.getResult().getLong("itemid"));
                            String itemasstring = convertObjToString(item);
                            Intent i = new Intent(getApplicationContext(), itemlist.class);
                            i.putExtra("itemstring",itemasstring);
                            i.putExtra("liststring",liststring);
                            i.putExtra("idgroup",groupid);
                            i.putExtra("itemid", "item"+item.getId());
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            docRef.update("itemid", item.getId()+1);
                                startActivity(i); finish();

                        }}

                    }});
            }
        else
        {
            namestr = name.getText().toString();
            descstr = desc.getText().toString();

            final DocumentReference docRef = db.collection("help").document("help");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Item item = new Item(namestr, descstr, task.getResult().getLong("itemid"));
                        docRef.update("itemid", item.getId()+1);
                        try {
                            upload(item);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }});
        }
    }

    public static String convertObjToString(Object clsObj) {
        //convert object  to string json
        String jsonSender = new Gson().toJson(clsObj, new TypeToken<Object>() {
        }.getType());
        return jsonSender;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {

        if(extiditem==null){
            Intent i = new Intent(getApplicationContext(), com.example.listapp2.itemlist.itemlist.class);
            i.putExtra("iditem",extiditem);
            i.putExtra("liststring",liststring);
            i.putExtra("idgroup",groupid);
            startActivity(i); finish();;
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else{
            finish();
        }

    }



}
