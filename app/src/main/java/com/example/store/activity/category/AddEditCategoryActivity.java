package com.example.store.activity.category;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.store.R;
import com.example.store.bean.CategoryProduct;
import com.example.store.db.DatabaseHandler;

public class AddEditCategoryActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private TextView textId;
    private EditText textContent;
    private Button buttonSave;
    private Button buttonCancel;
    private ImageView imgCategory;
    private byte[] source;

    private Button ibtn_camera;
    private Button ibtn_library;

    private CategoryProduct categoryProduct;
    private boolean needRefresh;
    private int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        this.textId = (TextView) this.findViewById(R.id.editText_category_id);
        this.textContent = (EditText)this.findViewById(R.id.editText_category_name);

        this.buttonSave = (Button)findViewById(R.id.button_save);
        this.buttonCancel = (Button)findViewById(R.id.button_cancel);
        this.imgCategory = (ImageView)findViewById(R.id.img_category);
        this.ibtn_camera = (Button)findViewById(R.id.btn_upload_image_category_cam);
        this.ibtn_library = (Button) findViewById(R.id.btn_upload_image_category_lib);

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
        this.categoryProduct = (CategoryProduct) intent.getSerializableExtra("category");
        if(categoryProduct== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.textId.setText(String.valueOf(categoryProduct.getiID()) );
            this.textContent.setText(categoryProduct.getsName());
            if(categoryProduct.getsSource()!= null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(categoryProduct.getsSource(), 0, categoryProduct.getsSource().length);
                this.imgCategory.setImageBitmap(bitmap);
            }
        }

    }

    // User Click on the Save button.
    public void buttonSaveClicked()  {
        ImageViewToByteArray();
        DatabaseHandler db = new DatabaseHandler(this);

        String title = this.textId.getText().toString();
        String content = this.textContent.getText().toString();

        if(content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }

        if(mode == MODE_CREATE ) {
            if(source!=null) {
                this.categoryProduct = new CategoryProduct(content,this.source);
            } else {
                this.categoryProduct = new CategoryProduct(content);
            }
            db.insertCategoryProduct(categoryProduct);
        } else  {
            this.categoryProduct.setiID(Integer.parseInt(title));
            this.categoryProduct.setsName(content);
            if(this.source != null ) {
                this.categoryProduct.setsSource(this.source);
            }
            db.updateCategoryProduct(categoryProduct);
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
                this.imgCategory.setImageBitmap(image);
            }
            catch (Exception e){

            }
        }
        if(requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK){
            try {
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    this.imgCategory.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void ImageViewToByteArray(){
        // Lưu hình dạng byte[]
        Bitmap image = ((BitmapDrawable) this.imgCategory.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        this.source = baos.toByteArray();
    }
}