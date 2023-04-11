package com.example.store.activity.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import com.example.store.adapter.BillAdapter;
import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.Bill;

public class BillActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView lv_bill;
    private List<Bill> lBill;
    private DatabaseHandler db;
    private String idUser;
    private int REQUEST_CODE_BILLDETAIL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        idUser = getIntent().getStringExtra("iduser");
        db = new DatabaseHandler(this);
        Mapping();
        lBill = db.getListBillOfUser(Integer.parseInt(idUser));
        if(lBill.size() != 0){
            BillAdapter adapter = new BillAdapter(this,lBill);
            lv_bill.setAdapter(adapter);
        }
        else setContentView(R.layout.item_empty_bill);

        toolbar = findViewById(R.id.toolbar_appbar);
        lv_bill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bill bill = lBill.get(position);
                Intent intentBill = new Intent(getBaseContext(), BillDetailActivity.class);
                intentBill.putExtra("idbill",bill.getiID());
                startActivityForResult(intentBill, REQUEST_CODE_BILLDETAIL);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void Mapping(){
        lv_bill = findViewById(R.id.lv_bill);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_BILLDETAIL && resultCode == Activity.RESULT_OK) {
            lBill = db.getListBillOfUser(Integer.parseInt(idUser));
            BillAdapter adapter = new BillAdapter(this,lBill);
            lv_bill.setAdapter(adapter);
        }
    }
}