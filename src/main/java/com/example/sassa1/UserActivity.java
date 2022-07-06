package com.example.sassa1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;

public class UserActivity extends AppCompatActivity {


    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private EditText etFullNames,etSurname,etIdNumber,etAddress,etNearestStation,etRequest
            ,etBookingNumber;
    private Spinner spGrantType,spGrantFor;
    private DatePicker dpAppDate;

    Button btnCancel, btnSubmit;

    String [] grantTypeArray = {"Please select","Older Persons", "Disability","War Veterans",
            "Care dependency","Foster Child","Child support"};
    String [] grantForArray = {"Please select","My Self","For someone else"};
    String [] monthsArray = {"January", "February","March","April","May","June","July","August",
    "September","October","November","December"};

    String resultsGrantType;
    String resultsGrantFor;
    int posGrantType;
    int postGrantFor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        etSurname = findViewById(R.id.etSurname);
        etFullNames = findViewById(R.id.etFullNames);
        etIdNumber = findViewById(R.id.iDNumber);
        etAddress = findViewById(R.id.etAddress);
        etNearestStation = findViewById(R.id.etNearestStation);

        spGrantType = findViewById(R.id.spGrantType);
        spGrantFor = findViewById(R.id.spGrantFor);
        dpAppDate = findViewById(R.id.dpAppDate);
        etRequest = findViewById(R.id.etRequest);
        etBookingNumber = findViewById(R.id.etBookingNumber);

        String title = "Book appointments";
        String subTitle =AppClass.getInitials( AppClass.user.getProperty("fullnames").toString()) + " " +
                AppClass.user.getProperty("surname").toString();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setSubtitle(subTitle);

        ArrayAdapter grantTypeAdapter = new ArrayAdapter(UserActivity.this,
                android.R.layout.simple_spinner_item,grantTypeArray);
        grantTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrantType.setAdapter(grantTypeAdapter);

        spGrantType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                resultsGrantType = grantTypeArray[i];
                posGrantType = i;
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter grantForAdapter = new ArrayAdapter(UserActivity.this,
                android.R.layout.simple_spinner_item,grantForArray);
        grantForAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrantFor.setAdapter(grantForAdapter);
        spGrantFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                resultsGrantFor = grantForArray[i];
                postGrantFor = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etFullNames.setText(AppClass.user.getProperty("fullnames").toString());
        etSurname.setText(AppClass.user.getProperty("surname").toString());
        etIdNumber.setText(AppClass.user.getProperty("idNo").toString());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserActivity.this.finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String address = etAddress.getText().toString().trim();
                String nearestSassaStation = etNearestStation.getText().toString().trim();
                String otherRequest = etRequest.getText().toString().trim();
                String bookingNumber = etBookingNumber.getText().toString().trim();

                mLoginFormView = findViewById(R.id.login_form);
                mProgressView = findViewById(R.id.login_progress);
                tvLoad = findViewById(R.id.tvLoad);

                int day = dpAppDate.getDayOfMonth();
                int month = dpAppDate.getMonth();
                int year = dpAppDate.getYear();

                String date = day + " " +  monthsArray[month] + " " + year;

                if(address.isEmpty()|| nearestSassaStation.isEmpty() || otherRequest.isEmpty())
                {
                    Toast.makeText(UserActivity.this, "" +
                            "Enter all fields", Toast.LENGTH_SHORT).show();
                }
                else {

                    if (posGrantType == 0)
                    {

                        Toast.makeText(UserActivity.this,
                                "Please select type of grant", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (postGrantFor != 0)
                        {
                            Appointment app1 = new Appointment();

                            app1.setFullNames(AppClass.user.getProperty("fullnames").toString()); //1
                            app1.setSurname(AppClass.user.getProperty("surname").toString()); //2
                            app1.setIdNumber(AppClass.user.getProperty("idNo").toString()); //3
                            app1.setResidentialAddress(address); //4
                            app1.setGrantType(resultsGrantType); //5
                            app1.setGrandFor(resultsGrantFor);//6
                            app1.setBookDate(date);// 7
                            app1.setOtherRequest(otherRequest); //8
                            app1.setBookingNumber(bookingNumber);//9
                            app1.setNearestSassaStation(nearestSassaStation);//10
                            app1.setCounterNumber("NULL");//11
                            app1.setTime("NULL");//12
                            app1.setQueueNumber("NULL");//13
                            app1.setEmail(AppClass.user.getEmail());


                            showProgress(true);
                            Backendless.Persistence.of(Appointment.class).save(app1,
                                    new AsyncCallback<Appointment>() {
                                @Override
                                public void handleResponse(Appointment response) {

                                    Toast.makeText(UserActivity.this,
                                            "You have successfully booked appointment",
                                            Toast.LENGTH_SHORT).show();
//**************************************************************************************************************


                                    String sb = "Please receive details of your appointment:\n" +
                                            "ID number : " + response.getIdNumber() + "\n" +
                                            "Full names : " + response.getFullNames() + "\n" +
                                            "Surname : " + response.getSurname() + "\n" +
                                            "GrantType : " + response.getGrantType() + "\n" +
                                            "Station : " + response.getNearestSassaStation() + "\n" +
                                            "Date : " + response.getBookDate() + "\n" +
                                            "You will receive an email confirming date,queue and counter number" +
                                            " once your application gets approved";
                                    Backendless.Messaging.sendTextEmail("APPOINTMENT STATUS",
                                            sb,
                                            AppClass.user.getEmail(),
                                            new AsyncCallback<MessageStatus>()
                                            {
                                        @Override
                                        public void handleResponse(MessageStatus response) {

                                            Toast.makeText(UserActivity.this,
                                                    "Sent ", Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                            UserActivity.this.finish();
                                            Intent intent = new Intent(UserActivity.this, ThankYouActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(UserActivity.this,
                                                    "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });



//**************************************************************************************************************
                                    AppClass.getInstance().sendMessageAsync("+27630068789", "TEXT MESSAGE",
                                            null, null, new AsyncCallback<Object>() {
                                                @Override
                                                public void handleResponse(Object response) {
                                                    // handle the result
                                                    Toast.makeText(UserActivity.this,
                                                            "Sent", Toast.LENGTH_SHORT).show();
                                                }
                                                @Override
                                                public void handleFault(BackendlessFault fault) {
                                                    showProgress(false);
                                                    Toast.makeText(UserActivity.this,
                                                            "Error : " + fault.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(UserActivity.this,
                                            "Error : " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgress(false);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(UserActivity.this,
                                    "Please select", Toast.LENGTH_SHORT).show();
                        }
                    }
                }//end else
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