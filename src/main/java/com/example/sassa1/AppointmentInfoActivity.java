package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AppointmentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        TextView tvId = findViewById(R.id.tvId);
        TextView tvFullNames = findViewById(R.id.tvFullNames);
        TextView tvSurname = findViewById(R.id.tvSurname);
        TextView tvGrantType = findViewById(R.id.tvGrantType);
        TextView tvStation = findViewById(R.id.tvStation);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvCounter = findViewById(R.id.tvCounter);
        TextView tvQueue = findViewById(R.id.tvQueue);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        String title = "Appointment information";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        int index = getIntent().getIntExtra("index",0);

        tvId.setText(AppClass.appointmentList.get(index).getIdNumber());
        tvFullNames.setText(AppClass.appointmentList.get(index).getFullNames());
        tvSurname.setText(AppClass.appointmentList.get(index).getSurname());
        tvGrantType.setText(AppClass.appointmentList.get(index).getGrantType());
        tvStation.setText(AppClass.appointmentList.get(index).getNearestSassaStation());
        tvDate.setText(AppClass.appointmentList.get(index).getBookDate());

        tvTime.setText(AppClass.appointmentList.get(index).getTime());
        tvCounter.setText(AppClass.appointmentList.get(index).getCounterNumber());
        tvQueue.setText(AppClass.appointmentList.get(index).getQueueNumber());

        btnGoBack.setOnClickListener(view -> {
            AppointmentInfoActivity.this.finish();;
        });
    }
}