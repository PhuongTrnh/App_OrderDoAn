package com.example.store.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.store.R;
import com.example.store.activity.MainActivity;
import com.example.store.bean.User;
import com.example.store.db.DatabaseHandler;

public class ChangePassActivity extends AppCompatActivity {

    EditText et_newpassword, et_confirmnewpassword;
    Button btn_confirmchangepass;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        String email = getIntent().getStringExtra("email");
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_confirmnewpassword = (EditText) findViewById(R.id.et_confirmnewpassword);
        btn_confirmchangepass = (Button) findViewById(R.id.btn_confirmchangepass);
        btn_confirmchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = et_newpassword.getText().toString();
                String confirmNewPass = et_confirmnewpassword.getText().toString();
                if (newPass.equals(confirmNewPass)) {
                    DatabaseHandler db = new DatabaseHandler(ChangePassActivity.this);
                    User user = db.getUserByEmail(email);
                    user.setsPassword(newPass);
                    db.changePassword(user);

                    Intent i = new Intent(ChangePassActivity.this, LoginActivity.class);
                    Toast.makeText(ChangePassActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    i.putExtra("KEY_USER_FROM_REGISTER", email);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    et_confirmnewpassword.setText("");
                }
            }
        });
    }

}