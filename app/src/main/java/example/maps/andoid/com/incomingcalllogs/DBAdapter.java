package example.maps.andoid.com.incomingcalllogs;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by techie93 on 4/24/2015.
 */

/**
 *
 *
 */
public class DBAdapter {

    public static final String KEY_NUMBER = "phone_number";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_REVIEW = "review";
    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table contacts (Phone_Number text primary key , "
            + "Name text not null, " + "Email text not null," + "review text);";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        this.DBHelper = new DBAdapter.DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           /*Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);*/
        }
    }

    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---close the database---
    public void close() {

        DBHelper.close();
    }

    // ---insert a contact into the database---
    public long insertContact(String phone_number,String name, String email,String review) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NUMBER,phone_number);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_REVIEW,review);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // ---deletes a particular contact---
    public boolean deleteContact(String phone_number) {
        return db.delete(DATABASE_TABLE, KEY_NUMBER + "=" + phone_number, null) > 0;
    }

    // ---deletes all contacts---
    public boolean deleteContacts() {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    // ---retrieves all the contacts---
    public Cursor getAllContacts() {
        return db.query(DATABASE_TABLE, new String[] { KEY_NUMBER, KEY_NAME,
                KEY_EMAIL, KEY_REVIEW }, null, null, null, null, null);




//		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
//				KEY_EMAIL }, null, null, null, null, "name DESC"); // order by clause
    }

    // ---retrieves a particular contact---
    public Cursor getContact(String phone_number) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                        KEY_NUMBER, KEY_NAME, KEY_EMAIL }, KEY_NUMBER + "=" + phone_number,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // ---updates a contact---
    public boolean updateContact(long phone_number, String name, String email) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_EMAIL, email);
        return db.update(DATABASE_TABLE, args, KEY_NUMBER + "=" + phone_number, null) > 0;
    }

}