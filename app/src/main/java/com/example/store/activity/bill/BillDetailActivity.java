package com.example.store.activity.bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.store.adapter.BillDetailAdapter;
import com.example.store.bean.Bill;
import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.BillDetail;

public class BillDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv_name, tv_phone, tv_address, tv_state, tv_totalprice, tv_totalquantity;
    private Button btn_cancel, btn_receive;
    private DatabaseHandler db;
    private int idBill;
    private List<BillDetail> lBillDetail;
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        db = new DatabaseHandler(this);
        idBill = getIntent().getIntExtra("idbill",0);
        Mapping();
        lBillDetail = db.getListBillDetail(idBill);
        bill = db.getBill(idBill);
        bill.setiID(idBill);
        Intent intent = new Intent();
        SetValue();

        RecyclerView rv_billdetail = (RecyclerView) findViewById(R.id.rv_confirm);
        DividerItemDecoration dividerHorizontal =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv_billdetail.addItemDecoration(dividerHorizontal);
        //rv_billdetail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_billdetail.setLayoutManager(layoutManager);
        BillDetailAdapter adapter = new BillDetailAdapter(lBillDetail, this);
        rv_billdetail.setAdapter(adapter);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_receive.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                bill.setiState(1);
                tv_state.setText("Completed");
                db.updateStateBill(bill);
                setResult(RESULT_OK, intent);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_receive.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                bill.setiState(2);
                tv_state.setText("Canceled");
                db.updateStateBill(bill);
                setResult(RESULT_OK, intent);
            }
        });
    }

    public void Mapping(){
        toolbar = findViewById(R.id.toolbar_appbar);
        tv_name = findViewById(R.id.tv_name_bill);
        tv_phone = findViewById(R.id.tv_phone_bill);
        tv_address = findViewById(R.id.tv_address_bill);
        tv_state = findViewById(R.id.tv_state_billdetail);
        tv_totalprice = findViewById(R.id.tv_totalprice_bill);
        tv_totalquantity = findViewById(R.id.tv_totalquantity_bill);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_receive = findViewById(R.id.btn_received);

    }

    public void SetValue(){
        toolbar.setTitle("Order No." + String.valueOf(idBill));
        tv_name.setText(bill.getsPersonName());
        tv_phone.setText(bill.getsPhone());
        tv_address.setText(bill.getsAddress());
        switch (bill.getiState()){
            case 0:
                tv_state.setText("Delivering");
                btn_cancel.setVisibility(View.VISIBLE);
                btn_receive.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_state.setText("Completed");
                btn_cancel.setVisibility(View.GONE);
                btn_receive.setVisibility(View.GONE);
                break;
            case 2:
                tv_state.setText("Canceled");
                btn_cancel.setVisibility(View.GONE);
                btn_receive.setVisibility(View.GONE);
                break;
        }
        tv_totalquantity.setText(String.valueOf(bill.getiQuantity()));
        tv_totalprice.setText(String.valueOf(bill.getlTotalPrice()));
    }
}