package com.example.sassa1;

import android.app.Application;

import androidx.appcompat.app.ActionBar;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppClass extends Application {

    public static final String APPLICATION_ID = "AB0709D9-45C6-6063-FFC1-E7B759B99B00";
    public static final String API_KEY = "DA16D55B-F6BA-4843-8FA6-91BBE47779E5";
    public static final String SERVER_URL =  "http://api.backendless.com";

    static final String SERVICE_NAME = "Twilio";

    public static BackendlessUser user;
    public static List<Appointment> appointmentList;

    private static AppClass ourInstance = new AppClass();


    public static AppClass getInstance()
    {
        return ourInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }

        // Function to validate the password.
        public static boolean isValidPassword(String password)
        {

            // Regex to check valid password.
            String regex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // If the password is empty
            // return false
            if (password == null) {
                return false;
            }

            // Pattern class contains matcher() method
            // to find matching between given password
            // and regular expression.
            Matcher m = p.matcher(password);

            // Return if the password
            // matched the ReGex
            return m.matches();
        }


    public static boolean isValidPhoneNumber(String mobNumber)
    {
//validates phone numbers having 10 digits (9998887776)
        if (mobNumber.matches("\\d{10}"))
            return true;
//validates phone numbers having digits, -, . or spaces
        else if (mobNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;
        else if (mobNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}"))
            return true;
//validates phone numbers having digits and extension (length 3 to 5)
        else if (mobNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
            return true;
//validates phone numbers having digits and area code in braces
        else if (mobNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;
        else if (mobNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}"))
            return true;
        else if (mobNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;
//return false if any of the input matches is not found
        else
            return false;
    }



    public static boolean isValidIdNumber(String idNumber)
    {
        String regExpression = "([0-9][0-9])((?:[0][1-9])|(?:[1][0-2]))((?:[0-2][0-9])|(?:[3][0-1]))([0-9])([0-9]{3})([0-9])([0-9])([0-9])";
        Pattern pattern = Pattern.compile(regExpression);

        Matcher matcher = pattern.matcher(idNumber);
        if (matcher.matches()) {

            int tot1 = 0;
            for (int i = 0; i < idNumber.length() - 1; i += 2) {
                tot1 = tot1 + Integer.parseInt(idNumber.substring(i, i + 1));
            }
            StringBuffer field1 = new StringBuffer("");
            for (int i = 1; i < idNumber.length(); i += 2) {
                field1.append(idNumber.substring(i, i + 1));
            }
            String evenTotStr = (Long.parseLong(field1.toString()) * 2) + "";
            int tot2 = 0;
            for (int i = 0; i < evenTotStr.length(); i++) {
                tot2 = tot2 + Integer.parseInt(evenTotStr.substring(i, i + 1));
            }
            int lastD = (10 - ((tot1 + tot2) % 10)) % 10;
            int checkD = Integer.parseInt(idNumber.substring(12, 13));

            if (checkD == lastD) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getInitials(String name) {
        StringBuilder initials = new StringBuilder();
        boolean addNext = true;
        if (name != null) {
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == ' ' || c == '-' || c == '.') {
                    addNext = true;
                } else if (addNext) {
                    initials.append(c);
                    addNext = false;
                }
            }
        }
        return initials.toString();
    }
    public void sendMessageAsync(String phoneNumber, String message, String mediaUrl, Object options, AsyncCallback<Object> callback)
    {
        Object[] args = new Object[]{phoneNumber, message, mediaUrl, options};
        Backendless.CustomService.invoke( SERVICE_NAME, "sendMessage", args, Object.class, callback);
    }

    public Object sendMessage(String phoneNumber, String message, String mediaUrl, Object options)
    {
        Object[] args = new Object[]{phoneNumber, message, mediaUrl, options};
        return Backendless.CustomService.invoke( SERVICE_NAME, "sendMessage", args, Object.class );
    }
}
