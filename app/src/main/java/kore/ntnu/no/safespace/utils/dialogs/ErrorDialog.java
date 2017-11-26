package kore.ntnu.no.safespace.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;

/**
 * The purpose of this class is to provide the user with feedback when something goes wrong.
 * It greates a new ErrorDialog and displays it.
 *
 * @author Robert
 */
public class ErrorDialog {

    public static void showErrorDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("An error occurred")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {})
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    public static void showReportErrorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("An error occurred")
                .setMessage("Please go back and create a new project before you send a report.")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {})
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public static void dismissErrorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog;
        dialog = builder.create();
        dialog.dismiss();
    }

}
