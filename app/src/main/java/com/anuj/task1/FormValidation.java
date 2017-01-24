package com.anuj.task1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

public class FormValidation extends AppCompatActivity implements View.OnClickListener {

    private EditText mFirstName;

    private EditText mLastName;

    private EditText mEmail;

    private EditText mPhone;

    private EditText mStreet;

    private RadioButton mRadioMale;

    private RadioButton mRadioFemale;

    private CheckBox mCheckboxTv;

    private CheckBox mCheckboxBooks;

    private CheckBox mCheckboxGames;

    private CheckBox mCheckboxStamps;

    private int mCheckFlag;

    private ArrayList userData;

    private ArrayList hobbyData = null;

    private Spinner mSpinnerCountries;

    private Spinner mSpinnerStates;

    private MyAdapter mAdapterCountries;

    private MyAdapter mAdapterStates;

    //private String[] mArrayCountries;

    //private String[] mArrayStatesOfCountry1;

    //private String[] mArrayStatesOfCountry2;

    private ArrayList mArrayCountries;

    private ArrayList mArrayStatesOfCountry;

    private ArrayList mArrayCountryId;

    private Button mEditDetailsButton;

    private Database myDB;

    private String mCountry;

    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_form_validation);

        mArrayCountries = new ArrayList();

        mArrayStatesOfCountry = new ArrayList();

        mArrayCountryId = new ArrayList();

        myDB = new Database(this);

        mFirstName = (EditText) findViewById(R.id.edt_firstname);

        mLastName = (EditText) findViewById(R.id.edt_lastname);

        mEmail = (EditText) findViewById(R.id.edt_email);

        mPhone = (EditText) findViewById(R.id.edt_phone);

        mStreet = (EditText) findViewById(R.id.edt_street);

        mRadioMale = (RadioButton) findViewById(R.id.radio_gender_male);

        mRadioFemale = (RadioButton) findViewById(R.id.radio_gender_female);

        mCheckboxTv = (CheckBox) findViewById(R.id.chkbox_tv);

        mCheckboxBooks = (CheckBox) findViewById(R.id.chkbox_books);

        mCheckboxGames = (CheckBox) findViewById(R.id.chkbox_games);

        mCheckboxStamps = (CheckBox) findViewById(R.id.chkbox_stamps);

        mEditDetailsButton = (Button) findViewById(R.id.btn_edit);

        mArrayCountries = new ArrayList();

        mArrayStatesOfCountry = new ArrayList();

/* mArrayCountries = new String[]{"Select Country", "India", "USA"};

        mArrayStatesOfCountry1 = new String[]{"Select State", "Uttar Pradesh", "Telangana", "Kerala",
                "Orissa", "Haryana", "Himachal Pradesh", "Andhra", "Karnataka", "Meghalaya", "Assam",
                "Jammu and Kashmir", "Mizoram", "West Bengal"};

        mArrayStatesOfCountry2 = new String[]{"Select State", "Minnesota", "Pennsylvania", "California", "Texas", "Florida"};*/
        mArrayCountries.add("Select Country");


        mSpinnerCountries = (Spinner) findViewById(R.id.spinner_countries_main);

        mSpinnerStates = (Spinner) findViewById(R.id.spinner_states_main);

        mAdapterCountries = new MyAdapter(mArrayCountries, this);

        mSpinnerCountries.setAdapter(mAdapterCountries);

        mEditDetailsButton.setOnClickListener(this);

        registerViews();
    }

    @Override
    protected void onResume() {

        try {
            Bundle bundle = getIntent().getExtras();

            userData = bundle.getStringArrayList("userInfo");

            System.out.println("User Details:" + userData);

            hobbyData = bundle.getStringArrayList("hobbyInfo");

            //Toolbar customisation
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);

            View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar, null);

            TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_text_view);

            textviewTitle.setText(userData.get(1).toString());

            getSupportActionBar().setCustomView(viewActionBar, params);

            getSupportActionBar().setDisplayShowCustomEnabled(true);

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            mFirstName.setText(userData.get(1).toString());

            mLastName.setText(userData.get(2).toString());

            mEmail.setText(userData.get(3).toString());

            mPhone.setText(userData.get(4).toString());

            mStreet.setText(userData.get(6).toString());

            getUserGender();

            mCountry = userData.get(7).toString();

            mState = userData.get(8).toString();

            DownloadCountryTask task = new DownloadCountryTask();

            task.execute("http://leansigmaway.com/api/ca_api/api.php?type=getCountries");

            /*// Getting position of country from database
            for (int i = 0; i < mArrayCountries.size(); i++) {
                System.out.println("Size of country array is: " + mArrayCountries.size());
                String c = mArrayCountries.get(i).toString();
                System.out.println("Value of c is: " + c);
                System.out.println("Value of mCountry is: "+mCountry);

                if (c.equals(mCountry)) {
                    mSpinnerCountries.setSelection(i);
                }

                else{
                    continue;
                }

            }*/


            mSpinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos;
                    pos = mSpinnerCountries.getSelectedItemPosition();
                    if (pos == 0) {
                        mSpinnerStates.setEnabled(false);
                    } else {
                        mSpinnerStates.setEnabled(true);
                        DownloadStateTask task = new DownloadStateTask();
                        task.execute("http://leansigmaway.com/api/ca_api/api.php?type=getStates&countryId="+pos);

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            getHobbies();

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onResume();

    }

