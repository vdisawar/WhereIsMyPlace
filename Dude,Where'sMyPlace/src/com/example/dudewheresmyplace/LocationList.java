package com.example.dudewheresmyplace;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LocationList extends ListActivity {

	private ListView locationListView;
	private CursorAdapter locationAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locationListView = getListView();
		locationListView.setOnItemClickListener(locationNavigateListener);
		
		//add a header to the list
		LayoutInflater inflater = getLayoutInflater();
		View headerView = inflater.inflate(R.layout.location_list_header,
				(ViewGroup) findViewById(R.id.locationListHeader));
		locationListView.addHeaderView(headerView);

		// map each location name with a textview in the list
		String[] start = new String[] { "name" };
		int[] end = new int[] { R.id.locationList };
		int flag = 1;
		// able to use non-deprecated constuctor in API version 11 and up
		if (Build.VERSION.SDK_INT >= 11) {
			locationAdapter = new SimpleCursorAdapter(this,
					R.layout.location_list, null, start, end, flag);
		} else {
			locationAdapter = new SimpleCursorAdapter(this,
					R.layout.location_list, null, start, end);
		}
		// provide cursor for listView
		this.setListAdapter(locationAdapter);
	}

	protected void onResume() {
		super.onResume();
		// create a new AsyncTask of getlocations and execute
		// in a separate thread
		new GetLocations().execute((Object[]) null);
	}

	protected void onStop() {
		super.onStop();
		Cursor cursor = locationAdapter.getCursor();
		if (cursor != null)
			cursor.close(); // close adapter
		// let go of cursor in adapter
		locationAdapter.changeCursor(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_location_list, menu);
		return true;
	}

	private OnItemClickListener locationNavigateListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int index,
				long row) {
			// TODO Auto-generated method stub
			// send details of location user picked to navigate activity
			startActivity(new Intent(LocationList.this, Navigate.class)
					.putExtra("row", row));
		}

	};

	private class GetLocations extends AsyncTask<Object, Object, Cursor> {
		// instantiate DatabaseConnector class to connect to SQLite database
		DatabaseConnector db = new DatabaseConnector(LocationList.this);

		@Override
		protected Cursor doInBackground(Object... params) {
			// TODO Auto-generated method stub
			// open database and get all locations
			db.open();
			// this cursor is passed to onPostExecute
			return db.getAllLocations();
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			// set the cursor
			locationAdapter.changeCursor(result);
			db.close();
		}
	}
}
