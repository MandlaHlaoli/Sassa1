package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin, btnRegister,btnReset,btnExit;

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnReset = findViewById(R.id.btnForgotPassword);
        btnExit = findViewById(R.id.btnExit);

        String title = "Welcome to my SASSA App";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setIcon(R.drawable.logo_only);


        btnLogin.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,LoginActivity.class)));
        btnRegister.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,RegisterUserActivity.class)));
        btnReset.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class)));
        btnExit.setOnClickListener(view -> MainActivity.this.finish());
    }
}