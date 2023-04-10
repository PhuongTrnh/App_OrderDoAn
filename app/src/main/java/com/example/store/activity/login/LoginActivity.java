package com.example.store.activity.login;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.example.store.activity.MainActivity;
import com.example.store.db.DatabaseHandler;
import com.example.store.R;
import com.example.store.bean.User;

public class LoginActivity extends AppCompatActivity {

    Button btn_register;
    TextView tv_forgotpassword;
    Button btn_login;
    EditText et_email;
    EditText et_password;
    DatabaseHandler db;
    String emailInput;
    String passwordInput;
    CheckBox cb_rememberMe;
    public static final int REQUEST_CODE_REGISTER = 1;
    public static final String KEY_USER_TO_MAIN = "KEY_USER_TO_MAIN";
    public static final String KEY_USER_FROM_REGISTER = "KEY_USER_FROM_REGISTER";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(getApplicationContext());
        Mapping();
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);

        et_email.setText(sharedPreferences.getString("username", ""));
        et_password.setText(sharedPreferences.getString("password", ""));
        cb_rememberMe.setChecked(sharedPreferences.getBoolean("checked", false));

        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getBaseContext(), RegisterActivity.class);
                startActivityForResult(intentRegister, REQUEST_CODE_REGISTER);
            }
        });

//quen pass
        tv_forgotpassword = (TextView)findViewById(R.id.tv_forgotpassword);
        tv_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getBaseContext(), ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
            }
        });

        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    if (!db.checkIfEmailExists(emailInput)) {
                        et_email.setError("Email address isn't registered yet.");
                    } else
                    if(db.checkLogin(emailInput, passwordInput)){
                        int iduser = db.getIDUser(emailInput);
                        User user = db.getUser(iduser);
                        if(user.getiState() == 1){
                            if(cb_rememberMe.isChecked()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("username", emailInput);
                                editor.putString("password", passwordInput);
                                editor.putBoolean("checked" ,true);
                                editor.commit();
                            }
                            else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("username");
                                editor.remove("password");
                                editor.remove("checked");
                                editor.commit();
                            }
                            et_email.setError(null);
                            et_password.setError(null);
                            Intent intentMainActivity = new Intent(getBaseContext(), MainActivity.class);
                            intentMainActivity.putExtra(KEY_USER_TO_MAIN, String.valueOf(iduser));
                            intentMainActivity.putExtra("idRole", String.valueOf(user.getiRole()));
                            startActivity(intentMainActivity);
                            finish();
                        }
                        else Toast.makeText(getApplicationContext(),"Your account has been disabled",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        et_password.setError("The password you entered is incorrect.");
                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REGISTER && resultCode == Activity.RESULT_OK) {
            String email = data.getStringExtra(KEY_USER_FROM_REGISTER);
            et_email.setText(email);
            et_password.setText("");
            et_password.requestFocus();
        }
    }

    private void logD(){
        List<User> lUser = db.getListUser();
        for(int i = 0; i < lUser.size(); i++)
        {
            Log.d("List User", String.valueOf(lUser.get(i).getiID()) + " " + lUser.get(i).getsName());
        }
    }

    public void Mapping(){
        et_email = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        cb_rememberMe = (CheckBox)findViewById(R.id.cb_rememberme);
    }

    private boolean checkValidate(){
        if(validateEmail() == true && validatePassword() == true){
            return true;
        }
        return false;
    }

    private boolean validateEmail() {
        emailInput = et_email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            et_email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            et_email.setError("Please enter a valid email address");
            return false;
        } else {
            et_email.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        passwordInput = et_password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            et_password.setError("Field can't be empty");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }
}