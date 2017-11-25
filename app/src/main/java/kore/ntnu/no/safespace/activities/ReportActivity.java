package kore.ntnu.no.safespace.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.adapters.ProjectSpinnerAdapter;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.data.Location;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.tasks.GetCurrentLocationTask;
import kore.ntnu.no.safespace.tasks.GetLocationTask;
import kore.ntnu.no.safespace.tasks.GetNearestLocationTask;
import kore.ntnu.no.safespace.tasks.RegisterNewLocationTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.ImageUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;
import kore.ntnu.no.safespace.utils.dialogs.ErrorDialog;

/**
 * Class description..
 *
 * @author x
 */
public class ReportActivity extends AppCompatActivity {

    private File imageFile;
    private ImageDisplayAdapter adapter;
    private ProjectSpinnerAdapter dropDownAdapter;
    private Project selectedProject = null;
    private TextView getLocationView;
    private ImageButton sendReport;
    private Location currentLocation = null;
    private CircularProgressButton getLocationBtn;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText reportHeader = findViewById(R.id.reportHeaderText);
        RecyclerView imageDisplay = findViewById(R.id.reportTakenPhoto);
        adapter = new ImageDisplayAdapter(this);
        imageDisplay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageDisplay.setAdapter(adapter);
        ImageButton capturePhoto = findViewById(R.id.takePhotoBtn);
        EditText reportDescription = findViewById(R.id.reportDescription);
        sendReport = findViewById(R.id.sendReportBtn);
        getLocationBtn = findViewById(R.id.getLocationBtn);
        getLocationBtn.setTag(1);
        getLocationView = findViewById(R.id.displayLocationView);

        adapter.setOnHoldListener(position -> displayImageOptions(adapter.getImage(position)));
        adapter.setOnClickListener(position -> openImage(adapter.getImage(position)));

        populateSpinner();

        reportHeader.setFocusableInTouchMode(true);
        reportHeader.requestFocus();

        capturePhoto.setOnClickListener(c -> takePhoto());
        setUpSendButton(sendReport);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        float currentLatitude = prefs.getFloat("CurrentLatitude", 0);
        float currentLongitude = prefs.getFloat("CurrentLongitude", 0);

        if (!runtime_permission()) {
            //getLocationButton();
            getCurrentLocationButton();
            //getNearestLocationButton();
        }
    }

    private void populateSpinner() {
        Spinner dropDown = findViewById(R.id.projectSpinner);
        dropDownAdapter = new ProjectSpinnerAdapter(this, R.layout.project_spinner_item, new ArrayList<>());
        dropDown.setAdapter(dropDownAdapter);
        setSpinnerAdapterOnSelectListener(dropDown, dropDownAdapter);
        if(ConnectionUtil.isConnected(this)) {
            new GetAllProjectsTask((projects) -> {
                if (projects.isSuccess()) {
                    dropDownAdapter.setData(projects.getResult());
                } else {
                    Log.e(DocumentActivity.class.getSimpleName(), "Failed to set spinner values");
                }
                checkProjects();
            }).execute();
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = prefs.getString(IdUtils.PROJECTS, gson.toJson(Collections.EMPTY_LIST));
            List<Project> projects = gson.fromJson(json, new TypeToken<List<Project>>(){}.getType());
            dropDownAdapter.setData(projects);
            checkProjects();
        }
    }

    private void checkProjects() {
        if(dropDownAdapter.getCount() == 0){
            ErrorDialog.showReportErrorDialog(this);
            sendReport.setEnabled(false);
        }
    }

    private void displayImageOptions(Image image) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Remove image");
        alertDialog.setMessage("Do you wish to remove the image from the incident report?");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which)-> {adapter.removeImage(image); StorageUtils.deleteImage(image);});
        alertDialog.show();
    }

    private void openImage(Image image){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("file://" + image.getImageFile().getAbsolutePath());
        intent.setDataAndType(data, "image/*");
        startActivity(intent);
    }

    private void setSpinnerAdapterOnSelectListener(Spinner spinner, ProjectSpinnerAdapter adapter) {
        spinner.setOnItemSelectedListener(adapter);
        adapter.setOnSelectListener((project -> {
            this.selectedProject = project;
        }));
    }

    private void setUpSendButton(ImageButton sendButton) {
        sendButton.setOnClickListener((view) -> {
            EditText reportHeader = findViewById(R.id.reportHeaderText);
            String title = reportHeader.getText().toString();
            EditText reportDescription = findViewById(R.id.reportDescription);
            String description = reportDescription.getText().toString();
            //Project project = new Project(1L, "", "", null);
            //List<Image> images = getImages();
            IncidentReport report = new IncidentReport(null, title, description, adapter.getImages(), null, this.selectedProject);
                report.setLocation(currentLocation);

            try {
                StorageUtils.saveReportToFile(report, getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, DisplayReportActivity.class);
            intent.putExtra(IdUtils.REPORT, report);
            startActivity(intent);
            finish();
        });
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
                startActivityForResult(takePictureIntent, IdUtils.TAKE_PICTURE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IdUtils.TAKE_PICTURE_REQUEST) {
            if (resultCode == RESULT_OK) {
                adapter.addImage(new Image(imageFile));
                imageFile = null;
            }
        }
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

    private void getLocationButton() {
        getLocationBtn.setOnClickListener(view -> {
            getLocationBtn.startAnimation();
            new GetLocationTask((result -> {
                getLocationBtn.doneLoadingAnimation(Color.parseColor("#D6D7D7"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_complete_symbol));
                handler.postDelayed(() -> getLocationBtn.revertAnimation(), 2500);
                getLocationView.setText("");
                getLocationView.append("Latitude: " + result.getResult().getLatitude() + "\nLongitude: " + result.getResult().getLongitude());
            })).execute();
        });
    }

    private void getCurrentLocationButton() {
        getLocationBtn.setOnClickListener(view -> {
            getLocationBtn.startAnimation();
            new GetLocationTask((result1 -> {
                getLocationBtn.doneLoadingAnimation(Color.parseColor("#D6D7D7"), BitmapFactory.decodeResource(getResources(), R.drawable.ic_complete_symbol));
                handler.postDelayed(() -> getLocationBtn.revertAnimation(), 6000);
                getLocationView.setText("");
                getLocationView.append("Location: " + result1.getResult().getLatitude() + "\nLongitude: " + result1.getResult().getLongitude());
                Location location = new Location(result1.getResult().getLatitude(), result1.getResult().getLongitude(), result1.getResult().getAccuracy());
                currentLocation = location;
                new GetCurrentLocationTask(( result -> {
                    if(result.isSuccess()) {
                        if(result.getResult() != null) {
                            String name = result.getResult().getName();
                            getLocationView.setText("");
                            getLocationView.append("Location: " + name);
                        }
                    }
                })).execute(location);
            })).execute();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //getNearestLocationButton();
            } else {
                runtime_permission();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                handler.removeCallbacksAndMessages(null);
                getLocationBtn.revertAnimation();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  private KnownLocation getCurrentLocation() {
        double latitude;
        double longitude;
        new GetLocationTask((result -> {
           // latitude = result.getResult().getLatitude();
            //longitude = result.getResult().getLongitude();
        })).execute();

        return new KnownLocation(null,"test", latitude, longitude, null);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLocationBtn.revertAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getLocationBtn.revertAnimation();
    }

}
