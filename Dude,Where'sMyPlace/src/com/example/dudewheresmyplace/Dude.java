package com.example.dudewheresmyplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Dude extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dude);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dude, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		if (item.getItemId() == R.id.compassMenu)
			startActivity(new Intent(this, Compass.class));
		else if (item.getItemId() == R.id.locationAddMenu)
			startActivity(new Intent(this, AddLocation.class));
		else if (item.getItemId() == R.id.locationListMenu)
			startActivity(new Intent(this, LocationList.class));
		else if (item.getItemId() == R.id.mapViewMenu)
			startActivity(new Intent(this, MyMapView.class));
		return super.onOptionsItemSelected(item);
	}

}
