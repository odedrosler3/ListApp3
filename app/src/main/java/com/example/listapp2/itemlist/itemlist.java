package com.example.listapp2.itemlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class itemlist extends AppCompatActivity {

    private RecyclerView recyclerViewmy;
    private TextView name;
    private TextView contactstxt;
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
    private HashMap<String, Item> itemlist;
    private String itemstring;
    private String liststring;
    private HashMap<String, Item> olditemlist;
    private Item newitem;
    Button edit;
    private  Group oldgroup;
    String textcon = new String("me, ");
    boolean checkedcontact = false;


    public static <T> T convertStringToObj(String strObj, Class<T> classOfT) {
        //convert string json to object
        return new Gson().fromJson(strObj, (Type) classOfT);
    }
    public static String hashmaptostring(HashMap<String, Item> hash) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(hash);
        return json;

    }
    public  static HashMap<String, Item> stringtohashmap(String str, HashMap<String, Item> hashh) throws IOException {

        HashMap<String,  Item> resultMap = new Gson().fromJson(str, new TypeToken<HashMap<String,Item>>(){}.getType());
        return resultMap;

    }

    public static <T> List<T> convertStringToList(String strListObj) {
        //convert string json to object List
        return new Gson().fromJson(strListObj, new TypeToken<List<Object>>() {
        }.getType());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                name = findViewById(R.id.groupname);
                contactstxt = findViewById(R.id.textcontacts);
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
                final HashMap<String, Item> itemlistnull = new HashMap<String, Item>();

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
                if(group.getContacts()!=null&&checkedcontact==false)
                {
                    if(!group.getAdmin().equals(userphone))
                    textcon =textcon+dataSnapshot.child("users").child(group.getAdmin()).child("name").getValue()+", ";
                    for(int i=0; i<group.getContacts().size();i++){
                        if(!group.getContacts().get(i).getPhonenumber().equals(userphone)){
                        textcon =textcon+group.getContacts().get(i).getNickname()+", " ;}}
                contactstxt.setText(textcon.substring(0,textcon.lastIndexOf(",")));
                    checkedcontact = true;
                }
                itemlist = new HashMap<String, Item>();
                olditemlist = new HashMap<String, Item>();

                if(newitem!=null)
                {
                    if (liststring!=null) {
                        try {

                            itemlist = stringtohashmap(liststring,itemlist);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                        itemlist.put("item"+newitem.getId(),newitem);

                }else{
                if (group!=null)
                {
                    if(group.getItems()!=null)
                        itemlist = group.getItems();
                }}
                olditemlist=itemlist;
                layoutManager1 = new LinearLayoutManager(getApplicationContext());

                mAdapter = new MyAdapteritem(groupid,getApplicationContext(),itemlist);
                recyclerViewmy.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; usersTable.addValueEventListener(postListener);

    }

    public void add (View v) throws JsonProcessingException {
        Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        i.putExtra("liststring",hashmaptostring(itemlist));
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
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            usersTable.child("groups").child(groupid).child("items").setValue(olditemlist);

        }

    }
}
