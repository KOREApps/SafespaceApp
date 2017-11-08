package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import kore.ntnu.no.safespace.Data.BugReport;
import kore.ntnu.no.safespace.R;

public class HelpActivity extends AppCompatActivity {
    Button RepBugBtn;
    EditText bugTitleView;
    EditText bugDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button bugBtn = findViewById(R.id.bugReportBtn);


        bugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindow(view);
            }
        });




    }

    public void onButtonShowPopupWindow(View view) {
        ConstraintLayout mainLayout = findViewById(R.id.activity_help);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_bugreport, null);

        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        RepBugBtn = popupView.findViewById(R.id.bugReportSendBtn);
        bugDescriptionView = popupView.findViewById(R.id.bugReportDescription);
        bugTitleView = popupView.findViewById(R.id.bugReportTitle);
        RepBugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bugTitle = bugTitleView.getText().toString();
                String bugDescription = bugDescriptionView.getText().toString();
                //TODO sett in User
                if (!bugTitle.equals("") && !bugDescription.equals("")) {
                    BugReport report = new BugReport(bugTitle, bugDescription, null);
                    System.out.println(bugTitle + bugDescription + report);
                } else {
                    //TODO Do something
                    System.out.println("Lulllll");
                }

            }
        });

    }
}
