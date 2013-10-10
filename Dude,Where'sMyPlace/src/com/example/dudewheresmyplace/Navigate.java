package com.example.dudewheresmyplace;

import android.app.Activity;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Navigate extends Activity {

	private LocationManager locationManager;
	private Location currentLocation; // store user current location
	private Location newLocation; // the destination
	private LocationView locationView;
	private double distanceTraveled;
	private double totalDistance;
	private double distanceLeft;

	private TextView totalMilesView;
	private TextView milesLeftView;
	private TextView locationNameView;
	private RelativeLayout navigateLayout;

	private long rowID; // id of a row of data
	private String bestProvider;

	public static final double METERS_TO_MILES = 0.000621371;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigate_view);

		// add location view for arrow pointer to the XML layout
		locationView = new LocationView(this);
		navigateLayout = (RelativeLayout) findViewById(R.id.navigateView);
		navigateLayout.addView(locationView);

		totalMilesView = (TextView) findViewById(R.id.totalMiles);
		milesLeftView = (TextView) findViewById(R.id.milesLeft);
		locationNameView = (TextView) findViewById(R.id.navigateTo);

		// get row id of location picked
		Bundle extra = getIntent().getExtras();
		rowID = extra.getLong("row");

		// get the location service
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	protected void onStart() {
		super.onStart();

		Criteria criteria = new Criteria();

		bestProvider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(bestProvider, 0, 0,
				locationListener);

	}

	private LocationListener locationListener = new LocationListener() {

		private Location previousLocation; // previous reported location

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			if (previousLocation != null) {
				// add to the total distanceTraveled
				distanceTraveled += location.distanceTo(previousLocation);
			}

			double currentLongitude = location.getLongitude() * 1E6;
			double currentLatitude = location.getLatitude() * 1E6;

			// create new location which user is at
			currentLocation = new Location(bestProvider);
			currentLocation.setLatitude(currentLatitude);
			currentLocation.setLongitude(currentLongitude);

			// calculate total miles traveled
			distanceTraveled *= METERS_TO_MILES;
			distanceLeft = totalDistance - distanceTraveled;
			milesLeftView.setText("Miles Left: " + distanceLeft);

			previousLocation = location;
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	};

	protected void onResume() {
		super.onResume();
		// execute database operation for specified row/location
		new LoadLocation().execute(rowID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_navigate, menu);
		return true;
	}

	private class LoadLocation extends AsyncTask<Long, Object, Cursor> {
		DatabaseConnector db = new DatabaseConnector(Navigate.this);

		@Override
		protected Cursor doInBackground(Long... params) {
			// TODO Auto-generated method stub
			db.open();
			// get cursor with all data of user selected location
			return db.getLocation(params[0]);
		}

		@Override
		protected void onPostExecute(Cursor result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			result.moveToFirst(); // move to first row item
			// get column index for each item
			int nameIndex = result.getColumnIndex("name");
			int longitudeIndex = result.getColumnIndex("longitude");
			int latitudeIndex = result.getColumnIndex("latitude");

			// get the values in the columns
			String locationName = result.getString(nameIndex);
			Double locationLongitude = result.getDouble(longitudeIndex);
			Double locationLatitude = result.getDouble(latitudeIndex);

			// set the title of location user is navigating to
			locationNameView.setText("Navigate To: " + locationName.toString());

			// the destination
			newLocation = new Location(bestProvider);
			newLocation.setLatitude(locationLatitude.doubleValue() / 1E6);
			newLocation.setLongitude(locationLongitude.doubleValue() / 1E6);
			
			//update destination to have arrow point to location
			locationView.updateLocation(newLocation);

			// current position
			Location currentLoc = locationManager
					.getLastKnownLocation(bestProvider);

			// get distance from current location to destination
			double distance = currentLoc.distanceTo(newLocation);
			totalDistance = distance * METERS_TO_MILES;
			totalMilesView.setText("Total miles: " + (totalDistance) + " ");

			result.close();
			db.close();
		}

	}

}
