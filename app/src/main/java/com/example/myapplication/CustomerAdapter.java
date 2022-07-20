package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    List<CustomerModel> modelList;
    Context context;
    String accountnumber;

    public CustomerAdapter(List<CustomerModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.username.setText(modelList.get(position).getName());
        holder.useraccountnumber.setText(modelList.get(position).getAccountno());
        holder.balance.setText(modelList.get(position).getBalance());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountnumber=modelList.get(position).getAccountno();
                Intent intent=new Intent(context,UserDataActivity.class);
                intent.putExtra("accountnumber",accountnumber);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,useraccountnumber,balance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            useraccountnumber=itemView.findViewById(R.id.useraccountnumber);
            balance=itemView.findViewById(R.id.balance);
        }
    }
}
