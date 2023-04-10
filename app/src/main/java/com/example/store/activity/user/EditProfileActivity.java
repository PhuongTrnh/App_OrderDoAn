package com.example.store.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.User;

public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton ibtn_camera;
    private ImageButton ibtn_library;
    private ImageView iv_avatar;
    private EditText et_name, et_email, et_phone, et_address;
    private Button btn_confirm;
    private DatabaseHandler db;
    private User user;
    private byte[] source;
    private String nameInput;
    private String phoneInput;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Mapping();
        String iduser = getIntent().getStringExtra("iduser");
        db = new DatabaseHandler(this);
        user = db.getUser(Integer.parseInt(iduser));
        setValue();

        ibtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, CAMERA_PIC_REQUEST);
            }
        });

        ibtn_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    ImageViewToByteArray();
                    user.setsName(nameInput);
                    user.setsPhone(phoneInput);
                    user.setsAddress(et_address.getText().toString().trim());
                    user.setsSource(source);

                    db.updateUser(user);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    Toast.makeText(getApplicationContext(),"Edit profile sucessful!", Toast.LENGTH_LONG).show();
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean checkValidate(){
        if(validateName() && validatePhone()){
            return true;
        }
        return false;
    }

    private boolean validateName() {
        nameInput = et_name.getText().toString().trim();

        if (nameInput.isEmpty()) {
            et_name.setError("Field can't be empty");
            return false;
        } else {
            et_name.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        phoneInput = et_phone.getText().toString().trim();

        if (db.checkIfPhoneExistsEdit(phoneInput)) {
            et_phone.setError("Phone already exist");
            return false;
        } else if (phoneInput.isEmpty()) {
            et_phone.setError("Field can't be empty");
            return false;
        } else if (!Patterns.PHONE.matcher(phoneInput).matches()) {
            et_phone.setError("Please enter a valid phone");
            return false;
        } else {
            et_phone.setError(null);
            return true;
        }
    }

    private void Mapping(){
        ibtn_camera = (ImageButton)findViewById(R.id.ibtn_camera);
        ibtn_library = (ImageButton)findViewById(R.id.ibtn_library);
        iv_avatar = (ImageView)findViewById(R.id.iv_avatar_profile);
        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_address = (EditText)findViewById(R.id.et_address);
        btn_confirm = (Button)findViewById(R.id.btn_confirmedit);
        toolbar = (Toolbar)findViewById(R.id.toolbar_appbar);
    }

    private void setValue(){
        et_name.setText(user.getsName());
        et_email.setText(user.getsEmail());
        et_phone.setText(user.getsPhone());
        et_address.setText(user.getsAddress());
        if(user.getsSource() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getsSource(), 0, user.getsSource().length);
            iv_avatar.setImageBitmap(bitmap);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST  && resultCode == RESULT_OK) {
            try {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                iv_avatar.setImageBitmap(image);
            }
            catch (Exception e){

            }
        }
        if(requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK){
            try {
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    iv_avatar.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void ImageViewToByteArray(){
        // Lưu hình dạng byte[]
        Bitmap image = ((BitmapDrawable) iv_avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        source = baos.toByteArray();
    }
}