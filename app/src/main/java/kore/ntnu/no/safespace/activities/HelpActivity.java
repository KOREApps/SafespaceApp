package kore.ntnu.no.safespace.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.BugReport;
import kore.ntnu.no.safespace.tasks.SendBugReportTask;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * The purpose of this activity is to report a bug / contact customer support.
 *
 * @author Erik
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button bugBtn = findViewById(R.id.bugReportBtn);
        bugBtn.setOnClickListener(this::onButtonShowPopupWindow);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onButtonShowPopupWindow(View view) {
        ConstraintLayout mainLayout = findViewById(R.id.activity_help);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_bugreport, null);

        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
        Button RepBugBtn = popupView.findViewById(R.id.bugReportSendBtn);
        EditText bugDescriptionView = popupView.findViewById(R.id.bugReportDescription);
        EditText bugTitleView = popupView.findViewById(R.id.bugReportTitle);
        RepBugBtn.setOnClickListener(view1 -> {
            String bugTitle = bugTitleView.getText().toString();
            String bugDescription = bugDescriptionView.getText().toString();
            BugReport bugReport = new BugReport(bugTitle, bugDescription, IdUtils.CURRENT_USER);
            new SendBugReportTask(result -> {
                if (result.isSuccess()) {
                    Toast.makeText(
                            this,
                            "Thanks for the bug report. Report id: " + result.getResult().getId(),
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(
                            this,
                            "Failed to submit report: " + result.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }).execute(bugReport);
//            if (!bugTitle.equals("") && !bugDescription.equals("")) {
//                BugReport report = new BugReport(bugTitle, bugDescription, IdUtils.CURRENT_USER);
//                Toast.makeText(this, "Thanks for the bug report. Report id: " + Math.abs(new Random().nextInt()) % 2500, Toast.LENGTH_LONG).show();
//                finish();
//            } else {
//                Toast.makeText(HelpActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
//            }
            popupWindow.dismiss();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
