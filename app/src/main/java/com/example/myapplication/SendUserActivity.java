package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class SendUserActivity extends AppCompatActivity {
    RecyclerView recyclerview;
    List<com.example.myapplication.CustomerModel> allUserlist = new ArrayList<>();
    com.example.myapplication.SendMoneyAdapter sendMoneyAdapter;
    String accountnumber, name, currentamount, transferamount, remainingamount;
    SearchView searchView;
    public static final String TAG = "Send Money";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_user);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            accountnumber = bundle.getString("accountnumber");
            name = bundle.getString("name");
            currentamount = bundle.getString("currentamount");
            transferamount = bundle.getString("transferamount");
            Log.d(TAG, "Account No:" + accountnumber + " name :" + name + " current Amount: " + currentamount + " Transfer amount " + transferamount);
            showData(accountnumber);

        }
    }

    private void showData(String accountnumber) {
        Cursor cursor = new DatabaseHelper(this).readselectuserdata(accountnumber);
        while (cursor.moveToNext()) {
            String balancefromdb = cursor.getString(2);
            Double balance = Double.parseDouble(balancefromdb);
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setGroupingUsed(true);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String price = nf.format(balance);
            com.example.myapplication.CustomerModel model = new com.example.myapplication.CustomerModel(cursor.getString(0), cursor.getString(5), cursor.getString(1), price);
            allUserlist.add(model);
        }
        sendMoneyAdapter=new com.example.myapplication.SendMoneyAdapter(allUserlist,SendUserActivity.this,name,currentamount,transferamount,accountnumber);
        recyclerview.setAdapter(sendMoneyAdapter);
    }
}