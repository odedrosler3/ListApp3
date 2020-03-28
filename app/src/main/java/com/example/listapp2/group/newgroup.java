package com.example.listapp2.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listapp2.MainActivity;
import com.example.listapp2.R;
import com.example.listapp2.data.Contact;
import com.example.listapp2.data.Group;
import com.example.listapp2.data.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class newgroup extends AppCompatActivity {

    int CONTACT_PICKER_REQUEST =15;
    int PERMISSIONS_REQUEST_READ_CONTACTS =10;

    private RecyclerView recyclerView;
    private EditText name;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference usersTable= FirebaseDatabase.getInstance().getReference(); //myDB
    FirebaseFirestore db = FirebaseFirestore.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgroup);
        
        name = findViewById(R.id.editnameg);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<Contact> contactlistnull  = new ArrayList<>();

        // specify an adapter (see also next example)
        mAdapter = new MyAdaptergroup(contactlistnull);
        recyclerView.setAdapter(mAdapter);


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
    List<Contact> contactlist;
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
                    String phone = "+972"+ results.get(i).getPhoneNumbers().get(0).getNumber().substring(1);

                    Contact c = new Contact(name, phone);
                    contactlist.add(c);
                }
                mAdapter = new MyAdaptergroup(contactlist);
                recyclerView.setAdapter(mAdapter);}
                else{
                    List<ContactResult> results = MultiContactPicker.obtainResult(data);
                    for(int i=0; i<results.size(); i++)
                    {
                        String name = results.get(i).getDisplayName();
                        String phone = results.get(i).getPhoneNumbers().get(0).getNumber();
                        Contact c = new Contact(name, phone);
                        contactlist.add(c);
                    }
                    mAdapter = new MyAdaptergroup(contactlist);
                    recyclerView.setAdapter(mAdapter);
                }

            } else if(resultCode == RESULT_CANCELED){
            }
        }
    }
    String namestr;
    public void save(View v) throws ExecutionException, InterruptedException {
        setContentView(R.layout.activity_newgroup);


        namestr =name.getText().toString();

        Toast.makeText(this, namestr, Toast.LENGTH_SHORT).show();


        final DocumentReference docRef = db.collection("help").document("help");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Long id = task.getResult().getLong("groupid");
                        Group g = new Group(id,namestr,contactlist);
                        usersTable.child("users").child("+972545838529").child("groups").child("group"+id).setValue(g);
                        docRef.update("groupid", id + 1);
                    }

                }});

}}

