package com.example.store.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.store.R;
import com.example.store.SendEmail;
import com.example.store.db.DatabaseHandler;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btn_back, btn_submit;
    EditText et_mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        et_mail = (EditText) findViewById(R.id.et_code);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_mail.getText().toString();
                DatabaseHandler db = new DatabaseHandler(ForgotPasswordActivity.this);
                if (db.checkEmail(email)) {
                    Random random = new Random();
                    String code = String.valueOf(random.nextInt(900000) + 100000);
                    try {
                        new SendEmail(email, code).execute();
                        Intent Icode = new Intent(ForgotPasswordActivity.this, CodeActivity.class);
                        Icode.putExtra("code" , code);
                        Icode.putExtra("email", email);
                        startActivity(Icode);

                        Toast.makeText(ForgotPasswordActivity.this, "Gửi mail thành công, vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                        et_mail.setText("");
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email chưa được đăng ký...", Toast.LENGTH_SHORT).show();
                    et_mail.setText("");
                }
            }
        });
    }
}