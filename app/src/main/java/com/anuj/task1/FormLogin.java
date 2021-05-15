package com.anuj.task1;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FormLogin extends AppCompatActivity {

    private EditText mFirstName;

    private EditText mLastName;

    private EditText mEmail;

    private EditText mPhone;

    private EditText mStreet;

    private RadioButton mRadioMale;

    private RadioButton mRadioFemale;

    private Spinner mSpinnerCountries;

    private Spinner mSpinnerStates;

    private int mCheckFlag;

    private CheckBox mCheckboxTerms;

    private CheckBox mCheckboxTv;

    private CheckBox mCheckboxBooks;

    private CheckBox mCheckboxGames;

    private CheckBox mCheckboxStamps;

    private Button mButtonSave;

    private MyAdapter mAdapterCountries;

    private MyAdapter mAdapterStates;

    // private String[] mArrayCountries;

    //private String[] mArrayStatesOfCountry1;

    //private String[] mArrayStatesOfCountry2;

    private ArrayList mArrayCountries;

    private ArrayList mArrayStatesOfCountry;

    private ArrayList mArrayCountryId;

    private Database myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCenter.start(getApplication(), "5c605c37-1c4a-404f-8cf9-59a6fe543200",
                  Analytics.class, Crashes.class);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_login);

        registerViews();

        mArrayCountries = new ArrayList();

        mArrayStatesOfCountry = new ArrayList();

        mArrayCountryId = new ArrayList();

        mCheckboxTv = (CheckBox) findViewById(R.id.chkbox_tv);

        mCheckboxBooks = (CheckBox) findViewById(R.id.chkbox_books);

        mCheckboxGames = (CheckBox) findViewById(R.id.chkbox_games);

        mCheckboxStamps = (CheckBox) findViewById(R.id.chkbox_stamps);

        myDB = new Database(this);

        //Toolbar customisation
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL);

        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar, null);

        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_text_view);

        textviewTitle.setText(R.string.app_name);

        getSupportActionBar().setCustomView(viewActionBar, params);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Registering checkbox
        mCheckboxTerms = (CheckBox) findViewById(R.id.chkbox_termsandconditions);

        //Spinner data
       /* mArrayCountries = new String[]{"Select Country", "India", "USA"};

        mArrayStatesOfCountry1 = new String[]{"Select State", "Uttar Pradesh", "Telangana", "Kerala", "Orissa",
                "Haryana", "Himachal Pradesh", "Andhra", "Karnataka", "Meghalaya", "Assam", "Jammu and Kashmir",
                "Mizoram", "West Bengal"};

        mArrayStatesOfCountry2 = new String[]{"Select State", "Minnesota", "Pennsylvania", "California",
                "Texas", "Florida"};*/

        mSpinnerCountries = (Spinner) findViewById(R.id.spinner_countries_main);

        mSpinnerStates = (Spinner) findViewById(R.id.spinner_states_main);

        mArrayCountries.add("Select Country");

        DownloadCountryTask task = new DownloadCountryTask();

        task.execute("http://leansigmaway.com/api/ca_api/api.php?type=getCountries");

        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {
                    mSpinnerStates.setEnabled(false);
                }
                else {
                    mSpinnerStates.setEnabled(true);
                    int pos;
                    pos = mSpinnerCountries.getSelectedItemPosition();
                    Log.i("Country-pos", "" + pos);

                    DownloadStateTask task = new DownloadStateTask();
                    System.out.println("http://leansigmaway.com/api/ca_api/api.php?type=getStates&countryId="+pos);
                    task.execute("http://leansigmaway.com/api/ca_api/api.php?type=getStates&countryId="+pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* mAdapterCountries = new MyAdapter(mArrayCountries, this);

        mSpinnerCountries.setAdapter(mAdapterCountries);

        // gettingCountries();
        mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    mSpinnerStates.setEnabled(false);

                } else {

                    mSpinnerStates.setEnabled(true);

                    int pos;

                    pos = mSpinnerCountries.getSelectedItemPosition();

                    int iden = parent.getId();

                    if (iden == R.id.spinner_countries_main) {
                        switch (pos) {
                            case 1:
                                mAdapterStates = new MyAdapter(mArrayStatesOfCountry1, getApplicationContext());

                                break;

                            case 2:
                                mAdapterStates = new MyAdapter(mArrayStatesOfCountry2, getApplicationContext());

                                break;

                            default:

                                break;
                        }

                        mSpinnerStates.setAdapter(mAdapterStates);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    /* Method to validate edit texts on the fly */
    private void registerViews() {
        mFirstName = (EditText) findViewById(R.id.edt_firstname);

        mFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextHandler.hasText(mFirstName);
                TextHandler.nameIsValid(mFirstName);
            }
        });


        mLastName = (EditText) findViewById(R.id.edt_lastname);

        mLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextHandler.hasText(mLastName);

                TextHandler.nameIsValid(mLastName);
            }
        });

        mEmail = (EditText) findViewById(R.id.edt_email);

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextHandler.hasText(mEmail);

                TextHandler.emailIsValid(mEmail);
            }
        });

        mPhone = (EditText) findViewById(R.id.edt_phone);

        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextHandler.hasText(mPhone);

                TextHandler.phoneIsValid(mPhone);
            }
        });

        mStreet = (EditText) findViewById(R.id.edt_street);

        mStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                TextHandler.hasText(mStreet);
            }
        });


        mButtonSave = (Button) findViewById(R.id.btn_save);

        mButtonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int spi1 = mSpinnerCountries.getSelectedItemPosition();

                int spi2 = mSpinnerStates.getSelectedItemPosition();

                if (!checkValidation()) {

                    Toast.makeText(getApplicationContext(), "Form contains errors.", Toast.LENGTH_SHORT).show();

                } else if (checkValidation()) {

                    if (!mCheckboxTerms.isChecked()) {

                        Toast.makeText(getApplicationContext(), "Please accept the terms and conditions to continue!", Toast.LENGTH_SHORT).show();

                    } else if (!checkBoxChecked()) {

                        Toast.makeText(getApplicationContext(), "Select atleast one hobby.", Toast.LENGTH_SHORT).show();

                    } else if (spi1 == 0) {

                        Toast.makeText(getApplication(), "Select Country", Toast.LENGTH_SHORT).show();

                    } else if (spi1 != 0) {

                        if (spi2 == 0) {

                            Toast.makeText(getApplication(), "Select State", Toast.LENGTH_SHORT).show();

                        } else {

                            submitForm(mFirstName.getText().toString(), mLastName.getText().toString(), mEmail.getText().toString(),
                                    mPhone.getText().toString(), userGender(), mStreet.getText().toString(), mSpinnerCountries.getSelectedItem().toString(),
                                    mSpinnerStates.getSelectedItem().toString(), watchTVSelected(), readBooksSelected(), videoGamesSelected(), collectStampsSelected());

                        }
                    }
                }
            }
        });
    }


    /*Method to check if atleast one of the hobby checkboxes is ticked or not*/
    private boolean checkBoxChecked() {

        if (mCheckboxTv.isChecked()) {
            return true;
        }

        if (mCheckboxBooks.isChecked()) {
            return true;
        }

        if (mCheckboxGames.isChecked()) {
            return true;
        }

        if (mCheckboxStamps.isChecked()) {
            return true;
        } else {
            return false;
        }

    }

    /*Method that returns text of all checkboxes selected by the user*/
    private ArrayList hobbies() {
        ArrayList list = new ArrayList();

        if (mCheckboxTv.isChecked()) {
            list.add(mCheckboxTv.getText().toString());
        }

        if (mCheckboxBooks.isChecked()) {
            list.add(mCheckboxBooks.getText().toString());
        }

        if (mCheckboxGames.isChecked()) {
            list.add(mCheckboxGames.getText().toString());
        }

        if (mCheckboxStamps.isChecked()) {
            list.add(mCheckboxStamps.getText().toString());
        }
        return list;
    }

    /*Methods to set the hobbies selected to true for each user*/
    public int watchTVSelected() {

        mCheckFlag = 0;

        if (mCheckboxTv.isChecked()) {

            mCheckFlag = 1;

        }
        return mCheckFlag;
    }


    public int readBooksSelected() {
        mCheckFlag = 0;

        if (mCheckboxBooks.isChecked()) {

            mCheckFlag = 1;

        }
        return mCheckFlag;
    }

    public int videoGamesSelected() {
        mCheckFlag = 0;

        if (mCheckboxGames.isChecked()) {

            mCheckFlag = 1;

        }
        return mCheckFlag;
    }

    public int collectStampsSelected() {

        mCheckFlag = 0;

        if (mCheckboxStamps.isChecked()) {

            mCheckFlag = 1;

        }
        return mCheckFlag;
    }


    /* Method to check if all personal details are entered correctly */
    private boolean checkValidation() {
        if (!TextHandler.hasText(mFirstName)) {
            return false;
        }
        if (!TextHandler.hasText(mLastName)) {
            return false;
        }

        if (!TextHandler.hasText(mEmail)) {
            return false;
        }

        if (!TextHandler.hasText(mPhone)) {
            return false;
        }

        if (!TextHandler.hasText(mStreet)) {
            return false;
        }
        return true;
    }

    /*Method that returns the gender of the user*/
    public String userGender() {

        mRadioMale = (RadioButton) findViewById(R.id.radio_gender_male);

        mRadioFemale = (RadioButton) findViewById(R.id.radio_gender_female);

        String userGender = "";

        if (mRadioMale.isChecked()) {

            userGender = mRadioMale.getText().toString();

            return userGender;
        } else if (mRadioFemale.isChecked()) {

            userGender = mRadioFemale.getText().toString();

            return userGender;
        }
        return userGender;
    }

    /* Method to Submit form if form is valid */
    private void submitForm(String firstName, String lastName, String email, String phone, String gender, String street,
                            String country, String state, int watchTV, int readBooks, int videoGames, int collectStamps) {

        Toast.makeText(this, "Form Submitted successfully.", Toast.LENGTH_SHORT).show();

        myDB.insertUsers(firstName, lastName, email, phone, gender, street, country, state);

        Cursor c1 = myDB.getLatestUserData();

        c1.moveToFirst();

        int userIdIndex = c1.getColumnIndex(myDB.USER_ID);

        try {
            while (c1 != null) {
                myDB.insertHobbies(c1.getInt(userIdIndex), watchTV, readBooks, videoGames, collectStamps);

                c1.moveToNext();
            }
        } catch (Exception e) {

        }
        setFormToDefault();

        TextHandler.exportDB(this, myDB.DATABASE_NAME);

        Intent intent = new Intent(FormLogin.this, FormWelcome.class);

        startActivity(intent);
    }

    /*Method to Reset all form details to default*/
    private void setFormToDefault() {
        mFirstName.getText().clear();

        mLastName.getText().clear();

        mEmail.getText().clear();

        mPhone.getText().clear();

        mStreet.getText().clear();

        mSpinnerCountries.setSelection(0);

        mSpinnerStates.setSelection(0);

        if (mRadioFemale.isChecked()) {

            mRadioMale.setChecked(true);
        }
        if (mCheckboxTerms.isChecked()) {

            mCheckboxTerms.setChecked(false);
        }
        if (checkBoxChecked()) {
            mCheckboxTv.setChecked(false);

            mCheckboxBooks.setChecked(false);

            mCheckboxGames.setChecked(false);

            mCheckboxStamps.setChecked(false);
        }
    }

    /*Method to display a pop-up on pressing a back button from main page of app*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FormLogin.this.finish();
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

    /*Adding menu and menu items to the action bar*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item1:

                Intent intent = new Intent(FormLogin.this, FormWelcome.class);

                startActivity(intent);

                break;

            case R.id.menu_item2:

                onBackPressed();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*Getting country and state list */
    public class DownloadCountryTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();

                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();

                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject jsonObject = new JSONObject(result);

                String countries = jsonObject.getString("result");

                JSONArray arr = new JSONArray(countries);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String country_name = jsonPart.getString("name");

                    int country_id = jsonPart.getInt("id");

                    mArrayCountryId.add(country_id);

                    mArrayCountries.add(country_name);

                    mAdapterCountries = new MyAdapter(mArrayCountries,getApplicationContext());

                    mSpinnerCountries.setAdapter(mAdapterCountries);

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }


        }
    }


    public class DownloadStateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";

            URL url;

            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();

                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();

                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                mArrayStatesOfCountry.clear();

                JSONObject jsonObject = new JSONObject(result);

                String states = jsonObject.getString("result");

                //Log.i("State content", states);

                JSONArray arr = new JSONArray(states);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String state_name = jsonPart.getString("name");

                    mArrayStatesOfCountry.add(state_name);

                    mAdapterStates = new MyAdapter(mArrayStatesOfCountry,getApplicationContext());

                    mSpinnerStates.setAdapter(mAdapterStates);

                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }


}







