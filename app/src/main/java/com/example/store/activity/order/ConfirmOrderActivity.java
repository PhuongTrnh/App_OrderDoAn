package com.example.store.activity.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.example.store.adapter.ConfirmOrderAdapter;
import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.Bill;
import com.example.store.bean.BillDetail;
import com.example.store.bean.Cart;
import com.example.store.bean.Product;
import com.example.store.bean.User;

public class ConfirmOrderActivity extends AppCompatActivity {

    String idUser;
    EditText et_personname;
    EditText et_personphone;
    EditText et_personaddress;
    TextView tv_totalquantity;
    TextView tv_totalprice;
    Button btn_confirm;
    private User user;
    private List<Cart> lCarts;
    DatabaseHandler db;
    private double total_all_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        db = new DatabaseHandler(this);
        idUser = getIntent().getStringExtra("iduser");
        lCarts = db.getListCartOfUserChecked(Integer.parseInt(idUser));

        Mapping();
        DefaultValue();

        RecyclerView rv_confirm = (RecyclerView) findViewById(R.id.rv_confirm);
        DividerItemDecoration dividerHorizontal =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv_confirm.addItemDecoration(dividerHorizontal);
        /*rv_category.setHasFixedSize(true);*/
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_confirm.setLayoutManager(layoutManager);
        ConfirmOrderAdapter adapter = new ConfirmOrderAdapter(lCarts, this);
        rv_confirm.setAdapter(adapter);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    long millis=System.currentTimeMillis();   java.sql.Date date=new java.sql.Date(millis);
                    Bill bill = new Bill(
                            Integer.parseInt(idUser),
                            Integer.parseInt(tv_totalquantity.getText().toString()),
                            et_personname.getText().toString(),
                            et_personphone.getText().toString(),
                            et_personaddress.getText().toString(),
                            date.toString(),
                            0,
                            total_all_price);
                    int idbill = db.insertBill(bill);
                    Product product;
                    BillDetail billDetail;
                    for (int i = 0; i < lCarts.size(); i++){
                        product = db.getProduct(lCarts.get(i).getiIDProduct());
                        billDetail = new BillDetail(idbill,lCarts.get(i).getiIDProduct(), lCarts.get(i).getiQuantity(), product.getlPrice());
                        db.insertBillDetail(billDetail);
                    }
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }



    private boolean checkValidate(){
        int error = 0;
        if(et_personname.getText().toString().isEmpty()){
            error = error +1;
            et_personname.setError("Field can't be empty");
        }
        else et_personname.setError(null);
        if(et_personphone.getText().toString().isEmpty()){
            error = error +1;
            et_personphone.setError("Field can't be empty");
        }
        else et_personphone.setError(null);
        if(et_personaddress.getText().toString().isEmpty()){
            error = error +1;
            et_personaddress.setError("Field can't be empty");
        }
        else et_personaddress.setError(null);
        if (error == 0)
            return true;
        return false;
    }

    private void Mapping(){
        et_personname = (EditText) findViewById(R.id.et_personname);
        et_personphone = (EditText) findViewById(R.id.et_personphone);
        et_personaddress = (EditText) findViewById(R.id.et_personaddress);
        tv_totalprice = (TextView) findViewById(R.id.tv_totalprice_bill);
        tv_totalquantity = (TextView)findViewById(R.id.tv_totalquantity_bill);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    private void DefaultValue(){
        user = db.getUser(Integer.parseInt(idUser));
        et_personname.setText(user.getsName());
        et_personphone.setText(user.getsPhone());
        et_personaddress.setText(user.getsAddress());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        total_all_price = db.totalPriceCheckedInCart(Integer.parseInt(idUser))+(db.totalPriceCheckedInCart(Integer.parseInt(idUser)))*0.1;
        tv_totalprice.setText(currencyFormatter.format(total_all_price));
        tv_totalquantity.setText(String.valueOf(db.totalQuantityCheckedInCart(Integer.parseInt(idUser))));
    }
}