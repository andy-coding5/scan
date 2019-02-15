package com.E2Execel.scanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.login_details.Login;
import com.E2Execel.scanner.Pojo.result_details.Data;
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

public class SearchResults extends AppCompatActivity {


    private ListViewAdapter adapter;        //this is my custom list view adapter created in this file.
    private ListView search_results_listView;
    private ApiService api;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_reaults);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        editor = pref.edit();


        //get intent from main activity
        Intent i = getIntent();
        ArrayList list = (ArrayList<Datum>) i.getSerializableExtra("datum_list");
        // Toast.makeText(this, "list successfully", Toast.LENGTH_SHORT).show();


        //list view str from list_row.xml

        search_results_listView = findViewById(R.id.results_listview);
        adapter = new ListViewAdapter(this, list);
        search_results_listView.setAdapter(adapter);

        pref = getSharedPreferences("SCANNER_PREF", MODE_PRIVATE);
        api = RetroClient.getApiService();

        // Set up progress before call
        progressDialog = new ProgressDialog(SearchResults.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


    }

    class ListViewAdapter extends BaseAdapter {

        private List<Datum> datum;
        private Context context;

        public ListViewAdapter(Context context, List<Datum> datum) {
            this.datum = datum;
            this.context = context;

        }

        @Override
        public int getCount() {
            return datum.size();
        }

        @Override
        public Object getItem(int i) {
            return datum.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_raw, viewGroup, false);
            }

            TextView name_textview = view.findViewById(R.id.name);
            TextView srno_textview = view.findViewById(R.id.srno);
            ImageView im = view.findViewById(R.id.main_image);

            final Datum thisDatum = datum.get(i);

            name_textview.setText(thisDatum.getName());
            srno_textview.setText(thisDatum.getSrno());
            Glide.with(SearchResults.this).load("http://192.168.0.110:8000" + thisDatum.getPhoto()).into(im);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, thisDatum.getName(), Toast.LENGTH_LONG).show();
                    globalValues.setID(thisDatum.getId().toString());
                    startActivity(new Intent(SearchResults.this, Information.class));
                }
            });

            return view;
        }
    }


}
