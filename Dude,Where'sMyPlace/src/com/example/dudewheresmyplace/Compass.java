package com.example.dudewheresmyplace;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Compass extends Activity {

	private LocationManager locationManager;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Sensor magnetometer;
	private CompassView compassView;

	private RelativeLayout compassLayout;
	private TextView longitude;
	private TextView latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		compassView = new CompassView(this);

		setContentView(R.layout.compass_view); // set the layout to the XML view
		// get an id to the relative layout
		compassLayout = (RelativeLayout) findViewById(R.id.compassView);
		compassLayout.addView(compassView); // add the compassView to the
											// relative layout

		// get the location and sensor services
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// get the sensors for accelerometer and magnetometer
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		// get references to the layouts
		longitude = (TextView) findViewById(R.id.longitude);
		latitude = (TextView) findViewById(R.id.latitude);
	}

	protected void onStart() {
		super.onStart();

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		// register listeners for sensors
		sensorManager.registerListener(sensorListener, accelerometer,
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(sensorListener, magnetometer,
				SensorManager.SENSOR_DELAY_UI);
		// register the listener for location
		String bestProvider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(bestProvider, 10, 10,
				locationListener);
	}

	private SensorEventListener sensorListener = new SensorEventListener() {

		float[] gravity;
		float[] geomagnetic;

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// obtain values for the rotation matrix
			// obtain gravity and magnetic field
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				gravity = event.values;
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				geomagnetic = event.values;
			if (gravity != null && geomagnetic != null) {
				// identity matrices R and I
				float[] R = new float[9]; // return val from rotation matrix to
											// be put into getOrientation()
				float[] I = new float[9];

				boolean work = SensorManager.getRotationMatrix(R, I, gravity,
						geomagnetic);

				if (work) {
					// create array for getOriention to store values in
					float[] values = new float[3];
					SensorManager.getOrientation(R, values);
					float azimuth = event.values[0]; // index 0 has the azimuth
					compassView.updatePos(azimuth); // update according to
													// azimuth
				}
			}
		}

	};

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// get the longitude latitude values
			Double newlongitude = location.getLongitude() * 1E6;
			Double newlatitude = location.getLatitude() * 1E6;
			// set the views to that longitude latitude values
			latitude.setText("Latitude: " + newlatitude.toString());
			longitude.setText("Longitude: " + newlongitude.toString());

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

	protected void onDestroy() {
		super.onDestroy();
		sensorManager.unregisterListener(sensorListener); // save resources
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_compass, menu);
		return true;
	}

}
