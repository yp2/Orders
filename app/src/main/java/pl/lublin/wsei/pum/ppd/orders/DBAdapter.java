package pl.lublin.wsei.pum.ppd.orders;

/**
 * Created by daniel on 20.01.15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";


    // stałe
    private static final int OBJ_CREATED = 1;
    private static final int OBJ_MODIFIED = 1;
    private static final int OBJ_NOT_CREATED = 0;
    private static final int OBJ_NOT_MODIFIED = 0;

    // sync type
    public static final int SYNC_CREATE = 1;
    public static final int SYNC_MODIFIED = 2;


    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // Settings table
    public static final String S_KEY_USERNAME = "username";
    public static final String S_KEY_HOST = "host";

    // Client table
    public static final String C_KEY_NAME = "name";
    public static final String C_KEY_ADDRESS = "address";
    public static final String C_KEY_CREATED = "created"; // utworzony - to ma prawo zmienić tylko synchronizacja
    public static final String C_KEY_MODIFIED = "modified"; // zmodyfikowany - to zmienia się z 1 za każdym razem gdy modyfikujemy
                                                            // synchronizacja zmienia to na 0 i tylko synchroniazacja

    // Order table
    public static final String O_KEY_CLIENT = "client";
//    public static final String O_KEY_USER = "user";
 //   public static final String O_KEY_ORDER_DATE = "order_date";
   // public static final String O_KEY_DELIVERY_DATE = "delivery_date";
    public static final String O_KEY_CONTENT = "content";

    // Settings table (0 - KEY_ROWID 1=...)
    public static final int S_COL_USERNAME = 1;
    public static final int S_COL_HOST = 2;

    // Client table (0 = KEY_ROWID, 1=...)
    public static final int C_COL_NAME = 1;
    public static final int C_COL_ADDRESS = 2;
    public static final int C_COL_CREATED = 3;
    public static final int C_COL_MODIFIED = 4;


    // Order table
    public static final int O_COL_CLIENT = 1;
//    public static final int O_COL_USER = 2;
//    public static final int O_COL_ORDER_DATE = 2;
//    public static final int O_COL_DELIVERY_DATE = 3;
    public static final int O_COL_CONTENT = 2;


    public static final String[] S_ALL_KEYS = new String[] {KEY_ROWID, S_KEY_USERNAME, S_KEY_HOST};
    public static final String[] C_ALL_KEYS = new String[] {KEY_ROWID, C_KEY_NAME, C_KEY_ADDRESS, C_KEY_CREATED, C_KEY_MODIFIED};
    public static final String[] O_ALL_KEYS = new String[] {
            KEY_ROWID, O_KEY_CLIENT, O_KEY_CONTENT};


    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "OrdersDB";
    public static final String SETTINGS_TABLE = "settingsTable";
    public static final String CLIENT_TABLE = "clietTable";
    public static final String ORDERS_TABLE = "orderTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 5;

    private static final String DB_CREATE_CLIENT_SQL =
            "create table " + CLIENT_TABLE
            + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
			 * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!

            + C_KEY_NAME + " text not null, "
            + C_KEY_ADDRESS + " text, "
            + C_KEY_CREATED + " integer, "
            + C_KEY_MODIFIED + " integer" + ");";

    private static final String  DB_CREATE_ORDER_SQL =
            "create table " + ORDERS_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + O_KEY_CLIENT + " int, "
                    + O_KEY_CONTENT + " text not null" + ");";

    private static final String DB_CREATE_SETTINGS_SQL =
            "create table " + SETTINGS_TABLE
                + " (" + KEY_ROWID + " integer primary key autoincrement, "
                + S_KEY_USERNAME + " text not null, "
                + S_KEY_HOST + " text " + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertClient(String name, String address) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:

        ContentValues initialValues = new ContentValues();
        initialValues.put(C_KEY_NAME, name);
        initialValues.put(C_KEY_ADDRESS, address);
        initialValues.put(C_KEY_CREATED, OBJ_CREATED); // utworzony - to ma prawo zmienić tylko synchronizacja
        initialValues.put(C_KEY_MODIFIED, OBJ_NOT_MODIFIED); // niezmodyfikowany

        // Insert it into the database.
        return db.insert(CLIENT_TABLE, null, initialValues);
    }

/*
    public long insertOrder(int client, String order_date,
                            String delivery_date, String content ){

        ContentValues values = new ContentValues();
        values.put(O_KEY_CLIENT, client);
//        values.put(O_KEY_USER, user);
        values.put(O_KEY_CONTENT, content);

        return db.insert(ORDERS_TABLE, null, values);
    }
*/

    public long insertOrder(long client, String content ){

        ContentValues values = new ContentValues();
        values.put(O_KEY_CLIENT, client);
//        values.put(O_KEY_USER, user);
        values.put(O_KEY_CONTENT, content);

        return db.insert(ORDERS_TABLE, null, values);
    }


    // Delete a row from the database, by rowId (primary key)
    public boolean deleteClient(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(CLIENT_TABLE, where, null) != 0;
    }

