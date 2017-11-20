package kore.ntnu.no.safespace.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.adapters.ProjectSpinnerAdapter;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.service.LocationService;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.tasks.SendReportTask;
import kore.ntnu.no.safespace.utils.ImageUtils;

public class ReportActivity extends AppCompatActivity {

    public static final String PICTURE_ID = "kore.ntnu.safespace.PICTURE_ID";
    public static final int TAKE_PICTURE_REQUEST = 1;
    private File imageFile;
    private RecyclerView imageDisplay;
    private ImageDisplayAdapter adapter;
    private ProjectSpinnerAdapter dropDownAdapter;
    private Project selectedProject = null;
    private SharedPreferences prefs;
    private BroadcastReceiver broadcastReceiver;
    private TextView getLocationView;
    private Button getLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText reportHeader = findViewById(R.id.reportHeaderText);
        imageDisplay = findViewById(R.id.reportTakenPhoto);
        adapter = new ImageDisplayAdapter(this);
        imageDisplay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageDisplay.setAdapter(adapter);
        ImageButton capturePhoto = findViewById(R.id.takePhotoBtn);
        EditText reportDescription = findViewById(R.id.reportDescription);
        Button sendReport = findViewById(R.id.sendReportBtn);
        getLocationBtn = findViewById(R.id.getLocationBtn);
        getLocationBtn.setTag(1);
        getLocationView = findViewById(R.id.displayLocationView);

        populateSpinner();

        reportHeader.setFocusableInTouchMode(true);
        reportHeader.requestFocus();

        capturePhoto.setOnClickListener(c -> takePhoto());
        setUpSendButton(sendReport);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        float currentLatitude = prefs.getFloat("CurrentLatitude", 0);
        float currentLongitude = prefs.getFloat("CurrentLongitude", 0);

        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
        if (!runtime_permission())
            enableButtons();
    }

    private void populateSpinner() {
        Spinner dropDown = findViewById(R.id.projectSpinner);
        dropDownAdapter = new ProjectSpinnerAdapter(this, R.layout.project_spinner_item, new ArrayList<>());
        dropDown.setAdapter(dropDownAdapter);
        setSpinnerAdapterOnSelectListener(dropDown, dropDownAdapter);
        new GetAllProjectsTask((projects) -> {
            if (projects.isSuccess()) {
                dropDownAdapter.setData(projects.getResult());
            } else {
                Log.e(DocumentActivity.class.getSimpleName(), "Failed to set spinner values");
            }
        }).execute();
    }

    private void setSpinnerAdapterOnSelectListener(Spinner spinner, ProjectSpinnerAdapter adapter) {
        spinner.setOnItemSelectedListener(adapter);
        adapter.setOnSelectListener((project -> {
            this.selectedProject = project;
        }));
    }

    private void setUpSendButton(Button sendButton) {
        sendButton.setOnClickListener((view) -> {
            EditText reportHeader = findViewById(R.id.reportHeaderText);
            String title = reportHeader.getText().toString();
            EditText reportDescription = findViewById(R.id.reportDescription);
            String description = reportDescription.getText().toString();
            //Project project = new Project(1L, "", "", null);
            //List<Image> images = getImages();
            IncidentReport report = new IncidentReport(null, title, description, null, null, this.selectedProject);
            report.setImages(adapter.getImages());
            new SendReportTask((result -> {
                if (result.isSuccess()) {
                    System.out.println(result.getResult().getTitle());
                    Toast.makeText(this, "Report was successfully sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Report was NOT sent", Toast.LENGTH_SHORT).show();
                }
            })).execute(report);
        });
    }

    private List<Image> getImages() {
        // TODO Get relevant images
        List<Image> images = new ArrayList<>();
        images = ImageUtils.getStoredImages(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        return images;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            imageFile = null;
            try {
                imageFile = ImageUtils.createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Report");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "no.ntnu.kore.fileprovider",
                        imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST) {
            if (resultCode == RESULT_OK) {
                adapter.addImage(new Image(imageFile));
                imageFile = null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println(intent.getExtras().get("coordinates"));
                    Toast.makeText(context, "" + intent.getExtras().get("coordinates"), Toast.LENGTH_SHORT).show();
                    getLocationView.setText((CharSequence) intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onStart() {
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
        super.onStart();
    }

    @Override
    protected void onStop() {
        try {
            if (broadcastReceiver != null) {
                Intent intent = new Intent(ReportActivity.this, LocationService.class);
                stopService(intent);
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {}
        super.onStop();
    }

    @Override
    protected void onPause() {
        try {
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {}
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            if (broadcastReceiver != null) {
                Intent intent = new Intent(ReportActivity.this, LocationService.class);
                stopService(intent);
                unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {}
        super.onDestroy();
    }

    private boolean runtime_permission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
            return true;
        }
        return false;
    }

    private void enableButtons() {
        getLocationBtn.setOnClickListener(view -> {
            final int status = (Integer) view.getTag();
            if (status == 1) {
                Intent intent = new Intent(ReportActivity.this, LocationService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
                getLocationBtn.setText("OK");
                view.setTag(0);
            } else {
                Intent intent = new Intent(ReportActivity.this, LocationService.class);
                stopService(intent);
                getLocationBtn.setText("GET LOCATION");
                view.setTag(1);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enableButtons();
            } else {
                runtime_permission();
            }
        }
    }
}
