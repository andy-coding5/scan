package com.E2Execel.scanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Retrofit.ApiService;
import com.E2Execel.scanner.Retrofit.RetroClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private EditText email_edittext, pass_edittext;
    private String email_input, password_input;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ApiService api;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();

        String LOGIN_STATUS = "true";
        if (LOGIN_STATUS.equals(pref.getString("IsUserLoggedIn", "false"))) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        email_edittext = findViewById(R.id.input_email);
        pass_edittext = findViewById(R.id.input_password);

        api = RetroClient.getApiService();

        // Set up progress before call
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }


    public void login_process(View view) {
        email_input = email_edittext.getText().toString();
        password_input = pass_edittext.getText().toString();

        //validation of null
        if (email_input.equals("") && password_input.equals("")) {

            Build_alert_dialog(LoginActivity.this, "Login Failed", "Credentials required");
        } else if (email_input.equals("") || password_input.equals("")) {
            if (email_input.equals("")) {
                Build_alert_dialog(LoginActivity.this, "Login Failed", "email required");
                //Toast.makeText(this, "Please Enter The Email Address", Toast.LENGTH_SHORT).show();
            }
            if (password_input.equals("")) {
                Build_alert_dialog(LoginActivity.this, "Login Failed", "password required");
            }
        } else {
            //define the logic to get the use details and match them with the entered details, ,,,,if matched then success  ---RV

            Call<Login> call = api.getLoginJason(email_input, password_input, "Android");

            progressDialog.show();

            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("Success")) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            //entry in local db (i.e in shared pref)

                            editor.putString("IsUserLoggedIn", "true");
                            editor.putString("token", response.body().getData().getToken());
                            editor.commit();

                            //start main activity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String status = jObjError.getString("message");
                            String error_msg = jObjError.getJSONObject("data").getString("errors");
                            Build_alert_dialog(LoginActivity.this, status, error_msg);


                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    public static void Build_alert_dialog(final Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(true);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();       //to completely close the entire application
    }
}
