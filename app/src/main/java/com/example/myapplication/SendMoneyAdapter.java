package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SendMoneyAdapter extends RecyclerView.Adapter<SendMoneyAdapter.ViewHolder> {
    List<CustomerModel> modelList;
    Context context;
    //Passing intent varibles
    String name, currentamount, transferamount, remainingamount,senderaccountnumber;
    //Selected custom variables
    String selectuser_accountnumber, selectuser_name, selectuser_balance, date;
    Intent intent;
    public void setFilterdList(List<CustomerModel> filteredList){
        this.modelList=filteredList;
        notifyDataSetChanged();
    }

    public SendMoneyAdapter(List<CustomerModel> modelList, Context context,String name,String currentamount,String transferamount,String senderaccountnumber) {
        this.modelList = modelList;
        this.context = context;
        this.name=name;
        this.currentamount=currentamount;
        this.transferamount=transferamount;
        this.senderaccountnumber=senderaccountnumber;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.userslist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(modelList.get(position).getName());
        holder.useraccountnumber.setText(modelList.get(position).getAccountno());
        holder.balance.setText(modelList.get(position).getBalance());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a");

                date = simpleDateFormat.format(calendar.getTime());
                selectuser_accountnumber=modelList.get(position).getAccountno();
                Cursor cursor = new DatabaseHelper(context).readparticulardata(selectuser_accountnumber);
                while (cursor.moveToNext()){
                    selectuser_name=cursor.getString(1);
                    selectuser_balance=cursor.getString(2);
                    Double dcurrentbalance=Double.parseDouble(selectuser_balance);
                    Double dtransferbalance=Double.parseDouble(transferamount);
                    Double dtotalbalance=dcurrentbalance+dtransferbalance;
                    new DatabaseHelper(context).insertTransferData(date,name,selectuser_name,transferamount,"SUCCESS");
                    Log.d("DatabaseHelper","Called");
                    new DatabaseHelper(context).updateAmount(selectuser_accountnumber,dtotalbalance.toString());
                    calculateAmount();
                    intent=new Intent(context,UserListActivity.class);
                    showDialog();
                }
            }
        });

    }

    private void calculateAmount() {
        Log.d("DatabaseHelper","Called");

        Double Dcurrentamount = Double.parseDouble(currentamount);
        Double Dtransferamount = Double.parseDouble(transferamount);
        Double Dremainingamount = Dcurrentamount - Dtransferamount;
        remainingamount = Dremainingamount.toString();
        new DatabaseHelper(context).updateAmount(senderaccountnumber,remainingamount);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setCancelable(false);//You can't cancel the dialog by clicking in the black space around the dialog
        Button btnOkay = dialog.findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                context.startActivity(intent);
                ((SendUserActivity)context).finish();
            }
        });
        dialog.show();
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
