package com.example.store.activity.user;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button btn_confirm;
    private EditText et_oldpassword, et_newpassword, et_confirmpassword;
    private DatabaseHandler db;
    private String oldPasswordInput;
    private String newPasswordConfirm;
    private String newPasswordInput;
    private User user;
    private String iduser;
    SharedPreferences sharedPreferences;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        iduser = getIntent().getStringExtra("iduser");
        Mapping();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        Boolean checked = sharedPreferences.getBoolean("checked",false);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    user.setsPassword(newPasswordInput);
                    db.changePassword(user);
                    Toast.makeText(getApplicationContext(),"Change password sucessful!", Toast.LENGTH_LONG).show();
                    et_oldpassword.setText("");
                    et_newpassword.setText("");
                    et_confirmpassword.setText("");
                    if(checked){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("password", "");
                        editor.commit();
                    }
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

    private void Mapping(){
        btn_confirm = findViewById(R.id.btn_confirmchangepass);
        et_oldpassword = findViewById(R.id.et_oldpassword);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_confirmpassword = findViewById(R.id.et_confirmnewpassword);
        db = new DatabaseHandler(this);
        user = db.getUser(Integer.parseInt(iduser));
        toolbar = findViewById(R.id.toolbar_appbar);
    }

    private boolean checkValidate(){
        if(validateOldPassword() && validateNewPassword() && validatePasswordConfirm()){
            return true;
        }
        return false;
    }

    private boolean validateOldPassword() {
        oldPasswordInput = et_oldpassword.getText().toString().trim();
        if (oldPasswordInput.isEmpty()) {
            et_oldpassword.setError("Field can't be empty");
            return false;
        } else if(oldPasswordInput.length() < 8){
            et_oldpassword.setError("Password must contain minimum 8 characters");
            return false;
        }
        else if(!db.checkOldPassword(Integer.parseInt(iduser),oldPasswordInput)){
            et_oldpassword.setError("The old password you entered is incorrect");
            return false;
        }
        else {
            et_oldpassword.setError(null);
            return true;
        }
    }

    private boolean validateNewPassword() {
        newPasswordInput = et_newpassword.getText().toString().trim();
        if (newPasswordInput.isEmpty()) {
            et_newpassword.setError("Field can't be empty");
            return false;
        } else if(newPasswordInput.length() < 8){
            et_newpassword.setError("Password must contain minimum 8 characters");
            return false;
        }
        else {
            et_newpassword.setError(null);
            return true;
        }
    }

    private boolean validatePasswordConfirm() {
        newPasswordConfirm = et_confirmpassword.getText().toString().trim();
        if (newPasswordConfirm.isEmpty()) {
            et_confirmpassword.setError("Field can't be empty");
            return false;
        } else if(!newPasswordConfirm.equals(et_newpassword.getText().toString().trim())){
            et_confirmpassword.setError("New password and confirm new password does not match");
            return false;
        }
        else {
            et_confirmpassword.setError(null);
            return true;
        }
    }

}