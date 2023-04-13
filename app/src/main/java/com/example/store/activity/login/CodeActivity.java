package com.example.store.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.store.R;

public class CodeActivity extends AppCompatActivity {
    EditText tcode;
    Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        String code = getIntent().getStringExtra("code");
        String email = getIntent().getStringExtra("email");
        tcode = findViewById(R.id.et_code);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tcode.getText().toString().equals(code)){
                    // Nếu trùng mã code thì chuyển tới trang đổi mật khẩu
                    Intent i = new Intent(CodeActivity.this, ChangePassActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                } else {
                    Toast.makeText(CodeActivity.this, "Mã xác thực không hợp lệ, vui lòng nhập lại...", Toast.LENGTH_SHORT).show();
                    tcode.setText("");
                }
            }
        });

    }
}