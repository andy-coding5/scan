package com.E2Execel.scanner;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.E2Execel.scanner.Pojo.search_details.Datum;

import java.util.ArrayList;
import java.util.List;

public class SearchReaults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_reaults);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button


        //get intent from main activity
        Intent i = getIntent();
        ArrayList list = (ArrayList<Datum>) i.getSerializableExtra("datum_list");
        Toast.makeText(this, "list successfully", Toast.LENGTH_SHORT).show();



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
