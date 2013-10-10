package com.example.dudewheresmyplace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Location;
import android.view.View;

public class LocationView extends View {

	private Point point;
	private Matrix matrix;
	private Bitmap bmp;

	public LocationView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		point = new Point();
		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.needle);
		matrix = new Matrix();

	}

	public void updateLocation(Location location) {
		Double longitude = location.getLongitude();
		Double latitude = location.getLatitude();

		// convert the latitude and longitude to a point
		point.set(latitude.intValue(), longitude.intValue());

		this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		// rotate bitmap image of compass towards location
		double angle = 0;
		if (point.x != 0)
			angle = Math.atan(point.y / point.x) * (180 / Math.PI); // convert
																	// radians
																	// to
																	// degrees

		matrix.postRotate((float) angle);

		Bitmap rotatedBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);

		canvas.drawBitmap(rotatedBmp, 270, (int) getHeight() / 4, null);
	}
}
