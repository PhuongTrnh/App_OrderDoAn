package com.example.store.activity.product;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.store.R;
import com.example.store.bean.Bill;
import com.example.store.bean.BillDetail;
import com.example.store.bean.Cart;
import com.example.store.bean.Product;
import com.example.store.constants.Resource;
import com.example.store.db.DatabaseHandler;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView iv_product;
    TextView tv_name;
    TextView tv_price;
    TextView tv_description, tv_quantity;
    EditText et_quantity;
    CardView cv_decrease;
    CardView cv_increase;
    Button btn_add;
    DatabaseHandler db;
    int quantityCart_temp;
    int quantityProduct_temp;
    private List<Cart> lCarts;
    private Product product;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Mapping();
        db = new DatabaseHandler(getApplicationContext());

        int product_id = getIntent().getIntExtra("idproduct",0);
        String iduser = getIntent().getStringExtra("iduser");

        et_quantity.setText("1");
        quantityCart_temp = Integer.parseInt(et_quantity.getText().toString());
        cv_decrease.setEnabled(false);

        Product product = db.getProduct(product_id);
        tv_name.setText(product.getsName());
        tv_description.setText(product.getsDescription());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        tv_price.setText(String.valueOf(currencyFormatter.format(product.getlPrice())));
        quantityProduct_temp = Integer.parseInt(et_quantity.getText().toString());

        tv_quantity.setText("" + product.getiQuantity());

        if(product.getsSource() == null){
            Uri imgUri= Uri.parse(Resource.RESOURCE_PATH);
            iv_product.setImageURI(null);
            iv_product.setImageURI(imgUri);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getsSource(), 0, product.getsSource().length);
            iv_product.setImageBitmap(bitmap);
        }

        if (product.getiQuantity() == 0) {
            cv_increase.setEnabled(false);
            btn_add.setEnabled(false);
        }
        et_quantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        String regex = "[0-9]+[\\.]?[0-9]*";
                        if (v.getText().length() <= 0 || v.getText().toString() == "0" || !Pattern.matches(regex, v.getText().toString())) {
                            Toast.makeText(ProductDetailActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                            et_quantity.setText(String.valueOf(quantityCart_temp));
                        } else if (Integer.parseInt(v.getText().toString()) > product.getiQuantity()) {
                            Toast.makeText(ProductDetailActivity.this, "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                            et_quantity.setText(String.valueOf(quantityCart_temp));
                        }
                        else if(Integer.parseInt(v.getText().toString()) == 1){
                            cv_decrease.setEnabled(false);
                        }
                        else {
                            quantityCart_temp = Integer.parseInt(et_quantity.getText().toString());
                            cv_decrease.setEnabled(true);
                        }
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });

        cv_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityCart_temp > 1){
                    quantityCart_temp = quantityCart_temp - 1;
                    et_quantity.setText(String.valueOf(quantityCart_temp));
                }
                else cv_decrease.setEnabled(false);
            }
        });

        cv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityCart_temp == product.getiQuantity()) {
                    Toast.makeText(ProductDetailActivity.this, "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                    et_quantity.setText(String.valueOf(quantityCart_temp));
                }
                else {
                    quantityCart_temp = quantityCart_temp + 1;
                    et_quantity.setText(String.valueOf(quantityCart_temp));
                }

                cv_decrease.setEnabled(true);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lCarts = db.getListCartOfUser(Integer.parseInt(iduser));
                Cart cart;
                int idcart = db.hasProductInCart(product_id,Integer.parseInt(iduser));
                if(idcart != 0){
                    Cart cart_quantity = db.getCart(idcart);
                    if(cart_quantity.getiQuantity() >= product.getiQuantity()){
                        Toast.makeText(ProductDetailActivity.this, "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cart = db.getCart(idcart);
                        cart.setiQuantity(quantityCart_temp + cart.getiQuantity());
                        db.updateQuantityCart(cart);
                    }
                }
                else {
                    cart = new Cart(product_id,Integer.parseInt(iduser),quantityCart_temp);
                    db.insertItemCart(cart);
                }
                Toast.makeText(ProductDetailActivity.this, product.getsName() + " has been added to your cart.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void Mapping(){
        iv_product = (ImageView)findViewById(R.id.iv_product);
        tv_name = (TextView)findViewById(R.id.tv_nameproduct);
        tv_price = (TextView)findViewById(R.id.tv_priceproduct);
        tv_description = (TextView)findViewById(R.id.tv_description);
        et_quantity = (EditText)findViewById(R.id.et_quantity);
        cv_decrease = (CardView)findViewById(R.id.cv_decrease);
        cv_increase = (CardView)findViewById(R.id.cv_increase);
        btn_add = (Button)findViewById(R.id.btn_add_to_cart);
        et_quantity.setTransformationMethod(null);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);
    }
}