package com.E2Execel.scanner;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    private TextView t_v_name, t_v_mobile, t_v_address, t_v_village, t_v_city, t_v_district, t_v_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        TextView t = view.findViewById(R.id.title);
        t.setText("Information");       //title of the screen ... in custom action bar

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.action_bar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        t_v_name = findViewById(R.id.name);
        t_v_mobile = findViewById(R.id.mobile);
        t_v_address = findViewById(R.id.address);
        t_v_village = findViewById(R.id.village);
        t_v_city = findViewById(R.id.city);
        t_v_district = findViewById(R.id.district);
        t_v_state = findViewById(R.id.state);

        Intent i = getIntent();

        String name = i.getStringExtra("name");
        String mobile = i.getStringExtra("mobile");
        String address = i.getStringExtra("address");
        String village = i.getStringExtra("village");
        String city = i.getStringExtra("city");
        String district = i.getStringExtra("district");
        String state = i.getStringExtra("state");

        t_v_name.setText(name);
        t_v_mobile.setText(mobile);
        t_v_address.setText(address);
        t_v_village.setText(village);
        t_v_city.setText(city);
        t_v_district.setText(district);
        t_v_state.setText(state);


    }

    public void add_details(View view) {
        startActivity(new Intent(Information.this,Pvmodules.class));
    }
}
