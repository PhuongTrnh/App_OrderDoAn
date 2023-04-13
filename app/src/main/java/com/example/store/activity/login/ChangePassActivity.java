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
                // kiểm tra validate của 2 mật khẩu trên theo phần đăng ký user (đúng 8 ký tự trở lên)
                if (newPass.length() >= 8 && confirmNewPass.length()>=8 && newPass.equals(confirmNewPass)) {
                    DatabaseHandler db = new DatabaseHandler(ChangePassActivity.this);
                    User user = db.getUserByEmail(email);
                    user.setsPassword(newPass);
                    db.changePassword(user);

                    // Nếu cập nhật thành công thì chuyển sang giao diện login và kèm theo email đăng nhập để nhập sẵn email
                    Intent backlo = new Intent(ChangePassActivity.this, LoginActivity.class);
                    startActivity(backlo);
                    Toast.makeText(ChangePassActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    backlo.putExtra("email", email);
                    backlo.putExtra("password", "");
                    backlo.putExtra("KEY_USER_FROM_REGISTER", email);
                    setResult(RESULT_OK, backlo);
                    finish();
                } else {
                    Toast.makeText(ChangePassActivity.this, "Mật khẩu không trùng khớp hoặc mật khẩu dưới 8 kí tự", Toast.LENGTH_SHORT).show();
                    et_confirmnewpassword.setText("");
                }
            }
        });
    }

}