/*Method to retrieve gender of the selected user*/

    public void getUserGender() {
        if (userData.get(5).toString().equals("Male")) {

            mRadioMale.setChecked(true);

        } else {

            mRadioFemale.setChecked(true);
        }
    }

/*Method to retrieve hobbies of the selected user's*/

    public void getHobbies() {

        if (hobbyData.get(0).toString().equals("1")) {
            mCheckboxTv.setChecked(true);
        }
        if (hobbyData.get(1).toString().equals("1")) {
            mCheckboxBooks.setChecked(true);
        }
        if (hobbyData.get(2).toString().equals("1")) {
            mCheckboxGames.setChecked(true);
        }
        if (hobbyData.get(3).toString().equals("1")) {
            mCheckboxStamps.setChecked(true);
        }

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

/*Method for Registering views*/

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

 /*Method to check if all personal details are entered correctly */

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

/*Validating and Saving the edited details*/

    @Override
    public void onClick(View v) {
        int spi1 = mSpinnerCountries.getSelectedItemPosition();

        int spi2 = mSpinnerStates.getSelectedItemPosition();

        if (!checkValidation()) {

            Toast.makeText(getApplicationContext(), "Form contains errors.", Toast.LENGTH_SHORT).show();

        } else if (checkValidation()) {

            if (!checkBoxChecked()) {

                Toast.makeText(getApplicationContext(), "Select atleast one hobby.", Toast.LENGTH_SHORT).show();

            } else if (spi1 == 0) {

                Toast.makeText(getApplication(), "Select Country", Toast.LENGTH_SHORT).show();

            } else if (spi1 != 0) {

                if (spi2 == 0) {

                    Toast.makeText(getApplication(), "Select State", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Changes have been Updated and Saved...", Toast.LENGTH_SHORT).show();

                    myDB.updateUsers(Integer.parseInt(userData.get(0).toString()),
                            mFirstName.getText().toString(), mLastName.getText().toString(), mEmail.getText().toString(),
                            mPhone.getText().toString(), userGender(), mStreet.getText().toString(),
                            mSpinnerCountries.getSelectedItem().toString(), mSpinnerStates.getSelectedItem().toString());

                    myDB.updateHobbies(Integer.parseInt(userData.get(0).toString()), watchTVSelected(),
                            readBooksSelected(), videoGamesSelected(), collectStampsSelected());

                }
            }
        }
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

                //String countries = jsonObject.getString("geonames");
                String countries = jsonObject.getString("result");

                Log.i("Country content", countries);

                JSONArray arr = new JSONArray(countries);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String country_name = jsonPart.getString("name");

                    int country_id = jsonPart.getInt("id");

                    mArrayCountryId.add(country_id);

                    mArrayCountries.add(country_name);

                    mAdapterCountries = new MyAdapter(mArrayCountries, getApplicationContext());

                    mSpinnerCountries.setAdapter(mAdapterCountries);

                }

                for (int i = 0; i < mArrayCountries.size(); i++) {
                    String c = mArrayCountries.get(i).toString();
                    if (c.equals(mCountry)) {
                        mSpinnerCountries.setSelection(i);
                    }
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

                Log.i("State content", states);

                JSONArray arr = new JSONArray(states);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String state_name = jsonPart.getString("name");

                    mArrayStatesOfCountry.add(state_name);

                    mAdapterStates = new MyAdapter(mArrayStatesOfCountry, getApplicationContext());

                    mSpinnerStates.setAdapter(mAdapterStates);

                }

                for (int i = 0; i < mArrayStatesOfCountry.size(); i++) {
                    String s = mArrayStatesOfCountry.get(i).toString();
                    if (s.equals(mState)) {
                        mSpinnerStates.setSelection(i);
                    }
                }
            }

            catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }


}
