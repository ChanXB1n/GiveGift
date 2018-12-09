package com.example.chan.firstlevelproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;



public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button linkToRegisterButton;
    private Button linkToResetScreenButton;
    private EditText nameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameInput = (EditText) findViewById(R.id.emailInput);//email输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//密码输入框
        loginButton = (Button) findViewById(R.id.loginButton);//登陆按钮
        linkToRegisterButton = (Button) findViewById(R.id.linkToRegisterScreenButton);//跳转到注册页面按钮
        linkToResetScreenButton=(Button) findViewById(R.id.linkToResetScreenButton);//跳转重置密码窗口
        loginButton.setOnClickListener(new loginOnClickListener());
        linkToRegisterButton.setOnClickListener(new linkToRegisterOnClickListener());
        linkToResetScreenButton.setOnClickListener(new linkToResetOnClickListener());

        SharedPreferences checkLogin=getSharedPreferences("data",MODE_PRIVATE);
        boolean isLogin=checkLogin.getBoolean("isFirstLogin",false);
        if(isLogin==true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class loginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String name = nameInput.getText().toString();
            String password = passwordInput.getText().toString();

            //判断输入是否为空
            if (name.trim().length() > 0 && password.trim().length() > 0) {
                checkLogin(name, password);
            }else {
                Toast.makeText(getApplicationContext(), " 请输入用户名或密码", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class linkToRegisterOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //跳转到注册页面
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class linkToResetOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //跳转到重置密码页面
            Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkLogin(final String name, final String password){
        List<Account> accounts= DataSupport.findAll(Account.class);
        for(Account account:accounts){
            if((name.equals(account.getName()))&&(password.equals(account.getPassword()))){
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putBoolean("isFirstLogin",true);
                editor.putString("name",name);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}

