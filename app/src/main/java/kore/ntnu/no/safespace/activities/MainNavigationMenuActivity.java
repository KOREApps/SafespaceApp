package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.User;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * This activity serves as a navigation menu and defines how the user navigates around the application.
 *
 * @author KORE
 */
public class MainNavigationMenuActivity extends AppCompatActivity {

    private Button reportBtn;
    private Button documentBtn;
    private Button projectBtn;
    private Button latestRepBtn;
    private Button helpBtn;
    private Button settingsBtn;
    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_menu);

        if (IdUtils.CURRENT_USER == null) {
            IdUtils.CURRENT_USER = (User) getIntent().getSerializableExtra(IdUtils.USER);
        }

        reportBtn = findViewById(R.id.rapportBtn);
        documentBtn = findViewById(R.id.dokumBtn);
        projectBtn = findViewById(R.id.projectBtn);
        latestRepBtn = findViewById(R.id.latestBtn);
        helpBtn = findViewById(R.id.helpBtn);
        settingsBtn = findViewById(R.id.settingsBtn);

        getProjects();
        setUpNavigationButtons();
        // buttonDrawableResizer();

    }

    private void getProjects() {
        if (ConnectionUtil.isConnected(this)) {
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
        getMenuInflater().inflate(R.menu.file_progress, menu);
        optionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logOutDialog();
        }
        if (id == R.id.action_maps) {
            Intent intent = new Intent(MainNavigationMenuActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_GPS) {
            Intent intent = new Intent(MainNavigationMenuActivity.this, GPSActivity.class);
            startActivity(intent);
        }
        if (id == R.id.file_upload_thing) {
            int documentsNum = StorageUtils.getDocumentations(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).size();
            int incidentNum = StorageUtils.getIncidents(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).size();
            int total = documentsNum + incidentNum;
            if (total == 0) {
                Toast.makeText(getApplicationContext(), "All files has been sent!", Toast.LENGTH_LONG).show();
                //optionsMenu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_done_white_48dp));
            } else {
                Toast.makeText(getApplicationContext(), "Documents not sent: " + documentsNum + "\nReports not sent: " + incidentNum, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
        startActivity(getIntent());
    }

    public void logOutDialog() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout")
                .setMessage("Are you sure you want to exit the application.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(MainNavigationMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void setUpNavigationButtons() {
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
            //startActivity(intent);
        });
    }

    /**
     * This will resize the icon on the buttons
     * Height is set by = left - right
     * Width is set by = top - bottom
     * setBounds(0,0,190,190) -> Height = 190
     */
    private void buttonDrawableResizer() {
        Drawable drawable1 = getApplicationContext().getResources().getDrawable(R.drawable.ic_report_problem);
        drawable1.setBounds(0, 0, 190, 190);
        reportBtn.setCompoundDrawables(null, drawable1, null, null);

        Drawable drawable2 = getApplicationContext().getResources().getDrawable(R.drawable.ic_document);
        drawable2.setBounds(0, 0, 190, 190);
        documentBtn.setCompoundDrawables(null, drawable2, null, null);

        Drawable drawable3 = getApplicationContext().getResources().getDrawable(R.drawable.ic_project_assignment);
        drawable3.setBounds(0, 0, 190, 190);
        projectBtn.setCompoundDrawables(null, drawable3, null, null);

        Drawable drawable4 = getApplicationContext().getResources().getDrawable(R.drawable.ic_file_download);
        drawable4.setBounds(0, 0, 190, 190);
        latestRepBtn.setCompoundDrawables(null, drawable4, null, null);

        Drawable drawable5 = getApplicationContext().getResources().getDrawable(R.drawable.ic_bug_report);
        drawable5.setBounds(0, 0, 190, 190);
        helpBtn.setCompoundDrawables(null, drawable5, null, null);

        Drawable drawable6 = getApplicationContext().getResources().getDrawable(R.drawable.ic_settings_art);
        drawable6.setBounds(0, 0, 190, 190);
        settingsBtn.setCompoundDrawables(null, drawable6, null, null);
    }

}
