package com.example.listapp2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class NameDialog extends AppCompatDialogFragment {
    private EditText newname;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("enter your name")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strname = newname.getText().toString();
                        if (strname.matches("")) {
                            @SuppressLint("RestrictedApi") Context context = getApplicationContext();
                            CharSequence text = "you must enter name";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        else {
                        listener.applyTexts(strname);
                    }}
                });

        newname = view.findViewById(R.id.edit_newname);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            listener = (ExampleDialogListener) context;

    }

    public interface ExampleDialogListener {
        void applyTexts(String username);
    }

}
