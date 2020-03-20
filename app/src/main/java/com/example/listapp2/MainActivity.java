package com.example.listapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.listapp2.data.Contact;
import com.example.listapp2.data.Group;
import com.example.listapp2.data.Item;
import com.example.listapp2.data.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseReference usersTable=FirebaseDatabase.getInstance().getReference(); //myDB
    private FirebaseAuth mAuth; //myAuth
    private static final int RC_SIGN_IN = 123;
    String username="bla";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
        } else {
            sign();
        }

    }

public void sign(){
    mAuth = FirebaseAuth.getInstance();
    mAuth.setLanguageCode("il");
    FirebaseUser user = mAuth.getCurrentUser();
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build());

    startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
            RC_SIGN_IN);

    AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                }
            });

}

    public void dbusercheck()
    {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(dataSnapshot.child("users").hasChild(user.getPhoneNumber()))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "yes";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
                else
                {
                    openDialog();
                    User newuser = new User(user.getPhoneNumber(),username);
                    usersTable.child("users").child(user.getPhoneNumber()).setValue(newuser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
       usersTable.addListenerForSingleValueEvent(userListener);


    }

    public void openDialog() {
        NameDialog named = new NameDialog();
        named.show(getSupportFragmentManager(), "example dialog");
    }




    public void applyTexts(String username) {
        username=this.username;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                dbusercheck();

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    public void signOut(View v) {
        // [START auth_fui_signout]
        FirebaseAuth.getInstance().signOut();
        sign();

        // [END auth_fui_signout]


    }
}

