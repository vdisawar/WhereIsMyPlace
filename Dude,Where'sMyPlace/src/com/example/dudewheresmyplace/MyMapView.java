package com.example.dudewheresmyplace;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapView extends MapActivity {

	private MapView mapView;
	private MapController mapController;
	private FrameLayout mapLayout;
	private LocationManager locationManager;

	private RouteOverlay overlay; // nested inner class

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		// get a reference to the XML layout this activity is in
		mapLayout = (FrameLayout) findViewById(R.id.mapLayout);
		// create a new google map view
		mapView = new MapView(this, getResources().getString(
				R.string.maps_API_key));
		// add the google map to the layout
		mapLayout.addView(mapView);

		mapView.setClickable(true); // allows for user interactions
		mapView.setEnabled(true); // enable the map to allow for interactions
		mapView.setSatellite(false);
		mapView.setBuiltInZoomControls(true); // allow for zooming

		mapController = mapView.getController(); // map views manipulations
		mapController.setZoom(20);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		overlay = new RouteOverlay();
		mapView.getOverlays().add(overlay);
	}

	protected void onStart() {
		super.onStart();

		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // fine location data

		// get location updates from best provider
		String bestProvider = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(bestProvider, 10, 10,
				locationListener);

		mapView.invalidate();
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			overlay.setLocation(location);

			Double longitude = location.getLongitude() * 1E6;
			Double latitude = location.getLatitude() * 1E6;

			GeoPoint geoPt = new GeoPoint(latitude.intValue(),
					longitude.intValue());

			mapController.animateTo(geoPt); // set view to current location

			mapView.invalidate(); // redraw based on updated location
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map_view, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// draw on the map
	protected class RouteOverlay extends Overlay {
		private Paint locationpaint;
		private Location location;

		public RouteOverlay() {
			locationpaint = new Paint();
			locationpaint.setColor(Color.MAGENTA);
			locationpaint.setStyle(Paint.Style.FILL);
			locationpaint.setAntiAlias(true);
		}

		public void setLocation(Location location) {
			this.location = location; 
		}

		@Override
		public void draw(Canvas canvas, MapView map, boolean arg2) {
			super.draw(canvas, map, arg2);
			if (location != null) {
				Double longitude = location.getLongitude() * 1E6;
				Double latitude = location.getLatitude() * 1E6;
				GeoPoint geoPt = new GeoPoint(latitude.intValue(),
						longitude.intValue());
				// get geo point of users location and convert it to a point to
				// draw on the canvas
				Point point = new Point();
				map.getProjection().toPixels(geoPt, point);
				// draw the point where the user is at
				canvas.drawCircle(point.x, point.y, 10, locationpaint);
			}
		}

	}
}
