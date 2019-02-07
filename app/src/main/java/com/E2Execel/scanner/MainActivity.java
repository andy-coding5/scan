package com.E2Execel.scanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.search_details.Datum;
import com.E2Execel.scanner.Pojo.search_details.Search;
import com.E2Execel.scanner.Retrofit.ApiService;
import com.E2Execel.scanner.Retrofit.RetroClient;
import com.E2Execel.scanner.global.globalValues;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText serial_num_edittext;
    private ApiService api;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);

        serial_num_edittext = findViewById(R.id.serial_number);
        api = RetroClient.getApiService();

        // Set up progress before call
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


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
                            Intent i = new Intent(MainActivity.this, SearchReaults.class);
                            i.putExtra("datum_list", (Serializable) list);
                            startActivity(i);

                        } else {
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
}
