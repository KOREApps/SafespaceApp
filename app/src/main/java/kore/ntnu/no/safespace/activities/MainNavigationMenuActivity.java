package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

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

    public static User getCurrentUser(){
        return currentUser;
    }
}
