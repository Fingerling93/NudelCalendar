package nudelsquad.nudelcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanuel on 27/04/16.
 */
public class DBHandlerTask extends SQLiteOpenHelper
{
    /**
     * Version of DB
     * Change if members are added/removed from Task.java
     */
    private static final int DATABASE_VERSION = 1;


    /**
     * Name of DB
     */
    private static final String DATABASE_NAME = "NudelCal_Data";

    
    /**
     * Table Name 
     */
    private static final String TABLE_TASKS = "tasks";


    /**
     * Columns of Table
     * Oriented at members of Task.java
     */
    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TASK_DUE = "due";
    private static final String KEY_TASK_NOTE = "color";
    //TODO Implement reminder logic

    
    public DBHandlerTask(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    
    /**
     * Creates the DB if not existing
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK_NAME + " TEXT,"
                + KEY_TASK_DUE + " INTEGER," + KEY_TASK_NOTE + "TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    /**
     * Function for upgrading DB
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Creating tables again
        onCreate(db);
    }


    /**
     * Adds a new Task to DB
     * @param task
     * TODO Implement reminder?
     */
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task.getTASK_ID());
        values.put(KEY_TASK_NAME, task.getTASK_NAME());
        values.put(KEY_TASK_DUE, task.getTASK_DUE());
        values.put(KEY_TASK_NOTE, task.getTASK_NOTES());
        //TODO HERE

        /**
         * Inserting values into Db
         */
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Getting task by ID
     * @param id
     * @return List taskList
     */
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_TASK_ID,
                        KEY_TASK_NAME, KEY_TASK_DUE, KEY_TASK_NOTE}, KEY_TASK_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        /**
         * 0 = ID
         * 1 = NAME
         * 2 = DUE DATE
         * 3 = NOTES
         * 4 = BOOL if checked
         */
        Task single_task = new Task(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                cursor.getString(3), cursor.getInt(4) > 0);
        return single_task;
    }

    /**
     * Get a list of tasks sorted by nearest first
     * @return List tasklist
     */
    public List<Task> getTasksByDate()
    {
        List<Task> taskList = new ArrayList<Task>();
        String dbQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + KEY_TASK_DUE +
                " = (SELECT MIN(" + KEY_TASK_DUE + ") FROM " + TABLE_TASKS + " )";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(dbQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Task task = new Task();
                task.setTASK_ID(cursor.getInt(0));
                task.setTASK_NAME(cursor.getString(1));
                task.setTASK_DUE(cursor.getInt(2));
                task.setTASK_NOTES(cursor.getString(3));
                task.setTASK_DONE(cursor.getInt(4) > 0);
            }
            while (cursor.moveToNext());
        }
        return taskList;
    }

    /**
     * Returns all Tasks
     * FOR DEBUG ONLY!
     * @return List TaskList
     */
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        String dbQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(dbQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTASK_ID(cursor.getInt(0));
                task.setTASK_NAME(cursor.getString(1));
                task.setTASK_DUE(cursor.getInt(2));
                task.setTASK_NOTES(cursor.getString(3));
                task.setTASK_DONE(cursor.getInt(4) > 0);

                
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        close();
        return taskList;
    }

    /**
     * Returns the count of tasks
     * @return int
     */
    public int getTasksCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    /**
     * Updates a task
     * @param task
     * @return
     */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_ID, task.getTASK_ID());
        values.put(KEY_TASK_NAME, task.getTASK_NAME()); // Task Name
        values.put(KEY_TASK_DUE, task.getTASK_DUE());
        values.put(KEY_TASK_NOTE, task.getTASK_NOTES());


        return db.update(TABLE_TASKS, values, KEY_TASK_ID + " = ?",
                new String[]{String.valueOf(task.getTASK_ID())});
    }

    /**
     * Deletes a task
     * @param task
     */
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TASK_ID + " = ?",
                new String[] { String.valueOf(task.getTASK_ID()) });
        db.close();
    }
}