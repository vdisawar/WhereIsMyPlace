package com.example.dudewheresmyplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnector {

	private DatabaseOpenHelper databaseHelper;
	private SQLiteDatabase sqlDatabase;

	private static final String DATABASE_NAME = "User Locations";
	private static final String TABLE_NAME = "locations";

	public DatabaseConnector(Context context) {
		// create the database
		databaseHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	}

	public void open() {
		// get a database that is able to be written/edited
		sqlDatabase = databaseHelper.getWritableDatabase();
	}

	public void close() {
		if (sqlDatabase != null)
			sqlDatabase.close();
		// close database to conserve resources
	}

	public void insertLocation(String name, String latitude, String longitude) {
		ContentValues newLocation = new ContentValues();
		// put the name and the associated longitude and latitude value
		// of the location into the database
		newLocation.put("name", name);
		newLocation.put("latitude", latitude);
		newLocation.put("longitude", longitude);
		// open the database, insert the content value, and then close
		this.open();
		sqlDatabase.insert(TABLE_NAME, null, newLocation);
		this.close();
	}

	public Cursor getAllLocations() {
		// get the array of names and id of locations stored
		return sqlDatabase.query(TABLE_NAME, new String[] { "_id", "name" },
				null, null, null, null, "name"); // order list by name
	}

	public Cursor getLocation(long id) {
		// get all the information for a specified location
		return sqlDatabase.query(TABLE_NAME, null, "_id=" + id, null, null,
				null, null);
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper {

		public DatabaseOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String create = "CREATE TABLE locations"
					+ "(_id integer primary key autoincrement, "
					+ "name TEXT, latitude TEXT, longitude TEXT);";
			db.execSQL(create);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}
}
