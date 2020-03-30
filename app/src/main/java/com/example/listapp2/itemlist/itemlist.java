package com.example.listapp2.itemlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.listapp2.NewHome.NewHomeActivity;
import com.example.listapp2.R;
import com.example.listapp2.data.Group;
import com.example.listapp2.data.Item;
import com.example.listapp2.item.NewItemActivity;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class itemlist extends AppCompatActivity {

    private RecyclerView recyclerViewmy;
    private TextView name;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView.LayoutManager layoutManager1;
    private String groupid;
    private String stringid;
    private Group group;
    private String userphone;
    private List<Item> itemlist;
    private String itemstring;
    private String liststring;
    private Item newitem;
    Button edit;
    private  Group oldgroup;



    public static <T> T convertStringToObj(String strObj, Class<T> classOfT) {
        //convert string json to object
        return new Gson().fromJson(strObj, (Type) classOfT);
    }

    public static List<Item> convertStringToList(String strListObj,List<Item> list) {
        Item[] arr = new Gson().fromJson(strListObj, Item[].class);
        return new ArrayList<Item>(Arrays.asList(arr));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                name = findViewById(R.id.groupname);
                userphone=mAuth.getCurrentUser().getPhoneNumber();
                groupid=getIntent().getStringExtra("idgroup");
                itemstring=getIntent().getStringExtra("itemstring");
                liststring=getIntent().getStringExtra("liststring");
                edit = findViewById(R.id.editbtnn);
                newitem = null;
                if(itemstring!=null) {
                    newitem = convertStringToObj(itemstring,Item.class);
                }

                recyclerViewmy = (RecyclerView) findViewById(R.id.myRecycler);

                recyclerViewmy.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerViewmy.setLayoutManager(layoutManager);
                final List<Item> itemlistnull  = new ArrayList<>();

                mAdapter = new MyAdapteritem(groupid,getApplicationContext(),itemlistnull);
                recyclerViewmy.setAdapter(mAdapter);

                if(groupid!=null){
                oldgroup = new Group(dataSnapshot.child("groups").child(groupid).getValue(Group.class));
                // Get Post object and use the values to update the UI
                String admin = dataSnapshot.child("groups").child(groupid).child("admin").getValue(String.class);
                if(mAuth.getCurrentUser().getPhoneNumber().substring(1).matches(admin.substring(1))){
                    edit.setVisibility(View.VISIBLE);
                }
                group = dataSnapshot.child("groups").child(groupid).getValue(Group.class);
                name.setText(group.getname());}
                itemlist = new ArrayList<Item>();
                if (group!=null)
                {
                    if(group.getItems()!=null)
                        itemlist = group.getItems();
                }
                if(newitem!=null)
                {
                    if (liststring!=null) {
                        itemlist = convertStringToList(liststring,itemlist);
                    }
                        itemlist.add(newitem);

                }
                if (group!=null){
                if(itemlist!=null){
                    usersTable.child("groups").child(groupid).child("items").setValue(null);
                }}
                //
                layoutManager1 = new LinearLayoutManager(getApplicationContext());

                mAdapter = new MyAdapteritem(groupid,getApplicationContext(),itemlist);
                recyclerViewmy.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; usersTable.addListenerForSingleValueEvent(postListener);

    }
    public static <T> String convertListObjToString(List<T> listObj) {
        //convert object list to string json for
        return new Gson().toJson(listObj, new TypeToken<List<T>>() {
        }.getType());
    }

    public void add (View v){
        Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        i.putExtra("liststring",convertListObjToString(itemlist));
        i.putExtra("idgroup",groupid);
        startActivity(i);
    }
    public void edit(View v){
        Intent i = new Intent(getApplicationContext(), com.example.listapp2.group.newgroup.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        i.putExtra("idgroup",groupid);
        startActivity(i);
    }


    public void save(View v) throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_itemlist);
        usersTable.child("groups").child(groupid).child("items").setValue(itemlist);

        Intent i = new Intent(getApplicationContext(), NewHomeActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        startActivity(i);
        finish();
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
        if(oldgroup==null){
            finish();
        }
        else {
            Intent i = new Intent(getApplicationContext(), NewHomeActivity.class);
            usersTable.child("groups").child(groupid).setValue(oldgroup);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
