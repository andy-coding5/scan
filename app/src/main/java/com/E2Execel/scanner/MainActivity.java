package com.E2Execel.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.search_details.Datum;
import com.E2Execel.scanner.Pojo.search_details.Search;
import com.E2Execel.scanner.Retrofit.ApiService;
import com.E2Execel.scanner.Retrofit.RetroClient;
import com.E2Execel.scanner.global.globalValues;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.E2Execel.scanner.LoginActivity.Build_alert_dialog;

public class MainActivity extends AppCompatActivity {

    private EditText serial_num_edittext;
    private ApiService api;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setCustomView(R.layout.custom_action_bar_logout_layout);
        View view = getSupportActionBar().getCustomView();

        Button logout_button = view.findViewById(R.id.action_bar_logout);


        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);


                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to Logout?");


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


        serial_num_edittext = findViewById(R.id.serial_number);
        api = RetroClient.getApiService();

        // Set up progress before call
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        update_token();


    }


    public void search_process(View view) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String serial_num = serial_num_edittext.getText().toString();

        if (serial_num.equals("")) {
            LoginActivity.Build_alert_dialog(MainActivity.this, "Input Error", "Please Enter The Value of Serial Number");
        } else {
            Toast.makeText(this, serial_num, Toast.LENGTH_SHORT).show();
            //call API of search

            Call<Search> call = api.getSearchJason(globalValues.APIKEY, "Token " + pref.getString("token", null), serial_num, "Android");
            progressDialog.show();

            call.enqueue(new Callback<Search>() {
                @Override
                public void onResponse(Call<Search> call, Response<Search> response) {

                    progressDialog.dismiss();

                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("Success")) {
                            //start search result activity
                            ArrayList<Datum> list = (ArrayList<Datum>) response.body().getData();
                            Intent i = new Intent(MainActivity.this, SearchResults.class);
                            i.putExtra("datum_list", (Serializable) list);
                            startActivity(i);

                        } else {
                            update_token();
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                String status = jObjError.getString("message");
                                String error_msg = jObjError.getJSONObject("data").getString("errors");
                                LoginActivity.Build_alert_dialog(MainActivity.this, status, error_msg);


                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        update_token();
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String status = jObjError.getString("message");
                            String error_msg = jObjError.getJSONObject("data").getString("errors");
                            LoginActivity.Build_alert_dialog(MainActivity.this, status, error_msg);


                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Search> call, Throwable t) {

                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });


        }
    }

    public void update_token() {
        //pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //Toast.makeText(this, "email from pref: " + pref.getString("email", "not fetched from pref"), Toast.LENGTH_SHORT).show();
        ApiService api = RetroClient.getApiService();


        Call<Login> call = api.getLoginJason(pref.getString("email", null), pref.getString("password", null), "Android");

        progressDialog.show();

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "new token: " + "token " + response.body().getData().getToken(), Toast.LENGTH_SHORT).show();
                    editor.putString("token", response.body().getData().getToken());
                    editor.commit();

                } else {
                    //but but i can access the error body here.,
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String status = jObjError.getString("message");
                        String error_msg = jObjError.getJSONObject("data").getString("errors");
                        Build_alert_dialog(getApplicationContext(), status, error_msg);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressDialog.dismiss();
                Build_alert_dialog(MainActivity.this, "Connection Error", "Please Check You Internet Connection");
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
