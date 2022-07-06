package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
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

public class AdministratorActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    AppointMentAdapter adapter;

    ListView lvApp;
    Button btnGoBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvApp = findViewById(R.id.lvApp);
        btnGoBack  = findViewById(R.id.btnGoBack);

        lvApp.setOnItemClickListener((adapterView, view, i, l) -> {

            Intent intent = new Intent(AdministratorActivity.this, ScheduleTimesActivity.class);
            intent.putExtra("index",i);
            startActivityForResult(intent,1);
        });


        String title = "Administrator";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        btnGoBack.setOnClickListener(view -> {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(AdministratorActivity.this);
            builder1.setMessage("Do you want to log out");
            builder1.setTitle("LOGOUT ?");
            builder1.setPositiveButton("Logout", (dialogInterface, i) -> {

                showProgress(true);
                String busy = "Busy logging you out ...please wait...";
                tvLoad.setText(busy);

                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {

                        Toast.makeText(AdministratorActivity.this,
                                "Successfully logged out", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                        AdministratorActivity.this.finish();
                        startActivity(new Intent(AdministratorActivity.this, LoginActivity.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(AdministratorActivity.this,
                                "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            builder1.setNegativeButton("CANCEL", (dialogInterface, i) -> {

            });
            builder1.show();

                }
        );

        String whereClause = "bookDate != 'NULL'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        String s = "Getting all appointments please wait..";
        showProgress(true);
        tvLoad.setText(s);

        Backendless.Persistence.of(Appointment.class).find(queryBuilder, new AsyncCallback<List<Appointment>>() {
            @Override
            public void handleResponse(List<Appointment> response) {
                AppClass.appointmentList = response;
                adapter = new AppointMentAdapter(AdministratorActivity.this, response);
                lvApp.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(AdministratorActivity.this, "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });

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