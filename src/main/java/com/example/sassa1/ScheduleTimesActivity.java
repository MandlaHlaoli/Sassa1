package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;

public class ScheduleTimesActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etCounter,etQueue;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scedule_times);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        TextView tester = findViewById(R.id.tester);

        timePicker = findViewById(R.id.timePicker);
        TextView tvId = findViewById(R.id.tvId);
        TextView tvFullNames = findViewById(R.id.tvFullNames);
        TextView tvGrantType = findViewById(R.id.tvGrantType);
        TextView tvStation = findViewById(R.id.tvStation);
        TextView tvDate = findViewById(R.id.tvDate);
        etCounter = findViewById(R.id.etCounter);
         etQueue = findViewById(R.id.etQueue);
        Button btnGoBack = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        String title = "Schedule appointments";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        int index = getIntent().getIntExtra("index",0);


        String names = AppClass.appointmentList.get(index).getFullNames();
        String surname = AppClass.appointmentList.get(index).getSurname();

        String fullName = AppClass.getInitials(names) + " " + surname;

        tester.setText(AppClass.appointmentList
        .get(index).getFullNames());

        tvId.setText(AppClass.appointmentList.get(index).getIdNumber());
        tvFullNames.setText(fullName);
        tvGrantType.setText(AppClass.appointmentList.get(index).getGrantType());
        tvStation.setText(AppClass.appointmentList.get(index).getNearestSassaStation());
        tvDate.setText(AppClass.appointmentList.get(index).getBookDate());


         etCounter.setText(AppClass.appointmentList.get(index).getCounterNumber());
         etQueue.setText(AppClass.appointmentList.get(index).getQueueNumber());

         btnSave.setOnClickListener(view -> {

             String counter = etCounter.getText().toString().trim();
             String queue = etQueue.getText().toString().trim();
             int minute = timePicker.getMinute();
             int hour = timePicker.getHour();

             String time = hour + ":" + minute;

             if(counter.isEmpty()|| queue.isEmpty())
             {
                 Toast.makeText(ScheduleTimesActivity.this,
                         "Please enter all values", Toast.LENGTH_SHORT).show();
             }
             else
             {
                 AppClass.appointmentList.get(index).setTime(time);
                 AppClass.appointmentList.get(index).setCounterNumber(counter);
                 AppClass.appointmentList.get(index).setQueueNumber(queue);

                 showProgress(true);
                 String busy = "Busy booking an appointment...please wait...";
                 tvLoad.setText(busy);
                 Backendless.Persistence.of(Appointment.class).save(AppClass.appointmentList.get(index),
                         new AsyncCallback<Appointment>() {
                             @Override
                             public void handleResponse(Appointment response) {

                                 String sb = "Please receive details of your appointment:\n" +
                                         "ID number : " + response.getIdNumber() + "\n" +
                                         "Full names : " + response.getFullNames() + "\n" +
                                         "Surname : " + response.getSurname() + "\n" +
                                         "GrantType : " + response.getGrantType() + "\n" +
                                         "Station : " + response.getNearestSassaStation() + "\n" +
                                         "Date of appointment : " + response.getBookDate() + "\n" +
                                         "Time : " + response.getTime() + "\n" +
                                         "Time : " + response.getTime() + "\n" +
                                         "Counter number : " + response.getCounterNumber() + "\n" +
                                         "Queue number : " + response.getQueueNumber() + "\n" ;

                                 Backendless.Messaging.sendTextEmail("Appointment confirmation",
                                         sb, response.getEmail(),
                                         new AsyncCallback<MessageStatus>() {
                                             @Override
                                             public void handleResponse(MessageStatus response) {

                                                 Toast.makeText(ScheduleTimesActivity.this,
                                                         "sent successfully", Toast.LENGTH_SHORT).show();
                                             }

                                             @Override
                                             public void handleFault(BackendlessFault fault) {
                                                 Toast.makeText(ScheduleTimesActivity.this,
                                                         "Error : " + fault.getMessage(),
                                                         Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                 Toast.makeText(ScheduleTimesActivity.this,
                                         "Saved successfully", Toast.LENGTH_SHORT).show();
                                 ScheduleTimesActivity.this.finish();
                                 showProgress(false);
                             }

                             @Override
                             public void handleFault(BackendlessFault fault) {

                                 Toast.makeText(ScheduleTimesActivity.this,
                                         "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                 showProgress(false);
                             }
                         });
             }
         });

        btnGoBack.setOnClickListener(view -> {
            ScheduleTimesActivity.this.finish();;
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}