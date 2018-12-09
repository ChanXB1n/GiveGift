package com.example.chan.firstlevelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private Button linkToLoginButton;
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText phoneNumberInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameInput = (EditText) findViewById(R.id.nameInput);//用户名输入框
        emailInput = (EditText) findViewById(R.id.emailInput);//邮箱输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//密码输入框
        passwordConfirmInput = (EditText) findViewById(R.id.passwordConfirmInput);//密码确认输入框
        phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);//手机号码输入框
        registerButton = (Button) findViewById(R.id.registerButton);//注册按钮
        linkToLoginButton = (Button) findViewById(R.id.linkToLoginScreenButton);//跳转到登陆页面按钮

        linkToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String passwordConfirm = passwordConfirmInput.getText().toString();
                String phoneNumber = phoneNumberInput.getText().toString();
                boolean cancel = false;
                View focusView = null;
                String errorMsg = null;

                //判断密码是否为空
                if (TextUtils.isEmpty(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "请输入密码哦";
                    //      passwordInput.setError("请输入密码哦");
                } else if (!isPasswordValid(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "密码长度不能少于6位哦";
                    //          passwordInput.setError("密码长度不能少于6位哦");
                }

                //判断名字是否为空
                if (TextUtils.isEmpty(name)) {
                    focusView = nameInput;
                    cancel = true;
                    errorMsg = "请输入名字哦";
                    //        nameInput.setError("请输入名字哦");
                }

                //判断邮箱是否为空且是否符合格式,必须包含@
                if (TextUtils.isEmpty(email)) {
                    focusView = emailInput;
                    cancel = true;
                    errorMsg = "请输入邮箱哦";
                    //       emailInput.setError("请输入邮箱哦");
                } else if (!isEmailValid(email)) {
                    focusView = emailInput;
                    cancel = true;
                    errorMsg = "邮箱格式不对哦";
                    //    emailInput.setError("邮箱格式不对哦");
                }

                //判断有无输入确认密码和两次密码输入是否相同
                if (TextUtils.isEmpty(passwordConfirm)) {
                    focusView = passwordConfirmInput;
                    cancel = true;
                    errorMsg = "请输入确认密码哦";
                    //      passwordConfirmInput.setError("请输入密码哦");
                } else if (!isPasswordValid(password)) {
                    focusView = passwordConfirmInput;
                    cancel = true;
                    errorMsg = "密码长度不能少于6位哦";
                    //          passwordConfirmInput.setError("密码长度不能少于6位哦");
                } else if (!password.equals(passwordConfirm)) {
                    focusView = passwordConfirmInput;
                    cancel = true;
                    errorMsg = "两次密码输入不一样哦";
                    //          passwordConfirmInput.setError("两次密码输入不一样哦");
                }

                //判断手机号码是否为空且是否符合格式，必须11位
                if (TextUtils.isEmpty(phoneNumber)) {
                    focusView = phoneNumberInput;
                    cancel = true;
                    errorMsg = "请输入手机号码哦";
                    //       phoneNumberInput.setError("请输入手机号码哦");
                } else if (!isPhoneNumberValid(phoneNumber)) {
                    focusView = phoneNumberInput;
                    cancel = true;
                    errorMsg = "手机号码格式不对哦";
                    //    phoneNumberInput.setError("手机号码格式是11位的哦");
                }

                //判断用户输入是否有效,如果有效,则进行注册操作
                if (!cancel) {
                    registerUser(name, email, password, phoneNumber);
                } else {
                    focusView.requestFocus();
                    cancel = false;
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(final String name, final String email, final String password,final String phoneNumber){
        Account account=new Account();
        account.setName(name);
        account.setEmail(email);
        account.setPassword(password);
        account.setPhoneNumber(phoneNumber);
        account.save();
        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //判断邮箱是否包含@
    private boolean isEmailValid(String email) {
        if(!email.contains("@")) {
            return false;
        }else {
            return true;
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }else {
            return true;
        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        if(phoneNumber.length()!=11) {
            return false;
        }else {
            return true;
        }
    }
}


