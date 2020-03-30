package com.example.listapp2.itemlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listapp2.R;
import com.example.listapp2.data.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyAdapteritem extends RecyclerView.Adapter<MyAdapteritem.MyViewHolder> {
    private List<Item> itemlist;
    private Context con;
    private String id;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView;
        public Button delButton;
        public ImageView img;
       // public Button delbtn;
        public MyViewHolder(View v) {
            super(v);
            nameTextView = (TextView) itemView.findViewById(R.id.list_name);
            delButton = (Button) itemView.findViewById(R.id.delbtn);
            img = itemView.findViewById(R.id.imageView2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapteritem(String groupid,Context con, List<Item> itemlist) {
        this.con = con;
        this.itemlist = itemlist;
        this.id=groupid;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapteritem.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recycleritemlist, parent, false);


        MyViewHolder vh = new MyViewHolder(contactView);
        return vh;
    }




    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Item c = itemlist.get(position);
        TextView name = holder.nameTextView;
        Button del = holder.delButton;
        final ImageView img = holder.img;
        if (c.getImagelink()!=null)
        {
            String imgid=c.getImagelink();
            StorageReference imageRef = storageRef.child("/images/"+imgid);
            String ext = imgid.substring(imgid.lastIndexOf("."));
            final long ONE_MEGABYTE = 1024 * 1024;
            try {
                File localFile = File.createTempFile("images", ext);
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        img.setImageBitmap(myBitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        name.setText(c.getName());

        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemlist.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }

        });

        name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Item item = itemlist.get(position);
                String  itemstring = convertObjToString(item);
                Intent i = new Intent(con, com.example.listapp2.item.ViewItem.class);
                i.putExtra("idgroup",id);
                i.putExtra("iditem",item.getId());
                i.putExtra("itemasstring",itemstring);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(i);
            }

    });

    }


    public static String convertObjToString(Object clsObj) {
        //convert object  to string json
        String jsonSender = new Gson().toJson(clsObj, new TypeToken<Object>() {
        }.getType());
        return jsonSender;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}