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

public class RegisterUserActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etFullNames,etSurname,etIdNumber,etEmail,etAge,etPhone,etConfirmPass,etPass;

    Button btnSubmit,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        etFullNames = findViewById(R.id.etFullNames);
        etSurname = findViewById(R.id.etSurname);
        etIdNumber = findViewById(R.id.etIdNumber);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        String title = "Register";
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);

        btnCancel.setOnClickListener(view -> RegisterUserActivity.this.finish());

        btnSubmit.setOnClickListener(view -> {

            String name = etFullNames.getText().toString().trim();
            String surname = etSurname.getText().toString().trim();
            String IdNumber = etIdNumber.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPass.getText().toString().trim();
            String rePassword = etConfirmPass.getText().toString().trim();

            if (name.isEmpty() || surname.isEmpty() || IdNumber.isEmpty()
                    || email.isEmpty() || age.isEmpty() || phone.isEmpty()
                    || password.isEmpty() || rePassword.isEmpty())
            {
                Toast.makeText(RegisterUserActivity.this,
                        "Enter all fields", Toast.LENGTH_SHORT).show();
            }
            else {
                if (AppClass.isValidPhoneNumber(phone)) {
                    if (password.equals(rePassword)) {
                        if (AppClass.isValidPassword(password)) {
                            if (AppClass.isValidIdNumber(IdNumber)) {
                                BackendlessUser user = new BackendlessUser();
                                user.setEmail(email);
                                user.setPassword(password);
                                user.setProperty("fullnames", name);
                                user.setProperty("surname", surname);
                                user.setProperty("idNo", IdNumber);
                                user.setProperty("age", age);
                                user.setProperty("phone", phone);
                                user.setProperty("Role", "User");

                                String busy = "Busy registering ..please wait..";
                                tvLoad.setText(busy);
                                showProgress(true);
                                tvLoad.setText(busy);
                                Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser response) {

                                        showProgress(false);
                                        Toast.makeText(RegisterUserActivity.this,
                                                response.getEmail() + " successfully registered", Toast.LENGTH_SHORT).show();

                                        RegisterUserActivity.this.finish();
                                        startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        showProgress(false);
                                        Toast.makeText(RegisterUserActivity.this,
                                                "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterUserActivity.this,
                                        "Invalid ID number", Toast.LENGTH_SHORT).show();
                                etIdNumber.setError("Please enter a valid ID number");
                            }
                        } else {
                            Toast.makeText(RegisterUserActivity.this,
                                    "Password must meet complexity requirements", Toast.LENGTH_SHORT).show();
                            etPass.setError("Enter valid password");
                            etConfirmPass.setError("Enter valid password");
                        }
                    } else {
                        Toast.makeText(RegisterUserActivity.this,
                                "Password do not match", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterUserActivity.this,
                            "Phone number is invalid", Toast.LENGTH_SHORT).show();
                    etPhone.setError("Enter valid phone number");
                }
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