package com.example.store.activity.product;

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
import com.example.store.bean.Product;
import com.example.store.constants.Resource;
import com.example.store.db.DatabaseHandler;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView iv_product;
    TextView tv_name;
    TextView tv_price;
    TextView tv_description;
    EditText et_quantity;
    CardView cv_decrease;
    CardView cv_increase;
    Button btn_add;
    DatabaseHandler db;
    int quantity_temp;
//    private List<Cart> lCarts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Mapping();
        db = new DatabaseHandler(getApplicationContext());
        int id = getIntent().getIntExtra("idproduct",0);
        String iduser = getIntent().getStringExtra("iduser");

        et_quantity.setText("1");
        quantity_temp = Integer.parseInt(et_quantity.getText().toString());
        cv_decrease.setEnabled(false);
        Product product = db.getProduct(id);
        tv_name.setText(product.getsName());
        tv_description.setText(product.getsDescription());
        Locale locale = new Locale("vn","VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        tv_price.setText(String.valueOf(currencyFormatter.format(product.getlPrice())));
        if(product.getsSource() == null){
            Uri imgUri= Uri.parse(Resource.RESOURCE_PATH);
            iv_product.setImageURI(null);
            iv_product.setImageURI(imgUri);
        }
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getsSource(), 0, product.getsSource().length);
            iv_product.setImageBitmap(bitmap);
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
                            et_quantity.setText(String.valueOf(quantity_temp));
                        } else if (Integer.parseInt(v.getText().toString()) > product.getiQuantity()) {
                            Toast.makeText(ProductDetailActivity.this, "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                            et_quantity.setText(String.valueOf(quantity_temp));
                        }
                        else if(Integer.parseInt(v.getText().toString()) == 1){
                            cv_decrease.setEnabled(false);
                        }
                        else {
                            quantity_temp = Integer.parseInt(et_quantity.getText().toString());
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
                if(quantity_temp > 1){
                    quantity_temp = quantity_temp - 1;
                    et_quantity.setText(String.valueOf(quantity_temp));
                }
                else cv_decrease.setEnabled(false);
            }
        });

        cv_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity_temp == product.getiQuantity()) {
                    Toast.makeText(ProductDetailActivity.this, "Product exceeds the allowed quantity", Toast.LENGTH_SHORT).show();
                    et_quantity.setText(String.valueOf(quantity_temp));
                }
                else {
                    quantity_temp = quantity_temp + 1;
                    et_quantity.setText(String.valueOf(quantity_temp));
                }

                cv_decrease.setEnabled(true);
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

        et_quantity.setTransformationMethod(null);
    }
}