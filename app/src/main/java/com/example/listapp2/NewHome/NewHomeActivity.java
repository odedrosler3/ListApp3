package com.example.listapp2.NewHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.listapp2.HomeActivity;
import com.example.listapp2.MainActivity;
import com.example.listapp2.R;
import com.example.listapp2.data.Group;
import com.example.listapp2.data.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewHomeActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    UploadTask uploadTask;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String phone;
    String username;
    private RecyclerView recyclerViewmy;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager layoutManager1;


    private FirebaseAuth mAuth; //myAuth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        mAuth = FirebaseAuth.getInstance();

        phone = mAuth.getCurrentUser().getPhoneNumber();
        final TextView username = this.findViewById(R.id.groupname);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                username.setText("hey "+dataSnapshot.child("users").child(phone).child("name").getValue().toString()+"!");

                recyclerViewmy = (RecyclerView) findViewById(R.id.myRecycler);

                recyclerViewmy.setHasFixedSize(true);
                final List<Group> mylist = new ArrayList<Group>();
                layoutManager1 = new LinearLayoutManager(getApplicationContext());
                recyclerViewmy.setLayoutManager(layoutManager1);
                DataSnapshot Ref = dataSnapshot.child("groups");
                for (DataSnapshot data: Ref.getChildren()) {
                    String ifphone=phone.substring(1);
                    String ifadmin=data.child("admin").getValue(String.class).substring(1);
                    if(ifadmin.matches(ifphone)){
                    String name=data.child("name").getValue(String.class);
                    Long id=data.child("id").getValue(Long.class);
                    Group g = new Group(id,name,phone);
                   mylist.add(g);
                }}

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("groups").document(phone);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                DataSnapshot Ref = dataSnapshot.child("groups");
                                for (Object data : task.getResult().getData().values()) {
                                    String groupid = data.toString().substring(1,data.toString().length()-1);
                                    String name = Ref.child(groupid).child("name").getValue(String.class);
                                    Long id = Ref.child(groupid).child("id").getValue(Long.class);
                                    String admin = Ref.child(groupid).child("admin").getValue(String.class);
                                    Group gr = new Group(id, name, admin);
                                    mylist.add(gr);
                                }
                            }
                        }
                        mAdapter1 = new MyAdaptermylist(getApplicationContext(),mylist);
                        recyclerViewmy.setAdapter(mAdapter1);
                    }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        }; usersTable.addValueEventListener(postListener);

    }

        public void add(View v){
            Intent i = new Intent(getApplicationContext(), com.example.listapp2.group.newgroup.class);
            startActivity(i);finish();
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

    public void signOut(View v) {
        // [START auth_fui_signout]
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        // [END auth_fui_signout]
    }

    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        this.finishAffinity();
        System.exit(1);
    }
}
