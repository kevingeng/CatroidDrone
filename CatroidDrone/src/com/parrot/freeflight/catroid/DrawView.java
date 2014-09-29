package com.parrot.freeflight.catroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class DrawView extends View {
	Bitmap bitmap;
	Canvas bitmapCanvas;

	boolean isInitialized;
	Paint paint = new Paint();

	public DrawView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);

		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		isInitialized = false;
	}

	private void init() {
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);

		bitmapCanvas = new Canvas();
		bitmapCanvas.setBitmap(bitmap);
		bitmapCanvas.drawColor(Color.BLACK);

		isInitialized = true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!isInitialized)
			init();

		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(3);
		canvas.drawRect(30, 30, 80, 80, paint);
		paint.setStrokeWidth(0);
		paint.setColor(Color.CYAN);
		canvas.drawRect(33, 60, 77, 77, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawRect(33, 33, 77, 60, paint);

		canvas.drawRect(130, 30, 80, 80, paint);
		paint.setStrokeWidth(0);
		paint.setColor(Color.CYAN);
		canvas.drawRect(133, 60, 77, 77, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawRect(133, 33, 77, 60, paint);

		paint.setColor(Color.MAGENTA);
		paint.setTextSize(50);
		canvas.drawText("Hello Drawer", 50, canvas.getHeight() / 2, paint);
	}
}