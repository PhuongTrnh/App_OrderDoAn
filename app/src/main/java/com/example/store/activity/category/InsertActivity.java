package com.example.store.activity.category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.CategoryProduct;
import com.example.store.bean.Product;

public class InsertActivity extends AppCompatActivity {
    private EditText name, idcate, price, des, quantity, state;
    private Button btncate, btnpro, btnimage;
    private ImageView iv;
    private byte[] source;
    private static final int PICK_IMAGE_REQUEST = 1;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        db = new DatabaseHandler(getApplicationContext());
        name = findViewById(R.id.name);
        idcate = findViewById(R.id.et_idcate);
        price = findViewById(R.id.price);
        des = findViewById(R.id.des);
        quantity = findViewById(R.id.quantity);
        state = findViewById(R.id.state);
        btncate = findViewById(R.id.btncate);
        btnpro = findViewById(R.id.btnPro);
        btnimage = findViewById(R.id.bt_im);
        iv = findViewById(R.id.imageView);

        btnimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
            }
        });

        btncate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewToByteArray();
                db.insertCategoryProduct(new CategoryProduct(name.getText().toString(),source));
                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
            }
        });

        btnpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewToByteArray();
                db.insertProduct(new Product(name.getText().toString(),
                        Integer.parseInt(idcate.getText().toString().trim()),
                        Long.parseLong(price.getText().toString()),
                        des.getText().toString(),
                        source,
                        Integer.parseInt(quantity.getText().toString()),
                        Integer.parseInt(state.getText().toString())));
                Toast.makeText(getApplicationContext(),"ok",Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK){
            try {
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    iv.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void ImageViewToByteArray(){
        // Lưu hình dạng byte[]
        Bitmap image = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        source = baos.toByteArray();
    }
}