package kore.ntnu.no.safespace.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.adapters.ProjectSpinnerAdapter;
import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.tasks.GetAllProjectsTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.ImageUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

public class DocumentActivity extends AppCompatActivity {
    private RecyclerView imageDisplay;
    private TextView sender;
    private EditText title;
    private TextView description;
    private ProjectSpinnerAdapter dropDownAdapter;
    private ImageDisplayAdapter adapter;
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
        sender.setText(IdUtils.CURRENT_USER.getFirstName());

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
        if(ConnectionUtil.isConnected(this)) {
            new GetAllProjectsTask((projects) -> {
                if (projects.isSuccess()) {
                    dropDownAdapter.setData(projects.getResult());
                } else {
                    Log.e(DocumentActivity.class.getSimpleName(), "Failed to set spinner values");
                }
            }).execute();
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Gson gson = new Gson();
            String json = prefs.getString(IdUtils.PROJECTS, gson.toJson(Collections.EMPTY_LIST));
            List<Project> projects = gson.fromJson(json, new TypeToken<List<Project>>(){}.getType());
            dropDownAdapter.setData(projects);

        }
    }

    private void submitDocumentation() {
        //TODO: Sub
        Documentation documentation = new Documentation(title.getText().toString(), description.getText().toString(),  (Project) project.getSelectedItem(), IdUtils.CURRENT_USER);
        documentation.setImages(adapter.getImages());
        try {
            StorageUtils.saveReportToFile(documentation, getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayImageOptions(Image image) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Remove image");
        alertDialog.setMessage("Do you wish to remove the image from the documentation?");
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
                startActivityForResult(takePictureIntent, IdUtils.TAKE_PICTURE_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == IdUtils.TAKE_PICTURE_REQUEST){
            if(resultCode == RESULT_OK){
                Image image = new Image(imageFile);
                addImageToList(image);
            }
        }
    }

    private void addImageToList(Image image) {
        adapter.addImage(image);
    }
}
