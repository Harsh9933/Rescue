package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.ChatActivity;
import com.example.myapplication.DataClass;
import com.example.myapplication.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position=holder.getAdapterPosition();
        Glide.with(context).load(dataList.get(position).getProfileImageURL()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getName());
        holder.recExpert.setText(dataList.get(position).getExpertise());
        holder.recAdd.setText(dataList.get(position).getAddress());

        String name = dataList.get(position).getName();

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context , ChatActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//                int pos=holder.getAdapterPosition();
//                intent.putExtra("name",dataList.get(pos).getName());
//                intent.putExtra("profile",dataList.get(pos).getProfileImageURL());
//                intent.putExtra("uid",dataList.get(pos).getUid());
//                context.startActivity(intent);
                Intent intent = new Intent(context, ChatActivity.class);
                int pos=holder.getAdapterPosition();
                intent.putExtra("name",dataList.get(pos).getName());
                intent.putExtra("profile",dataList.get(pos).getProfileImageURL());
                intent.putExtra("uid",dataList.get(pos).getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                String uid = dataList.get(pos).getUid();
                Log.d("Adapter", "UID: " + uid);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    ImageButton sendMessage;
    TextView recName , recExpert , recAdd;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recName = itemView.findViewById(R.id.recAddress);
        recExpert = itemView.findViewById(R.id.recExpert);
        recAdd = itemView.findViewById(R.id.recTitle);
        recCard = itemView.findViewById(R.id.recCard);
        sendMessage = itemView.findViewById(R.id.sendMessage);


    }
}
