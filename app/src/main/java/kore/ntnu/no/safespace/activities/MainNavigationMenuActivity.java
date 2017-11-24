package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.gson.Gson;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.tasks.InternetConnectionThread;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

public class MainNavigationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_menu);
        if(IdUtils.CURRENT_USER == null) {
            IdUtils.CURRENT_USER = (User) getIntent().getSerializableExtra(IdUtils.USER);
        }

        Button reportBtn = findViewById(R.id.rapportBtn);
        Button documentBtn = findViewById(R.id.dokumBtn);
        Button projectBtn = findViewById(R.id.projectBtn);
        Button latestRepBtn = findViewById(R.id.latestBtn);
        Button helpBtn = findViewById(R.id.helpBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);
        new InternetConnectionThread(this);

        getProjects();

        reportBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        documentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, DocumentActivity.class);
            startActivity(intent);
        });

        projectBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, ProjectActivity.class);
            startActivity(intent);
        });

        latestRepBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, LatestReportActivity.class);
            startActivity(intent);
        });

        helpBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainNavigationMenuActivity.this, SettingsActivity.class);
            startActivityForResult(intent, IdUtils.REQUEST_CODE);
        });

    }

    private void getProjects() {
        if(ConnectionUtil.isConnected(this)){
            new GetAllProjectsTask((projects) -> {
                if (projects.isSuccess()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(projects.getResult());

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(IdUtils.PROJECTS, json);
                    editor.apply();
                } else {
                    Log.e(DocumentActivity.class.getSimpleName(), "Failed to set spinner values");
                }
            }).execute();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        StorageUtils.deleteTempImages();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            Intent intent = new Intent(MainNavigationMenuActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_settings) {
            Intent intent = new Intent(MainNavigationMenuActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_GPS) {
            Intent intent = new Intent (MainNavigationMenuActivity.this, GPSActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        startActivity(getIntent());
    }


}
