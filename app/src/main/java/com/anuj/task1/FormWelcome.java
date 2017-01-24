package com.anuj.task1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class FormWelcome extends AppCompatActivity {
    private Database myDB;

    private Cursor mCursor;

    private ListView mListView;

    private ArrayList<String> mFirstName_ArrayList = new ArrayList<String>();

    private ArrayList<String> mLastName_ArrayList = new ArrayList<String>();

    private ArrayList<String> mUserID_ArrayList = new ArrayList<String>();

    private SQLiteListAdapter mListAdapter;

    private ArrayList<String> mUserDetails;

    private ArrayList<String> mHobbyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_welcome);

        //Toolbar customisation
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL);

        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar, null);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_text_view);

        textviewTitle.setText(R.string.app_welcome_title);

        getSupportActionBar().setCustomView(viewActionBar, params);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        myDB = new Database(this);

        mListView = (ListView) findViewById(R.id.listView1);

        mUserDetails = new ArrayList<String>();

        mHobbyDetails = new ArrayList<String>();

    }

    @Override
    protected void onResume() {
        mUserDetails.clear();

        mHobbyDetails.clear();

        showSQLiteDBdata();

        super.onResume();
    }

    /*Method to display user info in a list view*/
    private void showSQLiteDBdata() {
        try {
            SQLiteDatabase db = myDB.getWritableDatabase();

            mCursor = db.rawQuery("SELECT * FROM userData", null);

            mUserID_ArrayList.clear();

            mFirstName_ArrayList.clear();

            mLastName_ArrayList.clear();

            if (mCursor.moveToFirst()) {
                do {
                    mUserID_ArrayList.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.USER_ID))));

                    mFirstName_ArrayList.add(mCursor.getString(mCursor.getColumnIndex(myDB.FIRST_NAME)));

                    mLastName_ArrayList.add(mCursor.getString(mCursor.getColumnIndex(myDB.LAST_NAME)));


                } while (mCursor.moveToNext());
            }

            mListAdapter = new SQLiteListAdapter(FormWelcome.this,

                    mFirstName_ArrayList,

                    mLastName_ArrayList

            );

            mListView.setAdapter(mListAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ArrayList userInfoList = getUserFormDetails(Integer.parseInt(mUserID_ArrayList.get(position)));

                    ArrayList userHobbyList = getUserHobbyDetails(Integer.parseInt(mUserID_ArrayList.get(position)));

                    Intent intent = new Intent(FormWelcome.this, FormValidation.class);

                    Bundle bundle = new Bundle();

                    bundle.putStringArrayList("userInfo", userInfoList);

                    bundle.putStringArrayList("hobbyInfo", userHobbyList);

                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            });

            mCursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*Method to retrieve user details from the database*/
    public ArrayList getUserFormDetails(int id) {
        if (myDB != null) {
            try {
                mCursor = myDB.getSelectedUserData(id);

                if (mCursor.moveToFirst()) {
                    do {
                        mUserDetails.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.USER_ID))));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.FIRST_NAME)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.LAST_NAME)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.EMAIL)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.PHONE_NUMBER)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.GENDER)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.STREET)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.COUNTRY)));

                        mUserDetails.add(mCursor.getString(mCursor.getColumnIndex(myDB.STATE)));

                        mCursor.moveToNext();
                    }

                    while (mCursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return mUserDetails;
    }

    /*Method to retrieve user hobbies from the database*/
    public ArrayList getUserHobbyDetails(int id) {
        try {
            mCursor = myDB.getHobbies(id);

            mCursor.moveToFirst();

            while (mCursor != null) {

                mHobbyDetails.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.HOBBY_TV))));

                mHobbyDetails.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.HOBBY_BOOKS))));

                mHobbyDetails.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.HOBBY_VIDEOGAMES))));

                mHobbyDetails.add(Integer.toString(mCursor.getInt(mCursor.getColumnIndex(myDB.HOBBY_STAMPS))));

                mCursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHobbyDetails;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to go back to the home page?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FormWelcome.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();
    }
}
