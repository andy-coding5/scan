package com.E2Execel.scanner;

import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.E2Execel.scanner.global.globalValues;
import com.bumptech.glide.Glide;

import java.io.Serializable;

public class Information extends AppCompatActivity {

    private TextView t_v_srno, t_v_name, t_v_mobile, t_v_aadhar, t_v_address1, t_v_address2, t_v_zip, t_v_village, t_v_city, t_v_district, t_v_state;
    private ImageView imageview_photo;
    String size ;      //if size of pv module(received from search result activity is 1 or more, then this activity will call pv module list otherwise pv module called)


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


        imageview_photo = findViewById(R.id.main_image);
        t_v_srno = findViewById(R.id.srno);
        t_v_name = findViewById(R.id.name);
        t_v_mobile = findViewById(R.id.mobile);
        t_v_aadhar = findViewById(R.id.aadhar);
        t_v_address1 = findViewById(R.id.address_line_1);
        t_v_address2 = findViewById(R.id.address_line_2);
        t_v_zip = findViewById(R.id.zip);
        t_v_village = findViewById(R.id.village);
        t_v_city = findViewById(R.id.city);
        t_v_district = findViewById(R.id.district);
        t_v_state = findViewById(R.id.state);

        Intent i = getIntent();

        size = i.getStringExtra("size");

        String srno = i.getStringExtra("srno");
        String name = i.getStringExtra("name");
        String mobile = i.getStringExtra("mobile");
        String aadhar = i.getStringExtra("aadhar");
        String photo = i.getStringExtra("photo");
        String address1 = i.getStringExtra("address1");
        String address2 = i.getStringExtra("address2");
        String zip = i.getStringExtra("zipcode");
        String village = i.getStringExtra("village");
        String city = i.getStringExtra("city");
        String district = i.getStringExtra("district");
        String state = i.getStringExtra("state");

        Glide.with(this).load("http://192.168.0.110:8000" + photo).into(imageview_photo);
        t_v_srno.setText(srno);
        t_v_name.setText(name);
        t_v_mobile.setText(mobile);
        t_v_aadhar.setText(aadhar);
        t_v_address1.setText(address1);
        t_v_address2.setText(address2);
        t_v_village.setText(village);
        t_v_city.setText(city);
        t_v_zip.setText(zip);
        t_v_district.setText(district);
        t_v_state.setText(state);


    }

    public void add_details(View view) {
        if (Integer.parseInt(size) == 0) {
            Intent i = new Intent(Information.this, Pvmodules.class);

            startActivity(i);
        } else {
            Intent i = new Intent(Information.this, PvModulesList.class);
            i.putExtra("pvmodulelist", (Serializable)globalValues.getPvmodule());

            startActivity(i);
        }

    }

    public void edit_photo(View view) {
        //camera module or galley module

    }
}
