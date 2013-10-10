package com.example.dudewheresmyplace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CompassView extends View {

	private Paint compassPaint;
	private float pos;

	public CompassView(Context context) {
		super(context);

		pos = 0.0f;
		compassPaint = new Paint();
		compassPaint.setAntiAlias(true);
		compassPaint.setStrokeWidth(2);
		compassPaint.setTextSize(25);
		compassPaint.setStyle(Paint.Style.STROKE);
		compassPaint.setColor(Color.RED);

	}

	public void updatePos(float position) {
		this.pos = position;
		this.invalidate(); // redraw the compass
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawRect(0, 0, this.getMeasuredWidth(),
				this.getMeasuredHeight(), compassPaint);

		float y = this.getMeasuredHeight() / 2;
		float x = this.getMeasuredWidth() / 2;
		// get the min x or y val for radius and make it 1/2 of that value
		float radius = Math.min(x, y) * .5f;

		canvas.drawCircle(x, y, radius, compassPaint);
		// draw the needle
		canvas.drawLine(x, y,
				x + radius * (float) Math.sin((double) pos * (180 * Math.PI)),
				y - radius * (float) Math.cos((double) pos * (180 * Math.PI)),
				compassPaint);

		canvas.drawText(String.valueOf(pos), x, y, compassPaint);
	}

}
