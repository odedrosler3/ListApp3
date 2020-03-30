package com.example.listapp2.item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listapp2.NewHome.NewHomeActivity;
import com.example.listapp2.R;
import com.example.listapp2.data.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

public class ViewItem extends AppCompatActivity {

    TextView desc;
    TextView name;
    ImageView img;
    Item item;
    String itemstring;
    String itemid;
    String groupid;
    Button edit;
    private FirebaseAuth mAuth; //myAuth



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
        setContentView(R.layout.activity_view_item);
        mAuth = FirebaseAuth.getInstance();
        itemid = getIntent().getStringExtra("iditem");
        itemstring = getIntent().getStringExtra("itemasstring");
        item = convertStringToObj(itemstring, Item.class);
        groupid = getIntent().getStringExtra("idgroup");


        name = findViewById(R.id.editname);
        desc = findViewById(R.id.editdescription);
        edit = findViewById(R.id.editbtn);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                String admin = dataSnapshot.child("groups").child(groupid).child("admin").getValue(String.class);
                if(mAuth.getCurrentUser().getPhoneNumber().substring(1).matches(admin.substring(1))){
                    edit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usersTable.addListenerForSingleValueEvent(postListener);


        name.setText(item.getName());
        desc.setText(item.getDesc());
        if (item.getImagelink() != null) {
            String imgid = item.getImagelink();
            StorageReference imageRef = storageRef.child("/images/" + imgid);
            String ext = imgid.substring(imgid.lastIndexOf("."));
            final long ONE_MEGABYTE = 1024 * 1024;
            try {
                File localFile = File.createTempFile("images", ext);
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        img.setImageBitmap(myBitmap);
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
        public void edit(View v){
            Intent i = new Intent(getApplicationContext(), com.example.listapp2.item.NewItemActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            i.putExtra("idgroup",groupid);
            i.putExtra("iditem","item"+item.getId());
            i.putExtra("itemasstring",itemstring);
            startActivity(i);


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
        Intent i = new Intent(getApplicationContext(), com.example.listapp2.itemlist.itemlist.class);

        i.putExtra("idgroup",groupid);
        i.putExtra("itemstring",itemstring);
        startActivity(i); finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}