//    public void deleteAll() {
//        Cursor c = getAllRows();
//        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
//        if (c.moveToFirst()) {
//            do {
//                deleteRow(c.getLong((int) rowId));
//            } while (c.moveToNext());
//        }
//        c.close();
//    }

    // Return all data in the database.
    public Cursor getAllClients() {
        String where = null;
        Cursor c = 	db.query(false, CLIENT_TABLE, C_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllOrders() {
        String where = null;
        Cursor c = 	db.query(false, ORDERS_TABLE, O_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getAllCreatedClients(){
        String where = C_KEY_CREATED + "=" + OBJ_CREATED;
        Cursor c = 	db.query(false, CLIENT_TABLE, C_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public boolean setClientSync(long rowId, int syncType){
        String where = KEY_ROWID + "=" + rowId;

        ContentValues newValues = new ContentValues();
        if (syncType == SYNC_CREATE){
            newValues.put(C_KEY_CREATED, OBJ_NOT_CREATED);
        } else if ( syncType == SYNC_MODIFIED) {
            newValues.put(C_KEY_MODIFIED, OBJ_NOT_MODIFIED);
        }
        // Insert it into the database.
        return db.update(CLIENT_TABLE, newValues, where, null) != 0;
    }

    public Cursor getAllModifiedClients(){
        String where = C_KEY_MODIFIED + "=" + OBJ_MODIFIED;
        Cursor c = 	db.query(false, CLIENT_TABLE, C_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getClient(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db.query(false, CLIENT_TABLE, C_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }



    public Cursor getSettings(){
        String where = KEY_ROWID + "=1";
        Cursor c = 	db.query(false, SETTINGS_TABLE, S_ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public String getSettingsUsername(){
        String username = "";
        Cursor cursor = this.getSettings();
        if (cursor.moveToFirst()){
            username = cursor.getString(DBAdapter.S_COL_USERNAME);
        }
        cursor.close();
        return username;
    }

    public String getSettingsHost(){
        String host = "";
        Cursor cursor = this.getSettings();
        if (cursor.moveToFirst()){
            host = cursor.getString(DBAdapter.S_COL_HOST);
        }
        cursor.close();
        return host;
    }

    public boolean updateSettings(String username, String host){
        Cursor cursor = getSettings();

        String where = KEY_ROWID + "=1";
        ContentValues newValues = new ContentValues();
        newValues.put(S_KEY_USERNAME, username);
        newValues.put(S_KEY_HOST, host);

        if (cursor.moveToFirst()){
            cursor.close();
            return db.update(SETTINGS_TABLE, newValues, where, null) != 0;
        } else {
            cursor.close();
            newValues.put(KEY_ROWID, 1);
            long id = db.insert(SETTINGS_TABLE, null, newValues);
            return id != 0;
        }

    }

    // Change an existing row to be equal to new data.
    public boolean updateClient(long rowId, String name, String address) {
        String where = KEY_ROWID + "=" + rowId;

		/*
		 * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(C_KEY_NAME, name);
        newValues.put(C_KEY_ADDRESS, address);
        newValues.put(C_KEY_MODIFIED, 1);

        // Insert it into the database.
        return db.update(CLIENT_TABLE, newValues, where, null) != 0;
    }




    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DB_CREATE_CLIENT_SQL);
            _db.execSQL(DB_CREATE_ORDER_SQL);
            _db.execSQL(DB_CREATE_SETTINGS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}