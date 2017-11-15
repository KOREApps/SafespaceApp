package kore.ntnu.no.safespace.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import kore.ntnu.no.safespace.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView logOut = findViewById(R.id.tv_logout);
        Button gpsBtn = findViewById(R.id.gpsBtn);
        Button nor = findViewById(R.id.bt_nor);
        Button eng = findViewById(R.id.bt_eng);

        nor.setOnClickListener(view -> {
            changeLang(this, "no");
            finish();
            startActivity(getIntent());
        });

        eng.setOnClickListener(view -> {
            changeLang(this, "en");
            finish();
            startActivity(getIntent());
        });

        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        gpsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, GPSActivity.class);
            startActivity(intent);
        });

    }

    public static void changeLang(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
