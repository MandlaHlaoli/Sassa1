package com.example.sassa1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class ListAppointmentsActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    AppointMentAdapter adapter;
    private ListView lvApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_appointments);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvApp = findViewById(R.id.lvApp);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        String title = "List of appointments";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        lvApp.setOnItemClickListener((adapterView, view, i, l) -> {

            showProgress(true);
            ListAppointmentsActivity.this.finish();
            Intent intent = new Intent(ListAppointmentsActivity.this,AppointmentInfoActivity.class);
            intent.putExtra("index",i);
            startActivityForResult(intent,1);
        });

        btnGoBack.setOnClickListener(view -> {
            showProgress(true);
            ListAppointmentsActivity.this.finish();
        });

        String whereClause = "idNumber = '" + AppClass.user.getProperty("idNo") +"'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        String s = "Getting all appointments please wait..";
        showProgress(true);
        tvLoad.setText(s);
        Backendless.Persistence.of(Appointment.class).find(queryBuilder, new AsyncCallback<List<Appointment>>() {
            @Override
            public void handleResponse(List<Appointment> response) {
                AppClass.appointmentList = response;
                adapter = new AppointMentAdapter(ListAppointmentsActivity.this, response);
                lvApp.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(ListAppointmentsActivity.this, "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 )
        {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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