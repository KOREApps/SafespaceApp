package kore.ntnu.no.safespace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by robert on 11/20/17.
 */

public class ErrorDialog {

    public static void showErrorDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("An error ocurred")
            .setMessage(message)
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
