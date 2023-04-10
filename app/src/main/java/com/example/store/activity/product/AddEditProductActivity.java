package com.example.store.activity.product;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.store.R;
import com.example.store.bean.CategoryProduct;
import com.example.store.bean.Product;
import com.example.store.db.DatabaseHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddEditProductActivity extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText textName;
    private EditText textPrice;
    private EditText textDescription;
    private EditText textQuantity;
    private Button buttonSave;
    private Button buttonCancel;
    private ImageView imgProduct;
    private byte[] source;

    private Button ibtn_camera;
    private Button ibtn_library;

    private Product product;
    private List<CategoryProduct> categories ;
    private boolean needRefresh;
    private int mode;
    private Spinner spinnerCategories;
    private HashMap<String, Integer> hashCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHandler db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_product);

        // setup value in displays
        this.textName = (EditText) this.findViewById(R.id.txt_product_name);
        this.textPrice = (EditText)this.findViewById(R.id.txt_product_price);
        this.textDescription = (EditText) this.findViewById(R.id.txt_product_description);
        this.textQuantity= (EditText)this.findViewById(R.id.txt_product_quantity);
        this.buttonSave = (Button)findViewById(R.id.btn_product_save);
        this.buttonCancel = (Button)findViewById(R.id.btn_product_cancel);
        this.spinnerCategories = (Spinner)findViewById(R.id.spiner_categories);
        this.imgProduct = (ImageView) findViewById(R.id.imgProduct);
        this.ibtn_camera = (Button) findViewById(R.id.btn_upload_image_product_cam);
        this.ibtn_library = (Button) findViewById(R.id.btn_upload_image_product_lib);


        this.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                buttonSaveClicked();
            }
        });

        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                buttonCancelClicked();
            }
        });


        this.ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAMERA_PIC_REQUEST);
            }
        });

        this.ibtn_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
            }
        });

        Intent intent = this.getIntent();
        this.product = (Product) intent.getSerializableExtra("product");
        this.categories = db.getListCategoryProduct();
        List<String> items  = new ArrayList<>();

        hashCategories = new HashMap<>();
        for(CategoryProduct c : categories) {
            hashCategories.put(c.getsName(), c.getiID());
            items.add(c.getsName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinnerCategories.setAdapter(adapter);

        if(product== null)  {
            this.mode = MODE_CREATE;
            this.spinnerCategories.setSelection(0);

        } else  {
            this.mode = MODE_EDIT;
            this.textName.setText(product.getsName());
            this.textPrice.setText(String.valueOf(product.getlPrice()));
            this.textQuantity.setText(String.valueOf(product.getiQuantity()));
            this.textDescription.setText(String.valueOf(product.getsDescription()));
            String category = "";
            for(String i : hashCategories.keySet()) {
                if(hashCategories.get(i).equals(product.getiIDCategory())) {
                    category = i;
                }
            }
            if(category.equals("")) {
                this.spinnerCategories.setSelection(0);
            } else {
                this.spinnerCategories.setSelection(items.indexOf(category));
            }
            if(product.getsSource() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getsSource(), 0, product.getsSource().length);
                this.imgProduct.setImageBitmap(bitmap);
            }

        }

    }

    // User Click on the Save button.
    public void buttonSaveClicked()  {
        ImageViewToByteArray();
        DatabaseHandler db = new DatabaseHandler(this);
        String name = this.textName.getText().toString();
        String price = this.textPrice.getText().toString();
        String textDescription = this.textDescription.getText().toString();
        String textQuantity = this.textQuantity.getText().toString();
        String categoryString = this.spinnerCategories.getSelectedItem().toString();
        Integer idCategory = this.hashCategories.get(categoryString);
        if(name.equals("")||price.equals("")||textQuantity.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter name & price & quantity", Toast.LENGTH_LONG).show();
            return;
        }

        if(mode == MODE_CREATE ) {
            if(this.source!=null) {
                this.product = new Product(name,Long.parseLong(price),textDescription,Integer.parseInt(textQuantity),idCategory,this.source);
            } else {
                this.product = new Product(name,Long.parseLong(price),textDescription,Integer.parseInt(textQuantity),idCategory);

            }
            db.insertProduct(product);
        } else  {
            System.out.println("data get in form");
            this.product.setsName(name);
            this.product.setlPrice(Long.parseLong(price));
            this.product.setsDescription(textDescription);
            this.product.setiQuantity(Integer.parseInt(textQuantity));
            this.product.setiIDCategory(idCategory);
            this.product.setiState(1);
            if(this.source != null ) {
                this.product.setsSource(this.source);
            }
            db.updateProduct(product);
        }

        this.needRefresh = true;

        // Back to MainActivity.
        this.onBackPressed();
    }

    // User Click on the Cancel button.
    public void buttonCancelClicked()  {
        // Do nothing, back MainActivity.
        this.onBackPressed();
    }


    // When completed this Activity,
    // Send feedback to the Activity called it.
    @Override
    public void finish() {

        // Create Intent
        Intent data = new Intent();

        // Request MainActivity refresh its ListView (or not).
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST  && resultCode == RESULT_OK) {
            try {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                this.imgProduct.setImageBitmap(image);
            }
            catch (Exception e){

            }
        }
        if(requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK){
            try {
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    this.imgProduct.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void ImageViewToByteArray(){
        // Lưu hình dạng byte[]
        Bitmap image = ((BitmapDrawable) this.imgProduct.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        source = baos.toByteArray();
    }
}