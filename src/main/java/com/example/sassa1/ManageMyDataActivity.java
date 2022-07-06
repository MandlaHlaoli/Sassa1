package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManageMyDataActivity extends AppCompatActivity {

    Button btnViewAppointments,btnBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_data);

        btnViewAppointments = findViewById(R.id.btnViewAppointments);
        btnBook = findViewById(R.id.btnBook);

        String title = "Manage my information";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        btnBook.setOnClickListener(view -> startActivity(new Intent(ManageMyDataActivity.this,UserActivity.class)));

        btnViewAppointments.setOnClickListener(view -> startActivity(new Intent(ManageMyDataActivity.this,ListAppointmentsActivity.class)));
    }
}