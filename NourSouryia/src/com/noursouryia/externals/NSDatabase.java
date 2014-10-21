package com.noursouryia.externals;

import java.io.File;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class NSDatabase {

	protected SQLiteDatabase db; // a reference to the database manager class.
	private static final String DB_NAME = "NOUR_SOURYIA_DB"; // the name of our database
	private static final int DB_VERSION = 1; // the version of the database
	private NSSQLiteOpenHelper nsDBHelper;
	private Context context;

	protected static final int DEFAULT_VALUE = -1;
	
	// Tables names
	protected static final String TABLE_ARTICLES 		= "articles";
	protected static final String TABLE_TYPES 			= "types";
	protected static final String TABLE_COMMENTS 		= "comments";
	protected static final String TABLE_FILES 			= "files";
	protected static final String TABLE_AUTHORS 		= "authors";
	protected static final String TABLE_POLLS 			= "polls";
	protected static final String TABLE_QUESTIONS 		= "questions";
	protected static final String TABLE_CATEGORIES		= "categories";
	protected static final String TABLE_POLL_CHOICES	= "poll_choices";

//	//Common columns
	public static final String COL_ID 			= "_id";
	public static final String COL_TYPE_ID 		= "type_id";
	public static final String COL_FILE_ID 		= "file_id";
	public static final String COL_AUTHOR_ID 	= "author_id";
	public static final String COL_QUESTION_ID 	= "question_id";
	

	//Tables
	private static final String CREATE_TABLE_ARTICLES = "CREATE TABLE "+ TABLE_ARTICLES 
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NSManager.NID 			+" TEXT NOT NULL,"
			+ NSManager.TITLE 			+ " TEXT, "
			+ NSManager.BODY 			+ " TEXT, "
			+ NSManager.TYPE 			+ " TEXT, "
			+ NSManager.TYPE_A 			+ " TEXT, "
			+ NSManager.VISITS 			+ " INTEGER, "
			+ NSManager.CREATED 		+ " TEXT, "
			+ NSManager.NAME 			+ " TEXT, "
			+ NSManager.TID 			+ " INTEGER, "
			+ NSManager.FILE_PATH 		+ " TEXT, "
			+ NSManager.YOUTUBE_LINK 	+ " TEXT, "
			+ NSManager.MP4_LINK 		+ " TEXT, "
			+ NSManager.MP3_LINK 		+ " TEXT, "
			+ NSManager.PDF_LINK 		+ " TEXT, "
			+ COL_FILE_ID 				+ " INTEGER, "
			+ COL_AUTHOR_ID 			+ " INTEGER);";
	

	private static final String CREATE_TABLE_TYPES = "CREATE TABLE " + TABLE_TYPES
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NSManager.NAME_EN + " TEXT, " 
			+ NSManager.NAME_AR + " TEXT, " 
			+ NSManager.LINK 	+ " TEXT);";
	
	private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.LINK	+ " TEXT, " 
			+ NSManager.NAME	+ " TEXT, "
			+ NSManager.TID		+ " INTEGER, "
			+ COL_TYPE_ID		+ " TEXT);";
	
	private static final String CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.NAME	+ " TEXT, " 
			+ NSManager.COUNTRY	+ " TEXT, "
			+ NSManager.BODY	+ " TEXT, "
			+ NSManager.DATE	+ " TEXT, "
			+ NSManager.NID 	+ " INTEGER);";
	
	private static final String CREATE_TABLE_FILES = "CREATE TABLE " + TABLE_FILES
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.TID		+ " INTEGER, " 
			+ NSManager.NAME	+ " TEXT, "
			+ NSManager.COUNT	+ " INTEGER, "
			+ NSManager.LINK	+ " TEXT);";
	
	private static final String CREATE_TABLE_AUTHORS = "CREATE TABLE " + TABLE_AUTHORS
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.TID		+ " INTEGER, " 
			+ NSManager.NAME	+ " TEXT, "
			+ NSManager.COUNT	+ " INTEGER, "
			+ NSManager.LINK	+ " TEXT);";

	private static final String CREATE_TABLE_POLLS = "CREATE TABLE " + TABLE_POLLS
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.QID			+ " INTEGER, " 
			+ NSManager.QUESTION	+ " TEXT, "
			+ NSManager.DATE		+ " TEXT, "
			+ NSManager.LINK		+ " TEXT);";

	private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.QID			+ " INTEGER, " 
			+ NSManager.QUESTION	+ " TEXT);";
	
	private static final String CREATE_TABLE_POLL_CHOICES = "CREATE TABLE " + TABLE_POLL_CHOICES
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ NSManager.CHID	+ " INTEGER, " 
			+ NSManager.CHTEXT	+ " TEXT, "
			+ NSManager.CHVOTES	+ " INTEGER, "
			+ COL_QUESTION_ID	+ " INTEGER);";

	
	//Constructor
	public NSDatabase(Context _context) {

		this.context = _context;
		this.nsDBHelper = new NSSQLiteOpenHelper(this.context);

	}

	// the beginnings our SQLiteOpenHelper class
	private static class NSSQLiteOpenHelper extends SQLiteOpenHelper
	{

		public NSSQLiteOpenHelper(Context context) {
//			super(context, DB_NAME, null, DB_VERSION);
			super(context, context.getExternalFilesDir(null).getAbsolutePath()
		            + File.separator + DB_NAME, null, DB_VERSION);
			Log.e("", "NSSQLiteOpenHelper*******************************");
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			// execute the query string to the database.
			_db.execSQL(CREATE_TABLE_ARTICLES);
			_db.execSQL(CREATE_TABLE_TYPES);
			_db.execSQL(CREATE_TABLE_CATEGORIES);
			_db.execSQL(CREATE_TABLE_COMMENTS);
			_db.execSQL(CREATE_TABLE_FILES);
			_db.execSQL(CREATE_TABLE_AUTHORS);
			_db.execSQL(CREATE_TABLE_POLLS);
			_db.execSQL(CREATE_TABLE_QUESTIONS);
			_db.execSQL(CREATE_TABLE_POLL_CHOICES);

			Log.e("", "database tables creation*******************************");

		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {

			Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
			// on upgrade drop older tables
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLLS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
			_db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLL_CHOICES);
			// create new tables
			onCreate(_db);
		}
		// TODO: override the constructor and other methods for the parent class
	}

	/**
	 * open the db
	 * @return this
	 * @throws SQLException
	 */
	public NSDatabase open() throws SQLException 
	{
		this.db = this.nsDBHelper.getWritableDatabase();
		return this;
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}
	/**
	 * close return type: void
	 */
	public void close() {
		this.nsDBHelper.close();
	}
}
