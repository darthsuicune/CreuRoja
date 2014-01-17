package com.cruzroja.android.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.cruzroja.android.R;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
    }
}
