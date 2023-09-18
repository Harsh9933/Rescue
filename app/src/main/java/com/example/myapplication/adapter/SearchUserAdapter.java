package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DataClass;
import com.example.myapplication.R;

import java.util.List;



public class SearchUserAdapter extends RecyclerView.Adapter<MySearchViewHolder>{
    private Context context;
    private List<DataClass> dataList;

    public SearchUserAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchuser_recycler,parent,false);

        return new MySearchViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MySearchViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getProfileImageURL()).into(holder.recSearchImg);
        holder.recSearchName.setText(dataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MySearchViewHolder extends RecyclerView.ViewHolder{

    ImageView recSearchImg;
    TextView recSearchName;
    CardView searchCard;
    public MySearchViewHolder(@NonNull View itemView) {
        super(itemView);

        recSearchImg = itemView.findViewById(R.id.recSearchImg);
        recSearchName = itemView.findViewById(R.id.recSearchName);
        searchCard = itemView.findViewById(R.id.searchCard);



    }
}


