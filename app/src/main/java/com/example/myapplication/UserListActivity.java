package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private static final String TAG = "user_list";
    List<com.example.myapplication.CustomerModel> modelList_showlist = new ArrayList<>();
    RecyclerView mRecyclerView;
    String phonenumber;
    CustomerAdapter customerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().setTitle("All Customers");
        mRecyclerView=findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        showData();
    }

    private void showData() {
        Log.d(TAG, "showData: called");
        modelList_showlist.clear();
        Log.d(TAG, "showData: modellist cleared");
        Cursor cursor = new DatabaseHelper(this).readalldata();
        while(cursor.moveToNext()){

            Log.d(TAG, "showData: inside cursor");
            String balancefromdb = cursor.getString(2);
            Double balance = Double.parseDouble(balancefromdb);

            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setGroupingUsed(true);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            String price = nf.format(balance);

            com.example.myapplication.CustomerModel model = new com.example.myapplication.CustomerModel(cursor.getString(0), cursor.getString(5),cursor.getString(1), price);
            modelList_showlist.add(model);
        }
        customerAdapter=new CustomerAdapter(modelList_showlist,UserListActivity.this);
        mRecyclerView.setAdapter(customerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            Intent transactionintent=new Intent(UserListActivity.this, TransactionHistoryActivity.class);
            startActivity(transactionintent);
        }
        return super.onOptionsItemSelected(item);
    }
}