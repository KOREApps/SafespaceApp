package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import kore.ntnu.no.safespace.R;

public class ReportActivity extends AppCompatActivity {

    public static final String PICTURE_ID = "kore.ntnu.safespace.PICTURE_ID";
    public static final int TAKE_PICTURE_REQUEST = 0;
    private ImageView takenPhoto;
    private Bitmap bitmap;


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

        capturePhoto.setOnClickListener(c -> takePhoto());



    }

    private void takePhoto() {
        Intent intent = new Intent(ReportActivity.this, TakePictureActivity.class);
        intent.putExtra(PICTURE_ID, "report");
        startActivityForResult(intent,TAKE_PICTURE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TAKE_PICTURE_REQUEST){
            if(resultCode == RESULT_OK){
                String filePath = data.getStringExtra(PICTURE_ID);
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    bitmap = BitmapFactory.decodeFile(filePath);
                    takenPhoto.setImageBitmap(bitmap);


                Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            }
        }
    }




}
