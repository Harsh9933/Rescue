package com.example.myapplication.adapter;


import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ChatActivity;
import com.example.myapplication.DataClass;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<DataClass> dataList;
    static int PERMISSION_CODE = 100;


    public void setFilterdList(List<DataClass> filterdList) {
        this.dataList = filterdList;
        notifyDataSetChanged();

    }

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = holder.getAdapterPosition();
        Glide.with(context).load(dataList.get(position).getProfileImageURL()).into(holder.recImage);
        holder.recName.setText(dataList.get(position).getName());
        holder.recExpert.setText(dataList.get(position).getExpertise());
        holder.recAdd.setText(dataList.get(position).getAddress());

        String name = dataList.get(position).getName();

        holder.recAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String uid = dataList.get(position).getUid();
                    sendAlertToUser(uid);
                }
            }
        });


        holder.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    String phoneNumber = dataList.get(pos).getPhoneNum();

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {

                        startPhoneCall(phoneNumber);
                    } else {

                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
                    }
                }
            }
        });


        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                int pos = holder.getAdapterPosition();
                intent.putExtra("name", dataList.get(pos).getName());
                intent.putExtra("profile", dataList.get(pos).getProfileImageURL());
                intent.putExtra("uid", dataList.get(pos).getUid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                String uid = dataList.get(pos).getUid();
                Log.d("Adapter", "UID: " + uid);

            }
        });


    }

    private void sendAlertToUser(String uid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "2",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Change to your desired icon
                .setContentTitle("Alert")
                .setContentText("You've received an alert!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = new Random().nextInt(1000);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }


    private void startPhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    ImageButton sendMessage , phoneButton;
    AppCompatButton recAlert;
    TextView recName , recExpert , recAdd;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recName = itemView.findViewById(R.id.recTitle);
        recExpert = itemView.findViewById(R.id.recExpert);
        recAdd = itemView.findViewById(R.id.recAddress);
        recCard = itemView.findViewById(R.id.recCard);
        sendMessage = itemView.findViewById(R.id.sendMessage);
        phoneButton = itemView.findViewById(R.id.phonecall);
        recAlert = itemView.findViewById(R.id.alert);


    }

}
