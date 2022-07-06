package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        String title = "Login";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        etUsername.setText("");
        etPassword.setText("");

        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
            else
            {
                String busy = "Busy logging you in please wait...";
                tvLoad.setText(busy);
                showProgress(true);

                Backendless.UserService.login(username, password, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {

                        AppClass.user = response;
                        showProgress(false);

                        String role = AppClass.user.getProperty("Role").toString().trim();

                        if ("Administrator".equals(role))
                        {
                            startActivity(new Intent(LoginActivity.this, AdministratorActivity.class));
                        }
                        else
                        {
                             startActivity(new Intent(LoginActivity.this, ManageMyDataActivity.class));
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault)
                    {
                        showProgress(false);
                        Toast.makeText(LoginActivity.this, "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },true);
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