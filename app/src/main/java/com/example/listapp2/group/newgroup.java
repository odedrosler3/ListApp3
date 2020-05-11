package com.example.listapp2.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.listapp2.NewHome.NewHomeActivity;
import com.example.listapp2.R;
import com.example.listapp2.data.Contact;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class newgroup extends AppCompatActivity {

    int CONTACT_PICKER_REQUEST =15;
    int PERMISSIONS_REQUEST_READ_CONTACTS =10;

    private RecyclerView recyclerView;
    private EditText name;
    private String userphone;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth; //myAuth
    private String extgroupid;
    List<Contact> contactlist;
    HashMap<String, Item> olditemlist;
    Button savebtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgroup);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
        extgroupid=getIntent().getStringExtra("idgroup");
        name = findViewById(R.id.editnameg);
        mAuth = FirebaseAuth.getInstance();
        savebtn=findViewById(R.id.addbtn3);
        userphone = mAuth.getCurrentUser().getPhoneNumber();
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);



        if(extgroupid==null){

        final List<Contact> contactlistnull  = new ArrayList<>();

        mAdapter = new MyAdaptergroup(contactlistnull);
        recyclerView.setAdapter(mAdapter);}
        else{

                    // Get Post object and use the values to update the UI
                        Group g = dataSnapshot.child("groups").child(extgroupid).getValue(Group.class);
                        name.setText(g.getname());
                        olditemlist = g.getItems();
                        contactlist = g.getContacts();
                    mAdapter = new MyAdaptergroup(contactlist);
                    recyclerView.setAdapter(mAdapter);

                }}
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

                };
                usersTable.addListenerForSingleValueEvent(postListener);


        final EditText namee = findViewById(R.id.editnameg);
        namee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().length() > 0){
                    savebtn.setEnabled(true);
                } else {
                    savebtn.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0){
                    savebtn.setEnabled(true);
                } else {
                    savebtn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0){
                    savebtn.setEnabled(true);
                } else {
                    namee.setError("name required");
                    savebtn.setEnabled(false);
                }
            }
        });

    }




    public void add (View v){

        new MultiContactPicker.Builder(this) //Activity/fragment context
                .hideScrollbar(false) //Optional - default: false
                .showTrack(true) //Optional - default: true
                .searchIconColor(Color.WHITE) //Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)!!!!!!!!!!!!!!!!
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                if(contactlist==null){
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                contactlist  = new ArrayList<>();
                for(int i=0; i<results.size(); i++)
                {
                    String name = results.get(i).getDisplayName();
                    String phone = results.get(i).getPhoneNumbers().get(0).getNumber();
                    phone = phone.replace("-","");
                    char[] ch = phone.toCharArray();
                    if(ch[0]=='+'){
                        Contact c = new Contact(name, phone);
                        contactlist.add(c);}
                    else{
                        String newphone= "+972"+phone.substring(1);
                        Contact c = new Contact(name, newphone);
                        contactlist.add(c);
                    }
                }
                mAdapter = new MyAdaptergroup(contactlist);
                recyclerView.setAdapter(mAdapter);}
                else{
                    List<ContactResult> results = MultiContactPicker.obtainResult(data);
                    for(int i=0; i<results.size(); i++)
                    {
                        String name = results.get(i).getDisplayName();
                        String phone = results.get(i).getPhoneNumbers().get(0).getNumber();
                        phone = phone.replace("-","");
                        char[] ch = phone.toCharArray();
                        if(ch[0]=='+'){
                        Contact c = new Contact(name, phone);
                        contactlist.add(c);}
                        else{
                            String newphone= "+972"+phone.substring(1);
                            Contact c = new Contact(name, newphone);
                            contactlist.add(c);}
                        }
                    }
                    mAdapter = new MyAdaptergroup(contactlist);
                    recyclerView.setAdapter(mAdapter);
                }

            } else if(resultCode == RESULT_CANCELED){
            }

    }
    String namestr;
    Group g;
    Long id;
    String phone;
    public void save(View v) throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_newgroup);


        namestr =name.getText().toString();


        final DocumentReference docRef = db.collection("help").document("help");
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (extgroupid == null) {
                            id = task.getResult().getLong("groupid");
                            g = new Group(id, namestr, contactlist, userphone);
                            usersTable.child("groups").child("group" + id).setValue(g);
                            docRef.update("groupid", id + 1);
                        } else {
                            id = Long.parseLong(extgroupid.substring(5));
                            g = new Group(id, namestr, contactlist, olditemlist, userphone);
                            usersTable.child("groups").child("group" + id).setValue(g);
                        }

                        db.collection("groups").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (int i = 0; i < contactlist.size(); i++) {
                                        phone = contactlist.get(i).getPhonenumber();
                                            db.collection("groups").document(phone).update("groups", FieldValue.arrayUnion("group" + id));
                                         //   Map<String, Object> docData = new HashMap<>();
                                           // docData.put("groups", Arrays.asList("group" + id));
                                           // db.collection("groups").document(phone).set(docData, SetOptions.merge());

                                    }
                                    }
                                }

                               });


                        if (extgroupid == null) {
                            Intent in = new Intent(getApplicationContext(), com.example.listapp2.itemlist.itemlist.class);
                            in.putExtra("idgroup", "group" + id);
                            startActivity(in);
                            finish();
                        } else {
                            Intent in = new Intent(getApplicationContext(), com.example.listapp2.itemlist.itemlist.class);
                            in.putExtra("idgroup", extgroupid);
                            startActivity(in);
                            finish();
                        }


                    } }});




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

        if(extgroupid==null){
        Intent i = new Intent(getApplicationContext(), NewHomeActivity.class);

        startActivity(i); finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);}
        else{
            finish();
        }

    }

}

