package com.example.listapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listapp2.data.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ViewItem extends AppCompatActivity {

    TextView desc;
    TextView name;
    ImageView img;
    Item item;
    String itemid = "item41";


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    UploadTask uploadTask;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        name = findViewById(R.id.editname);
        desc =  findViewById(R.id.editdescription);



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                item = dataSnapshot.child("users").child("+972545838529").child("groups").child("items").child(itemid).getValue(Item.class);
                name.setText(item.getName());
                desc.setText(item.getDesc());
                if (item.getImagelimk()!=null)
                {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
            usersTable.addListenerForSingleValueEvent(postListener);





    }
}
