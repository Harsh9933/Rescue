package com.example.myapplication.adapter;

import static com.example.myapplication.ChatActivity.recImg;
import static com.example.myapplication.ChatActivity.senderImg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.msgModelClass;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<msgModelClass> msgModelClassArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;


    public messageAdapter(Context context, ArrayList<msgModelClass> msgModelClassArrayList) {
        this.context = context;
        this.msgModelClassArrayList = msgModelClassArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sen_layout, parent, false);
            return new senderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.rec_layout, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            msgModelClass msgs = msgModelClassArrayList.get(position);
        if (holder.getClass()==senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(msgs.getMessage());
            Glide.with(context).load(senderImg).into(viewHolder.circleImageView);

        }else { reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(msgs.getMessage());
            Glide.with(context).load(recImg).into(viewHolder.circleImageView);


        }
    }

    @Override
    public int getItemCount() {
        return msgModelClassArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelClass msgs = msgModelClassArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msgs.getSenderId())){
            return ITEM_SEND;
        }else{
            return ITEM_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);

        }
    }

}
