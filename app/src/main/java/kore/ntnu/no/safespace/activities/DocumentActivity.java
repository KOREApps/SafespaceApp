package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.adapters.ProjectSpinnerAdapter;
import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.utils.ImageUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

public class DocumentActivity extends AppCompatActivity {
    public static final int TAKE_PICTURE_REQUEST = 33;
    private RecyclerView imageDisplay;
    private TextView sender;
    private EditText title;
    private TextView description;
    private ProjectSpinnerAdapter dropDownAdapter;
    private ImageDisplayAdapter adapter;
    private List<Image> imageList;
    private File imageFile;
    private Spinner project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        imageDisplay = findViewById(R.id.docTakenPhotos);
        sender = findViewById(R.id.docSenderID);
        description = findViewById(R.id.docDescription);
        project = findViewById(R.id.docProject);
        title = findViewById(R.id.docTitle);
        sender.setText(MainNavigationMenuActivity.getCurrentUser().getFirstName());

        imageList = new ArrayList<>();

        populateSpinner();

        adapter = new ImageDisplayAdapter(this);
        adapter.setOnHoldListener(position -> displayImageOptions(adapter.getImage(position)));
        adapter.setOnClickListener(position -> openImage(adapter.getImage(position)));

        imageDisplay.setAdapter(adapter);
        imageDisplay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        findViewById(R.id.docTakePhotoBtn).setOnClickListener(c->takePhoto());
        findViewById(R.id.docSubmitDocumentation).setOnClickListener(c->submitDocumentation());


    }
    private void populateSpinner(){

        dropDownAdapter = new ProjectSpinnerAdapter(this, R.layout.project_spinner_item, new ArrayList<>());
        project.setAdapter(dropDownAdapter);
        new GetAllProjectsTask((projects) -> {
            if (projects.isSuccess()) {
                dropDownAdapter.setData(projects.getResult());
            } else {
                Log.e(DocumentActivity.class.getSimpleName(), "Failed to set spinner values");
            }
        }).execute();
//        String[] projects = new String[]{"Robert blir ferdig", "Kristoffer får bank", "Oskar døde"}; //TODO erstatt dummy data med data fra server
//        droptDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, projects);
//        dropDown.setAdapter(droptDownAdapter);

    }

    private void submitDocumentation() {
        //TODO: Sub
        Toast.makeText(DocumentActivity.this, "You pressed Submit, doesitwork? maybe", Toast.LENGTH_SHORT).show();
        Documentation documentation = new Documentation(title.getText().toString(), description.getText().toString(),imageList,  (Project) project.getSelectedItem(), MainNavigationMenuActivity.getCurrentUser());
        try {
            StorageUtils.saveReportToFile(documentation, getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
        Intent intent = new Intent(this, DisplayReportActivity.class);
        intent.putExtra(LatestReportActivity.REPORT, documentation);
        startActivity(intent);
    }

    private void displayImageOptions(Image image) {
        //TODO: display alert to user about removing picture or taking a new one.
    }
    private void openImage(Image image){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("file://" + image.getImageFile().getAbsolutePath());
        intent.setDataAndType(data, "image/*");
        startActivity(intent);
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should
            imageFile = null;
            try {
                imageFile = ImageUtils.createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"Documentation");
            } catch (IOException ex) {
                ex.printStackTrace();
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
                Image image = new Image(imageFile);
                addImageToList(image);
            }
        }
    }

    private void addImageToList(Image image) {
        adapter.addImage(image);
        imageList.add(image);
    }
}
