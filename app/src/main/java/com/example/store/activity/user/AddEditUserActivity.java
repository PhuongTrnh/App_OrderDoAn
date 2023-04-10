package com.example.store.activity.user;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.store.R;
import com.example.store.bean.Role;
import com.example.store.bean.User;
import com.example.store.db.DatabaseHandler;

public class AddEditUserActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int PICK_IMAGE_REQUEST = 1;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;

    private EditText textName;
    private EditText textEmail;
    private EditText textAddress;
    private EditText textPhone;
    private Button buttonSave;
    private Button buttonCancel;
    private ImageView imgUser;
    private byte[] source;

    private Button ibtn_camera;
    private Button ibtn_library;

    private User user;
    private List<Role> roles ;
    private boolean needRefresh;
    private int mode;
    private Spinner spinnerRoles;
    private HashMap<String, Integer> hashRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);
        DatabaseHandler db = new DatabaseHandler(this);
        // setup value in displays
        this.textName = (EditText) this.findViewById(R.id.txt_user_name);
        this.textEmail = (EditText)this.findViewById(R.id.txt_user_email);
        this.textAddress = (EditText) this.findViewById(R.id.txt_user_address);
        this.textPhone= (EditText)this.findViewById(R.id.txt_user_phone);
        this.buttonSave = (Button)findViewById(R.id.btn_user_save);
        this.buttonCancel = (Button)findViewById(R.id.btn_user_cancel);
        this.spinnerRoles = (Spinner)findViewById(R.id.spinner_user_role);
        this.imgUser = (ImageView) findViewById(R.id.img_user);
        this.ibtn_camera = (Button) findViewById(R.id.btn_upload_image_user_cam);
        this.ibtn_library = (Button) findViewById(R.id.btn_upload_image_user_lib);


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
        this.user = (User) intent.getSerializableExtra("user");
        this.roles = db.getListRoles();
        List<String> items  = new ArrayList<>();

        hashRoles = new HashMap<>();
//        for(Role role : roles) {
//            hashRoles.put(role.getsName(), role.getiID());
//            items.add(role.getsName());
//        }
        hashRoles.put("ADMIN",1);
        hashRoles.put("USER",2);
        items.add("ADMIN");
        items.add("USER");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinnerRoles.setAdapter(adapter);

        if(user== null)  {
            this.mode = MODE_CREATE;
            this.spinnerRoles.setSelection(0);

        } else  {
            this.mode = MODE_EDIT;
            this.textName.setText(user.getsName());
            this.textEmail.setText(String.valueOf(user.getsEmail()));
            this.textAddress.setText(String.valueOf(user.getsAddress()));
            this.textPhone.setText(String.valueOf(user.getsPhone()));
            String role = "";
            for(String i : hashRoles.keySet()) {
                if(hashRoles.get(i).equals(user.getiRole())) {
                    role = i;
                }
            }
            if(role.equals("")) {
                this.spinnerRoles.setSelection(0);
            } else {
                this.spinnerRoles.setSelection(items.indexOf(role));
            }
            if(user.getsSource() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(user.getsSource(), 0, user.getsSource().length);
                this.imgUser.setImageBitmap(bitmap);
            }

        }

    }
    // User Click on the Save button.
    public void buttonSaveClicked()  {
        ImageViewToByteArray();
        DatabaseHandler db = new DatabaseHandler(this);
        String name = this.textName.getText().toString();
        String address = this.textAddress.getText().toString();
        String email = this.textEmail.getText().toString();
        String phone = this.textPhone.getText().toString();
        String categoryString = this.spinnerRoles.getSelectedItem().toString();
        Integer idRoles = this.hashRoles.get(categoryString);
        if(name.equals("")||address.equals("")||email.equals("")||phone.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter name & address & phone", Toast.LENGTH_LONG).show();
            return;
        }

        if(mode == MODE_CREATE ) {
            if(this.source!=null) {
                this.user = new User(name,email,address,phone,idRoles,this.source);
            } else {
                this.user = new User(name,email,address,phone,idRoles);

            }
            db.registerUser(user);
        } else  {
            System.out.println("data get in form");
            this.user.setsName(name);
            this.user.setsEmail(email);
            this.user.setsAddress(address);
            this.user.setsPhone(phone);
            this.user.setiRole(idRoles);
            this.user.setbState(1);
            this.user.setbVerifyEmail(1);
            if(this.source != null ) {
                this.user.setsSource(this.source);
            }
            db.updateUser(user);
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
                this.imgUser.setImageBitmap(image);
            }
            catch (Exception e){

            }
        }
        if(requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK){
            try {
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    this.imgUser.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    private void ImageViewToByteArray(){
        // Lưu hình dạng byte[]
        Bitmap image = ((BitmapDrawable) this.imgUser.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        source = baos.toByteArray();
    }
}