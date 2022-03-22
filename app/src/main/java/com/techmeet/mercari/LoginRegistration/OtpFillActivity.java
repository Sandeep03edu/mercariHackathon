package com.techmeet.mercari.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.techmeet.mercari.R;
import com.techmeet.mercari.Utils.Constants;

public class OtpFillActivity extends AppCompatActivity {

    int type = -1;
    String prefixedMobileNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_fill);

        if(getIntent()==null || !getIntent().hasExtra(Constants.PREFIXED_MOBILE_NUMBER)){
            return;
        }

        if(getIntent().hasExtra(Constants.VERIFICATION_ID)){
            type = Constants.REGISTRATION_TYPE;
        }
        else{
            type = Constants.LOGIN_TYPE;
        }


    }
}