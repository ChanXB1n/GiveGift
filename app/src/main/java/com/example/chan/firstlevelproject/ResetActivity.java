package com.example.chan.firstlevelproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.litepal.crud.DataSupport;
import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ResetActivity extends AppCompatActivity {

    private Button resetButton;
    private Button linkToLoginButton;
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText phoneNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        nameInput = (EditText) findViewById(R.id.nameInput);//用户名输入框
        emailInput = (EditText) findViewById(R.id.emailInput);//邮箱输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//密码输入框
        passwordConfirmInput = (EditText) findViewById(R.id.passwordConfirmInput);//密码确认输入框
        phoneNumberInput = (EditText) findViewById(R.id.phoneNumberInput);//手机号码输入框
        resetButton = (Button) findViewById(R.id.resetButton);//注册按钮
        linkToLoginButton = (Button) findViewById(R.id.linkToLoginScreenButton);//跳转到登陆页面按钮

        linkToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click");
                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String passwordConfirm = passwordConfirmInput.getText().toString();
                String phoneNumber = phoneNumberInput.getText().toString();
                boolean cancel = true;

                List<Account> accounts= DataSupport.findAll(Account.class);
                for(Account account:accounts){
                    if((account.getName().equals(name))&&(account.getEmail().equals(email))&&(account.getPhoneNumber().equals(phoneNumber))&&(password.equals(passwordConfirm))){
                        account.setPassword(password);
                        account.updateAll("name =? and email = ? and phoneNumber = ?", name,email,phoneNumber);
                        cancel=false;
                    }
                }
                if(cancel==true){
                    Toast.makeText(getApplicationContext(), "输入有误/信息不匹配，请再次确认", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "密码已重置", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

