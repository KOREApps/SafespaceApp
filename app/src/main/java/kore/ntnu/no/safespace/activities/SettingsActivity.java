package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kore.ntnu.no.safespace.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView logOut = findViewById(R.id.tv_logout);
        Button gpsBtn = findViewById(R.id.gpsBtn);

        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        gpsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, GPSActivity.class);
            startActivity(intent);
        });
    }
}
