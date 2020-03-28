package com.example.listapp2.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listapp2.R;
import com.example.listapp2.data.Contact;
import com.wafflecopter.multicontactpicker.ContactResult;

import java.util.List;

public class MyAdaptergroup extends RecyclerView.Adapter<MyAdaptergroup.MyViewHolder> {
    private List<Contact> contactlist;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView;
        public Button delButton;
       // public Button delbtn;
        public MyViewHolder(View v) {
            super(v);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            delButton = (Button) itemView.findViewById(R.id.delbtn);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdaptergroup(List<Contact> contactlist) {
        this.contactlist = contactlist;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdaptergroup.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recycleritemconatct, parent, false);


        MyViewHolder vh = new MyViewHolder(contactView);
        return vh;
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Contact c = contactlist.get(position);
        TextView name = holder.nameTextView;
        Button del = holder.delButton;
        name.setText(c.getNickname());
        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                contactlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, contactlist.size());

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contactlist.size();
    }
}