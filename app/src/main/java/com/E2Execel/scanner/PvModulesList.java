package com.E2Execel.scanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.result_details.Data;
import com.E2Execel.scanner.Pojo.result_details.Pvmodule;
import com.E2Execel.scanner.Pojo.result_details.Result;
import com.E2Execel.scanner.Pojo.search_details.Datum;
import com.E2Execel.scanner.Retrofit.ApiService;
import com.E2Execel.scanner.Retrofit.RetroClient;
import com.E2Execel.scanner.global.globalValues;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.E2Execel.scanner.LoginActivity.Build_alert_dialog;

public class PvModulesList extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListViewAdapter adapter;        //this is my custom list view adapter created in this file.
    private ApiService api;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private List<Pvmodule> pvmoduleList;
    private ListView pvmoduleListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pv_modules_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_layout_action_bar_skip_button);
        View view = getSupportActionBar().getCustomView();


        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button skip_button = view.findViewById(R.id.action_bar_skip);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PvModulesList.this, Pump.class));
            }
        });


        // Set up progress before call
        progressDialog = new ProgressDialog(PvModulesList.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        api = RetroClient.getApiService();
        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();


        Call<Result> call = api.getResultsJson(globalValues.APIKEY, "Token " + pref.getString("token", null), globalValues.getID());
        progressDialog.show();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    Data data = response.body().getData();

                    globalValues.setPvmodule(data.getPvmodule());
                    pvmoduleList = data.getPvmodule();


                    pvmoduleListview = findViewById(R.id.pvmoduleList);
                    adapter = new ListViewAdapter(PvModulesList.this, pvmoduleList);
                    pvmoduleListview.setAdapter(adapter);


                } else {
                    update_token();

                    Toast.makeText(PvModulesList.this, "response not received", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        /* String status = jObjError.getString("detail");
                         */
                        Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();

                        //Build_alert_dialog(getApplicationContext(), "Error", status);

                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PvModulesList.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });


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
                   // Toast.makeText(PvModulesList.this, "new token: " + "token " + response.body().getData().getToken(), Toast.LENGTH_SHORT).show();
                    editor.putString("token", response.body().getData().getToken());
                    editor.commit();
                    Toast.makeText(PvModulesList.this, "Please try to restrat the application again", Toast.LENGTH_SHORT).show();


                } else {
                    //but but i can access the error body here.,
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String status = jObjError.getString("message");
                        String error_msg = jObjError.getJSONObject("data").getString("errors");
                        Build_alert_dialog(getApplicationContext(), status, error_msg);

                    } catch (Exception e) {
                       // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressDialog.dismiss();
                Build_alert_dialog(PvModulesList.this, "Connection Error", "Failed to reach to the server");
            }
        });


    }

    public void add_new_pvmodule(View view) {
        //to add new pv modulde, no need to send any data load the fresh empty pv module activity
        startActivity(new Intent(PvModulesList.this, Pvmodules.class));
    }

    class ListViewAdapter extends BaseAdapter {

        private List<Pvmodule> pvmodule;
        private Context context;

        public ListViewAdapter(Context context, List<Pvmodule> pvmodule) {
            this.pvmodule = pvmodule;
            this.context = context;

        }

        @Override
        public int getCount() {
            return pvmodule.size();
        }

        @Override
        public Object getItem(int i) {
            return pvmodule.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.pvmodule_list_row, viewGroup, false);
            }

            ImageView pv_imageview = view.findViewById(R.id.pv_module_image);
            TextView pv_srno_textview = view.findViewById(R.id.pv_module_srno);

            final Pvmodule thisPvmodule = pvmodule.get(i);


            Glide.with(PvModulesList.this).load(globalValues.IP + thisPvmodule.getImage()).into(pv_imageview);
            pv_srno_textview.setText(thisPvmodule.getSrno());        //SrNo of clicked PV module


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context, "id of clicked PV module: " + thisPvmodule.getId(), Toast.LENGTH_LONG).show();

                    String ID = String.valueOf(thisPvmodule.getId());       //ID of clicked Pv module
                    //globalValues.setID(thisPvmodule.getId().toString());
                    Intent i = new Intent(PvModulesList.this, Pvmodules.class);
                    i.putExtra("id", ID);
                    i.putExtra("pvmodulesrno", thisPvmodule.getSrno());
                    i.putExtra("pvmoduleimage", thisPvmodule.getImage());
                    startActivity(i);

                }
            });

            return view;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Call<Result> call = api.getResultsJson(globalValues.APIKEY, "Token " + pref.getString("token", null), globalValues.getID());
        progressDialog.show();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    Data data = response.body().getData();

                    globalValues.setPvmodule(data.getPvmodule());
                    pvmoduleList = data.getPvmodule();


                    pvmoduleListview = findViewById(R.id.pvmoduleList);
                    adapter = new ListViewAdapter(PvModulesList.this, pvmoduleList);
                    pvmoduleListview.setAdapter(adapter);


                } else {
                    update_token();

                   // Toast.makeText(PvModulesList.this, "response not received", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        /* String status = jObjError.getString("detail");
                         */
                        Toast.makeText(getApplicationContext(), jObjError.toString(), Toast.LENGTH_LONG).show();

                        //Build_alert_dialog(getApplicationContext(), "Error", status);

                    } catch (Exception e) {
                       // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PvModulesList.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
