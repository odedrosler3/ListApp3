package com.example.listapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.listapp2.data.Contact;
import com.example.listapp2.data.Group;
import com.example.listapp2.data.Item;
import com.example.listapp2.data.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



   //// FirebaseDatabase mydatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data();

    }


    public void data ()
    {

        FirebaseDatabase database;
        DatabaseReference usersTable;
        database = FirebaseDatabase.getInstance();
        usersTable = database.getReference();


        List<Item> items1 = new ArrayList<Item>();
        items1.add(new Item(1, "mj","mk"));
        items1.add(new Item(2, "nj","nk"));
        items1.add(new Item(3, "bj","bk"));

        List <Contact> contacts1 = new ArrayList<Contact>();
        contacts1.add(new Contact("nick1","098765432"));
        contacts1.add(new Contact("nick2","787655"));
        contacts1.add(new Contact("nick3","3489897"));

        List <Group> groups = new ArrayList<Group>();

        groups.add(new Group(0,"group1",contacts1,items1));


        List<Item> items2 = new ArrayList<Item>();
        items2.add(new Item(1, "mjdjjd","mddccdk"));
        items2.add(new Item(2, "nddj","nkdc"));
        items2.add(new Item(3, "bjdd","bkdc"));

        List <Contact> contacts2 = new ArrayList<Contact>();
        contacts2.add(new Contact("ndenjdnick1","098900765432"));
        contacts2.add(new Contact("nidndck2","787650905"));
        contacts2.add(new Contact("nim dcmdck3","34898777797"));

        groups.add(new Group(0,"group2",contacts2,items2));
        User user1= new User("3456789","name1", groups);

        usersTable.child("users").child(user1.getPhonenumer()).setValue(user1);

    }
}

