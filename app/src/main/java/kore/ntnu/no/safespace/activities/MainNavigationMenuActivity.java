package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;

public class MainNavigationMenuActivity extends AppCompatActivity {
    private static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_menu);

        currentUser = (User) getIntent().getSerializableExtra(MainActivity.USER);
        

        Button reportBtn = findViewById(R.id.rapportBtn);
        Button documentBtn = findViewById(R.id.dokumBtn);
        Button projectBtn = findViewById(R.id.projectBtn);
        Button latestRepBtn = findViewById(R.id.latestBtn);
        Button helpBtn = findViewById(R.id.helpBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

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
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
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


        return super.onOptionsItemSelected(item);
    }

    public static User getCurrentUser(){
        return currentUser;
    }

}
