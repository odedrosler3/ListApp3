package com.example.listapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.listapp2.group.newgroup;
import com.example.listapp2.item.EditItem;
import com.example.listapp2.item.NewItemActivity;
import com.example.listapp2.item.ViewItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends Activity {

    DatabaseReference usersTable = FirebaseDatabase.getInstance().getReference(); //myDB
    private FirebaseAuth mAuth; //myAuth
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void movetonewitem(View v){
        Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void movetonewgroup(View v){
        Intent i = new Intent(getApplicationContext(), newgroup.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void edititem(View v){
        Intent i = new Intent(getApplicationContext(), EditItem.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void movetoViewitem(View v){
        Intent i = new Intent(getApplicationContext(), ViewItem.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void signOut(View v) {
        // [START auth_fui_signout]
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        // [END auth_fui_signout]
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}