package com.example.gravballalpha.Physics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

public class StaticObject extends Object {
	
	private Paint paint;
	
	public rot_2D rotation;
	
	public int radius = 10;
	
	private Bitmap bitmap;

	public StaticObject(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		position = new loc_2D();
		rotation = new rot_2D();
		
		paint = new Paint();
	}
	
	public void onSaveInstanceState(Bundle savedState, int index) {
		
		position.onSaveInstanceState(savedState, index);
		rotation.onSaveInstanceState(savedState, index);
		savedState.putInt(this.getClass().getSimpleName() + "_radius" + index, radius);
	}
	
	public void onRestoreInstanceState(Bundle savedState, int index) {
		
		position.onRestoreInstanceState(savedState, index);
		rotation.onRestoreInstanceState(savedState, index);
		radius = savedState.getInt(this.getClass().getSimpleName() + "_radius" + index);
	}
	
	public void setLocation(loc_2D position) {
		this.position.set(position);
	}
	
	public void update() {
		
	}
	
	public void draw(Canvas canvas) {
		
		paint.setColor(Color.BLUE);
		
		canvas.drawCircle(position.getloc_x(), position.getloc_y(), radius, paint);
		
	}

}
