package com.example.lab2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> itemList;
    private DatabaseReference mDatabase;

    public MyAdapter(List<String> itemList, DatabaseReference databaseReference) {
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

        CompoundButton.OnCheckedChangeListener switchCheckedChangeListener = (buttonView, isChecked) -> {
            String status = isChecked ? "on" : "off";
            mDatabase.child(itemName.toLowerCase()).setValue(status);
        };

        mDatabase.child(itemName.toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    holder.switchItem.setOnCheckedChangeListener(null);
                    holder.switchItem.setChecked("on".equalsIgnoreCase(value));
                    holder.switchItem.setOnCheckedChangeListener(switchCheckedChangeListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        Switch switchItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            switchItem = itemView.findViewById(R.id.switch_item);
        }
    }
}
