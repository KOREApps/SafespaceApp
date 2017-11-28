package kore.ntnu.no.safespace.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Locale;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.utils.ApplicationContext;


/**
 * The purpose if this activity is to display the different kind of settings/options the application has.
 *
 * @author x
 */
public class SettingsActivity extends AppCompatActivity {

    private Button gpsBtn;
    private Button mapsBtn;
    private Button nor;
    private Button eng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        gpsBtn = findViewById(R.id.gpsBtn);
        mapsBtn = findViewById(R.id.mapsBtn);
        nor = findViewById(R.id.bt_nor);
        eng = findViewById(R.id.bt_eng);

        setUpSettingsButtons();

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
            logOutDialog();

        }
        if(id == R.id.action_GPS_settings) {
            Intent intent = new Intent(SettingsActivity.this, GPSActivity.class);
            startActivity(intent);
        }
        if(id == android.R.id.home) {
            finish();
        }
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpSettingsButtons() {
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


    public void logOutDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(ApplicationContext.getContext(), MainActivity.class);
                    ApplicationContext.getContext().startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }
}
