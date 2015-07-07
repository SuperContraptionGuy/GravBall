package com.example.gravballalpha.Physics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;

import com.example.gravballalpha.DrawableObject;

public class Object /*extends DrawableObject */{
	
	public loc_2D position;
	public loc_2D linVelocity;
	
	public Object(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void onSaveInstanceState(Bundle savedState, int index) {
		
		position.onSaveInstanceState(savedState, index);
		linVelocity.onSaveInstanceState(savedState, index);
	}
	
	public void onRestoreInstatanceState(Bundle savedState, int index) {
		
		position.onRestoreInstanceState(savedState, index);
		linVelocity.onRestoreInstanceState(savedState, index);
	}
}
