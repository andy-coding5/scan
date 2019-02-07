package com.E2Execel.scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class LoginActivity extends AppCompatActivity {


    private EditText email_edittext, pass_edittext;
    private String email_input, password_input;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        email_edittext = findViewById(R.id.input_email);
        pass_edittext = findViewById(R.id.input_password);



        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();

    }


    public void login_process(View view) {
        email_input = email_edittext.getText().toString().trim();
        password_input = pass_edittext.getText().toString().trim();

    }
}
