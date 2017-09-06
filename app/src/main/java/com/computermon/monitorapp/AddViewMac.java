package com.computermon.monitorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddViewMac extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_mac);
        Button viewLocation = (Button) findViewById(R.id.view_all);
        viewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddViewMac.this,ListPcActivity.class));

            }
        });

        Button addKid = (Button) findViewById(R.id.add_pc);
        addKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddViewMac .this,PcActivity.class));

            }
        });
    }
}
