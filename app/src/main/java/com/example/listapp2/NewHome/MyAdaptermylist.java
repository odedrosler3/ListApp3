package com.example.listapp2.NewHome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listapp2.R;
import com.example.listapp2.data.Group;
import com.example.listapp2.itemlist.itemlist;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MyAdaptermylist extends RecyclerView.Adapter<MyAdaptermylist.MyViewHolder> {
    private List<Group> groupslist;
    private Context con;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String phone;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public TextView listTextView;
        // public Button delbtn;
        public MyViewHolder(View v) {
            super(v);

            listTextView = (TextView) itemView.findViewById(R.id.list_name);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdaptermylist(Context con,List<Group> groupslist) {
        if(mAuth.getCurrentUser()!=null)
            phone = mAuth.getCurrentUser().getPhoneNumber();
        this.con = con;
        this.groupslist = groupslist;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdaptermylist.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recyclergroupconatct, parent, false);


        MyViewHolder vh = new MyViewHolder(contactView);
        return vh;
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Group g = groupslist.get(position);
        String phoneg = g.getAdmin();
        TextView name = holder.listTextView;
        name.setText(g.getname());

        if(phoneg==phone){
            name.setTextColor(Color.BLUE);
        }


        name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Group gd = groupslist.get(position);
                final Long thisid = gd.getid();
                Intent i = new Intent(con, itemlist.class);
                i.putExtra("idgroup","group"+thisid.toString());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(i);

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return groupslist.size();
    }
}