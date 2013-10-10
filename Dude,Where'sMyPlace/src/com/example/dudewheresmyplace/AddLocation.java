package com.example.dudewheresmyplace;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddLocation extends Activity {

	private LocationManager locationManager;

	private TextView longitude;
	private TextView latitude;
	private Button addLocation;
	private EditText locationName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_add);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		longitude = (TextView) findViewById(R.id.longitudeTag);
		latitude = (TextView) findViewById(R.id.latitudeTag);
		addLocation = (Button) findViewById(R.id.locationPick);
		locationName = (EditText) findViewById(R.id.locationTag);
	}

	protected void onStart() {
		super.onStart();
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		// get the best provider
		String bestProvider = locationManager.getBestProvider(criteria, true);
		// register the listener for location changes
		locationManager.requestLocationUpdates(bestProvider, 0, 0,
				locationListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_location, menu);
		return true;
	}

	public void locationPick(View v) { // button handler for adding location
		if (locationName.getText().length() != 0) {
			AsyncTask<Object, Object, Object> saveLocation = new AsyncTask<Object, Object, Object>() {
				// save this contact using an Asynchronous thread
				@Override
				protected Object doInBackground(Object... arg0) {
					// TODO Auto-generated method stub
					AddLocation.this.saveLocation(); // save location in this
														// process
					return null;
				}

				@Override
				protected void onPostExecute(Object result) {
					// TODO Auto-generated method stub
					// send user to location list
					startActivity(new Intent(AddLocation.this,
							LocationList.class));
				}

			};
			// save contact to database
			saveLocation.execute((Object[]) null);
		}
	}

	private void saveLocation() {
		// connect to SQLite database
		DatabaseConnector db = new DatabaseConnector(AddLocation.this);

		// get the index of the string "Latitude: " and "Longitude: "
		// so that we can get the value after that text which is the
		// actual latitude and longitude values
		int latitudeText = latitude.getText().toString().indexOf(" ");
		int longitudeText = longitude.getText().toString().indexOf(" ");
		String latitudeVal = latitude.getText().toString()
				.substring(latitudeText);
		String longitudeVal = longitude.getText().toString()
				.substring(longitudeText);

		// insert location info in database
		db.insertLocation(locationName.getText().toString(), latitudeVal,
				longitudeVal);
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// get the longitude latitude values
			Double newlongitude = location.getLongitude() * 1E6;
			Double newlatitude = location.getLatitude() * 1E6;

			longitude.setText("Longitude: " + newlongitude.toString());
			latitude.setText("Latitude: " + newlatitude.toString());

			latitude.invalidate();
			longitude.invalidate();
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

}
