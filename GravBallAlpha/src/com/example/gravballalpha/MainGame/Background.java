package com.example.gravballalpha.MainGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;

public class Background {
	
	private Bitmap backgroundBitmap;
	
	private Matrix matrix;
	private int width;
	private int height;
	
	private int padding;
	
	private int extraPointsZoneHeight;
	private int deathZoneHeight;
	private RectF rect;

	public Background(int width, int height) {

		matrix = new Matrix();
		this.width = width;
		this.height = height;
		
		rect = new RectF();
		
	}
	
	public void onSaveInstanceState(Bundle savedState) {
		
	}
	
	public void onRestoreInstatnceState(Bundle savedState) {
		
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.backgroundBitmap = bitmap;
	}
	
	public void setZoneHeights(int extraPointsZone, int deathZone) {
		this.extraPointsZoneHeight = extraPointsZone;
		this.deathZoneHeight = deathZone;
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	
	
	public void update() {
		
	}
	
	public void render(Canvas canvas, Paint paint) {
		matrix.reset();
		matrix.postTranslate(-backgroundBitmap.getWidth() / 2, 0);
		matrix.postScale(height / ((float) backgroundBitmap.getHeight()),height / ((float) backgroundBitmap.getHeight()));
		matrix.postTranslate(width / 2, 0);
		canvas.drawBitmap(backgroundBitmap, matrix, null);
		
		
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(60, 0, 100, 255);
		rect.set(padding, padding, width - padding, (int) (extraPointsZoneHeight - padding / 2));
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(padding / 2);
		paint.setARGB(150, 0, 100, 255);
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);

		
		
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(60, 0, 255, 100);
		rect.set(padding, (int) (extraPointsZoneHeight + padding / 2), width - padding, (int) ((height - deathZoneHeight) - padding / 2));
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(padding / 2);
		paint.setARGB(150, 0, 255, 100);
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);

		
		
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(60, 255, 0, 0);
		rect.set(padding, (int) ((height - deathZoneHeight) + padding / 2), width - padding, height + 100);
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(padding / 2);
		paint.setARGB(150, 255, 0, 0);
		canvas.drawRoundRect(rect, (float) padding, (float) padding, paint);
		
	}

}
