package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.Adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.Data.Documentation;
import kore.ntnu.no.safespace.Data.Image;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.Utils.ImageUtils;

public class DocumentActivity extends AppCompatActivity {
    public static final int TAKE_PICTURE_REQUEST = 33;
    private RecyclerView imageDisplay;
    private Documentation doc;
    private TextView sender;
    private TextView description;
    private ArrayAdapter<String> droptDownAdapter;
    private ImageDisplayAdapter adapter;
    private List<Image> imageList;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Spinner dropDown = findViewById(R.id.docProject);
        imageDisplay = findViewById(R.id.docTakenPhotos);
        sender = findViewById(R.id.docSenderID);
        description = findViewById(R.id.docDescription);

        imageList = new ArrayList<>();

        String[] projects = new String[]{"Robert blir ferdig", "Kristoffer får bank", "Oskar døde"}; //TODO erstatt dummy data med data fra server
        droptDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, projects);
        dropDown.setAdapter(droptDownAdapter);

        adapter = new ImageDisplayAdapter(this);
        adapter.setOnHoldListener(position -> displayImageOptions(adapter.getImage(position)));
        adapter.setOnClickListener(position -> openImage(adapter.getImage(position)));

        imageDisplay.setAdapter(adapter);
        imageDisplay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        findViewById(R.id.docTakePhotoBtn).setOnClickListener(c->takePhoto());
        findViewById(R.id.docSubmitDocumentation).setOnClickListener(c->submitDocumentation());


    }

    private void submitDocumentation() {
        //TODO: Sub
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
