package com.example.store.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.Bill;

public class BillAdapter extends BaseAdapter {
    private Activity activity;
    private List<Bill> bills;
    DatabaseHandler db;
    private TextView tv_orderno;
    private TextView tv_date;
    private TextView tv_state;
    private Button btn_shoppingnow;

    public BillAdapter(Activity activity, List<Bill> bills) {
        this.activity = activity;
        this.bills = bills;
    }
    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.item_bill, null);
        Mapping(view);
        Bill bill = bills.get(position);
        tv_orderno.setText("Order No." + bill.getiID());
        tv_date.setText(bill.getsDate());
        // 0. Dang giao 1. Da nhan 2. Da huy
        switch (bill.getiState()){
            case 0:
                tv_state.setText("Delivering");
                break;
            case 1:
                tv_state.setText("Completed");
                break;
            case 2:
                tv_state.setText("Canceled");
                break;
        }

        return view;
    }

    public void Mapping(View view){
        tv_orderno = view.findViewById(R.id.tv_orderno);
        tv_date = view.findViewById(R.id.tv_dateorder);
        tv_state = view.findViewById(R.id.tv_state);
    }
}
