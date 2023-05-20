package com.example.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> itemList;
    private DatabaseReference mDatabase;
    private Context context;

    public MyAdapter(Context context, List<String> itemList, DatabaseReference databaseReference) {
        this.context = context;
        this.itemList = itemList;
        this.mDatabase = databaseReference;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String itemName = itemList.get(position);
        holder.tvItemName.setText(itemName);

        // Load initial image
        String initialImageName = itemName.toLowerCase() + "_" + "closed";
        Glide.with(context)
                .load(getImage(context, initialImageName))
                .override(200, 200)
                .into(holder.image);

        CompoundButton.OnCheckedChangeListener switchCheckedChangeListener = (buttonView, isChecked) -> {
            String status;
            if (itemName.equalsIgnoreCase("light")) {
                status = isChecked ? "on" : "off";
            } else {
                status = isChecked ? "open" : "closed";
            }
            mDatabase.child(itemName.toLowerCase()).setValue(status);

            // Load image based on status
            String imageName = itemName.toLowerCase() + "_" + status;
            Glide.with(context)
                    .load(getImage(context, imageName))
                    .override(200, 200)
                    .into(holder.image);
        };

        mDatabase.child(itemName.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    holder.switchItem.setOnCheckedChangeListener(null);
                    holder.switchItem.setChecked(false); // Set to neutral state
                    holder.switchItem.setChecked("on".equalsIgnoreCase(value) || "open".equalsIgnoreCase(value)); // Then set the real state
                    holder.switchItem.setOnCheckedChangeListener(switchCheckedChangeListener);

                    // Load image based on the value from the database
                    String imageName = itemName.toLowerCase() + "_" + value.toLowerCase();
                    Glide.with(context)
                            .load(getImage(context, imageName))
                            .override(200, 200)
                            .into(holder.image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }


    @SuppressLint("DiscouragedApi")
    public static int getImage(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView image;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            image = itemView.findViewById(R.id.image);
            switchItem = itemView.findViewById(R.id.switch_item);
        }
    }
}
