package com.techmeet.mercari.LoginRegistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.techmeet.mercari.R;
import com.techmeet.mercari.Utils.Constants;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView signIn, register;
    EditText mobileNumber;
    private final String numberPatternRegex = "[0-9]+";
    String verificationCode = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _init();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check Mobile Number and Send OTP
                SendOtp();
            }
        });

    }

    private void SendOtp() {
        String number = mobileNumber.getText().toString().trim();
        if (number.length() != 10) {
            Toast.makeText(getApplicationContext(), "Enter a valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        Pattern numberPattern = Pattern.compile(numberPatternRegex);
        Matcher matcher = numberPattern.matcher(number);

        if (!matcher.matches()) {
            Toast.makeText(getApplicationContext(), "Please enter numeric value only", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // TODO : Check from API whether user exist or not and then send OTP

        String prefixedPhoneNumber = "+91" + number;
        FirebaseAuth auth = FirebaseAuth.getInstance();

        PhoneAuthOptions options = PhoneAuthOptions
                .newBuilder(auth)
                .setPhoneNumber(prefixedPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(LoginActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode = s;
                        progressDialog.dismiss();

                        // Move to Otp enter activity to enter Otp
                        Intent otpIntent = new Intent(LoginActivity.this, OtpFillActivity.class);
                        otpIntent.putExtra(Constants.PREFIXED_MOBILE_NUMBER, prefixedPhoneNumber);
                        otpIntent.putExtra(Constants.VERIFICATION_ID, verificationCode);
                        startActivity(otpIntent);
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void _init() {
        signIn = findViewById(R.id.login_sign_in);
        register = findViewById(R.id.login_register);
        mobileNumber = findViewById(R.id.login_mobile_number);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Sending Otp");
        progressDialog.setMessage("Please wait...");
    }
}