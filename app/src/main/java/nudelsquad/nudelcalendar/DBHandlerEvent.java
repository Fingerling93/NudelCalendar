package nudelsquad.nudelcalendar;

/**
 * Created by emanuel on 20/04/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerEvent extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "NudelCal_Data";

    // Events table name
    private static final String TABLE_EVENTS = "events";

    // Events Table Columns names
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_NAME = "name";
    private static final String KEY_EVENT_START = "start";
    private static final String KEY_EVENT_STOP = "stop";
    private static final String KEY_EVENT_DATUM = "datum";
    private static final String KEY_EVENT_TYPE = "type";
    private static final String KEY_EVENT_LOCATION = "location";
    private static final String KEY_EVENT_COLOR = "color";
    //TODO Implement reminder logic

    public DBHandlerEvent(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_EVENT_NAME + " TEXT,"
                + KEY_EVENT_START + " TIME, " + KEY_EVENT_STOP + " TIME,"
                + KEY_EVENT_DATUM + " DATE, "
                + KEY_EVENT_TYPE + " TEXT, " + KEY_EVENT_LOCATION + " TEXT, "
                + KEY_EVENT_COLOR + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        
        // Creating tables again
        onCreate(db);
    }
    
    
    // Adding new event
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_EVENT_ID, 0);
        values.put(KEY_EVENT_NAME, event.getEVENT_NAME()); // Event Name
        values.put(KEY_EVENT_START, event.getEVENT_START());
        values.put(KEY_EVENT_STOP, event.getEVENT_END());
        values.put(KEY_EVENT_TYPE, event.getEVENT_TYPE());
        values.put(KEY_EVENT_DATUM, event.getEVENT_DATUM());
        values.put(KEY_EVENT_LOCATION, event.getEVENT_LOCATION());
        values.put(KEY_EVENT_COLOR, event.getEVENT_COLOR());

        //TODO Implement Reminder logic
        //values.put(KEY_EVENT_REMINDER, event.getEVENT_REMINDER());

        // Inserting a Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
        Log.e("IN DB", "Added");
    }
    // Getting one event
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EVENTS, new String[]{KEY_EVENT_ID,
                        KEY_EVENT_NAME, KEY_EVENT_START, KEY_EVENT_STOP, KEY_EVENT_DATUM, KEY_EVENT_TYPE,
                        KEY_EVENT_LOCATION, KEY_EVENT_COLOR}, KEY_EVENT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Event single_event = new Event(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getInt(7));

        // return event object
        return single_event;
    }
    // Getting All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
    // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getInt(7));

                // Adding events to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return ecents list (Debug only)
        return eventList;
    }

    public List<Event> getEventsFromDay(String day) {
        List<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " where " + KEY_EVENT_DATUM
                + " = '" + day + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getInt(7));

                // Adding events to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return ecents list (Debug only)
        return eventList;
    }

    // Getting events Count
    public int getEventsCount() {
        String countQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    // Updating a event
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_ID, event.getEVENT_ID());
        values.put(KEY_EVENT_NAME, event.getEVENT_NAME()); // Event Name
        values.put(KEY_EVENT_START, event.getEVENT_START());
        values.put(KEY_EVENT_STOP, event.getEVENT_END());
        values.put(KEY_EVENT_TYPE, event.getEVENT_TYPE());
        values.put(KEY_EVENT_LOCATION, event.getEVENT_LOCATION());
        values.put(KEY_EVENT_COLOR, event.getEVENT_COLOR());

        // updating row
        return db.update(TABLE_EVENTS, values, KEY_EVENT_ID + " = ?",
                new String[]{String.valueOf(event.getEVENT_ID())});
    }

    // Deleting a event
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_EVENT_ID + " = ?",
                new String[] { String.valueOf(event.getEVENT_ID()) });
        db.close();
    }
}