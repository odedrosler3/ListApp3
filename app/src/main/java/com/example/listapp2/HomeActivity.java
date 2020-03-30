package com.example.listapp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.listapp2.NewHome.NewHomeActivity;
import com.example.listapp2.group.newgroup;
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


    public void movetonewhome(View v){
        Intent i = new Intent(getApplicationContext(), NewHomeActivity.class);
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

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}