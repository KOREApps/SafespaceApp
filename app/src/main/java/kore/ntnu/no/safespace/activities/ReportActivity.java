package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ProjectSpinnerAdapter;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.tasks.SendReportTask;
import kore.ntnu.no.safespace.utils.ImageUtils;

public class ReportActivity extends AppCompatActivity {

    public static final String PICTURE_ID = "kore.ntnu.safespace.PICTURE_ID";
    public static final int TAKE_PICTURE_REQUEST = 1;
    private File imageFile;
    private ImageView takenPhoto;
    private Bitmap bitmap;
    private ProjectSpinnerAdapter dropDownAdapter;
    private Project selectedProject = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText reportHeader = findViewById(R.id.reportHeaderText);
        takenPhoto = findViewById(R.id.reportTakenPhoto);
        ImageButton capturePhoto = findViewById(R.id.takePhotoBtn);
        ImageView takenPhotos = findViewById(R.id.reportTakenPhotos);
        EditText reportDescription = findViewById(R.id.reportDescription);
        Button sendReport = findViewById(R.id.sendReportBtn);

        populateSpinner();

        reportHeader.setFocusableInTouchMode(true);
        reportHeader.requestFocus();

        capturePhoto.setOnClickListener(c -> takePhoto());
        setUpSendButton(sendReport);
    }

    private void populateSpinner(){
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

    private void setSpinnerAdapterOnSelectListener(Spinner spinner, ProjectSpinnerAdapter adapter){
        spinner.setOnItemSelectedListener(adapter);
        adapter.setOnSelectListener((project -> {
            this.selectedProject = project;
        }));
    }

    private void setUpSendButton(Button sendButton){
        sendButton.setOnClickListener((view) -> {
            EditText reportHeader = findViewById(R.id.reportHeaderText);
            String title = reportHeader.getText().toString();
            EditText reportDescription = findViewById(R.id.reportDescription);
            String description = reportDescription.getText().toString();
            //Project project = new Project(1L, "", "", null);
            IncidentReport report = new IncidentReport(null, title, description, null, null, this.selectedProject);
            new SendReportTask((result -> {
                System.out.println(result.getResult().getTitle());
            })).execute(report);
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
                startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TAKE_PICTURE_REQUEST){
            if(resultCode == RESULT_OK){
                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                takenPhoto.setImageBitmap(bitmap);

            }
        }
    }
}
