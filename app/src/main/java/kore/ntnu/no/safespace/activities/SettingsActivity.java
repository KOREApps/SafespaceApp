package kore.ntnu.no.safespace.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

import kore.ntnu.no.safespace.R;

/**
 * This class represents the settings menu.
 *
 * @author x
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView logOut = findViewById(R.id.tv_logout);
        TextView gpsBtn = findViewById(R.id.gpsBtn);
        TextView mapsBtn = findViewById(R.id.mapsBtn);
        TextView nor = findViewById(R.id.bt_nor);
        TextView eng = findViewById(R.id.bt_eng);

        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        gpsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, GPSActivity.class);
            startActivity(intent);
        });

        mapsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SettingsActivity.this, MapsActivity.class);
            startActivity(intent);
        });

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

    }

    public static void changeLang(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout_settings){
            //logOutDialog();
            // TODO Fix "You need to use a theme appcompat or descendant with this activity".
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_GPS_settings) {
            Intent intent = new Intent(SettingsActivity.this, GPSActivity.class);
            startActivity(intent);
        }
        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
