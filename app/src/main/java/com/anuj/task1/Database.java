package com.anuj.task1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anuj on 07-10-2016.
 */

/*Class to handle data in database*/
public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public static final String USERTABLE_NAME = "userData";

    public static final String HOBBIESTABLE_NAME = "hobbies";

    public static final String USER_ID = "UserID";

    public static final String HOBBY_ID = "HobbyID";

    public static final String FIRST_NAME = "FirstName";

    public static final String LAST_NAME = "LastName";

    public static final String PHONE_NUMBER = "Phone";

    public static final String EMAIL = "Email";

    public static final String GENDER = "Gender";

    public static final String STREET = "Street";

    public static final String COUNTRY = "Country";

    public static final String STATE = "State";

    public static final String HOBBY_TV = "WatchTV";

    public static final String HOBBY_BOOKS = "ReadBooks";

    public static final String HOBBY_VIDEOGAMES = "VideoGames";

    public static final String HOBBY_STAMPS = "CollectStamps";


    public Database(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*CREATING USERS TABLE*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + USERTABLE_NAME + "(" + USER_ID + " INTEGER PRIMARY KEY, " + FIRST_NAME + " VARCHAR, " + LAST_NAME + " VARCHAR, " + EMAIL + " VARCHAR, " + PHONE_NUMBER + " VARCHAR(12), " + GENDER + " VARCHAR," + STREET + " VARCHAR, " + COUNTRY + " VARCHAR, " + STATE + " VARCHAR)");

        /*CREATING HOBBIES TABLE*/
        db.execSQL("CREATE TABLE IF NOT EXISTS " + HOBBIESTABLE_NAME + "(" + HOBBY_ID + " INTEGER PRIMARY KEY, " + USER_ID + " INTEGER, " + HOBBY_TV + " INTEGER, " + HOBBY_BOOKS + " INTEGER, " + HOBBY_VIDEOGAMES + " INTEGER, " + HOBBY_STAMPS + " INTEGER)");
    }

    /*Method to upgrade the data in a database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + USERTABLE_NAME);
        // create fresh users table
        this.onCreate(db);
    }

    /*Method to insert data into userdata table*/
    public void insertUsers(String firstName, String lastName, String email, String phone, String gender, String street, String country, String state) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(FIRST_NAME, firstName);

        contentValues.put(LAST_NAME, lastName);

        contentValues.put(EMAIL, email);

        contentValues.put(PHONE_NUMBER, phone);

        contentValues.put(GENDER, gender);

        contentValues.put(STREET, street);

        contentValues.put(COUNTRY, country);

        contentValues.put(STATE, state);

        db.insert(USERTABLE_NAME, null, contentValues);

    }

    /*Method to insert data into hobbies table */
    public void insertHobbies(int userId, int watchTVHobby, int readBooksHobby, int videoGamesHobby, int collectStamps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userId);

        contentValues.put(HOBBY_TV, watchTVHobby);

        contentValues.put(HOBBY_BOOKS, readBooksHobby);

        contentValues.put(HOBBY_VIDEOGAMES, videoGamesHobby);

        contentValues.put(HOBBY_STAMPS, collectStamps);

        db.insert(HOBBIESTABLE_NAME, null, contentValues);

    }

    public void updateUsers(int id, String firstName, String lastName, String email, String phone, String gender, String street, String country, String state) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(FIRST_NAME, firstName);

        contentValues.put(LAST_NAME, lastName);

        contentValues.put(EMAIL, email);

        contentValues.put(PHONE_NUMBER, phone);

        contentValues.put(GENDER, gender);

        contentValues.put(STREET, street);

        contentValues.put(COUNTRY, country);

        contentValues.put(STATE, state);

        String[] whereargs = {String.valueOf(id)};
        db.update(USERTABLE_NAME, contentValues, USER_ID + " =?", whereargs);

    }

    public void updateHobbies(int userId, int watchTVHobby, int readBooksHobby, int videoGamesHobby, int collectStamps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userId);

        contentValues.put(HOBBY_TV, watchTVHobby);

        contentValues.put(HOBBY_BOOKS, readBooksHobby);

        contentValues.put(HOBBY_VIDEOGAMES, videoGamesHobby);

        contentValues.put(HOBBY_STAMPS, collectStamps);

        String[] whereargs = {String.valueOf(userId)};

        db.update(HOBBIESTABLE_NAME, contentValues, USER_ID + "=?", whereargs);

    }


    /*Method to get data from userData table in database*/
    public Cursor getLatestUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + USERTABLE_NAME + " WHERE " + USER_ID + "=(SELECT MAX(" + USER_ID + ") FROM " + USERTABLE_NAME + ")", null);

        return res;
    }


    public Cursor getSelectedUserData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + USERTABLE_NAME + " WHERE " + USER_ID + "=" + id + "", null);

        return res;
    }

    /*Method to get data from hobbies table in database*/
    public Cursor getHobbies(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + HOBBIESTABLE_NAME + " WHERE " + USER_ID + "=" + userId + "", null);

        return res;
    }


    /*Method to delete data in a database*/
    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(USERTABLE_NAME,
                "UserID = ? ",
                new String[]{Integer.toString(id)});
    }

    /*Method to drop a table from the database*/
    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE " + USERTABLE_NAME);
    }


}
