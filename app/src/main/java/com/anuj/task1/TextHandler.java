package com.anuj.task1;

import android.content.Context;
import android.os.Environment;
import android.widget.EditText;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anuj on 06-10-2016.
 */
public class TextHandler {

    /*Using regex to validate data entered is valid or not*/
    private static final String NAME_REGEX = "^[a-z ]*$";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{5}([- ]*)\\d{6}";
    private static String mStr;
    private static Pattern mPattern;
    private static Matcher mMatcher;
    private static boolean mBoolean;


    /*Method to check if a valid name is entered*/
    public static boolean nameIsValid(EditText editText) {
        mStr = editText.getText().toString();
        mPattern = Pattern.compile(NAME_REGEX, Pattern.CASE_INSENSITIVE);
        mMatcher = mPattern.matcher(mStr);
        mBoolean = mMatcher.find();

        if (!mBoolean) {
            editText.setError("Invalid");
            return false;
        } else {
            return true;
        }


    }

    /*Method to check if a valid email is entered*/
    public static boolean emailIsValid(EditText editText) {
        mStr = editText.getText().toString().trim();
        mPattern = Pattern.compile(EMAIL_REGEX);
        mMatcher = mPattern.matcher(mStr);
        mBoolean = mMatcher.find();
        if (!mBoolean) {
            editText.setError("Invalid");
            return false;
        } else {
            return true;
        }
    }

    /*Method to check if a valid phone is entered*/
    public static boolean phoneIsValid(EditText editText) {
        mStr = editText.getText().toString().trim();
        mPattern = Pattern.compile(PHONE_REGEX);
        mMatcher = mPattern.matcher(mStr);
        mBoolean = mMatcher.find();
        if (!mBoolean) {
            editText.setError("Invalid");
            return false;
        } else {
            return true;
        }
    }


    /*Method to check if the edit texts are empty or not*/
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();

        if (text.length() == 0) {
            editText.setError("Empty");
            return false;
        }
        return true;

    }

    /*Method to export database*/
    public static void exportDB(Context context, String db_name) {

        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + "Your Backup Folder" +
                File.separator);

        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {

            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + context.getPackageName() + "/databases/" + db_name;
            String backupDBPath = db_name;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                //                Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
