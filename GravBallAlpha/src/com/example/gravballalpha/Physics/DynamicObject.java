package com.example.gravballalpha.Physics;

import com.example.gravballalpha.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import java.lang.Math;

public class DynamicObject extends Object {
	
	private static final String TAG = DynamicObject.class.getSimpleName();
	
	private Bitmap bitmap;
	private Matrix matrix;
	
	public float radius;
	private float circumferance;
	
	public rot_2D rotation;
	public float rotVelocity_z;
	
	public DynamicObject(Context context) {
		super(context);
		//Log.d(TAG, "New Ball Added.");
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
		
		radius = 35;
		circumferance = (float) (Math.PI * (radius * radius));
		
		position = new loc_2D();
		linVelocity = new loc_2D();
		rotation = new rot_2D();
		matrix = new Matrix();
		
		rotVelocity_z = 0;
	}
	
	public void onSaveInstanceState(Bundle savedState, int index) {
		super.onSaveInstanceState(savedState, index);
		
		rotation.onSaveInstanceState(savedState, index);
		
		savedState.putFloat(this.getClass().getSimpleName() + "_radius" + index, radius);
		savedState.putFloat(this.getClass().getSimpleName() + "_circumferance" + index, circumferance);
		savedState.putFloat(this.getClass().getSimpleName() + "_rotVelocity_z" + index, rotVelocity_z);
		
		float[] tmp = new float[9]; 
		matrix.getValues(tmp);
		savedState.putFloatArray(this.getClass().getSimpleName() + "matrix" + index, tmp);
	}
	
	public void onRestoreInstanceState(Bundle savedState, int index) {
		super.onRestoreInstatanceState(savedState, index);
		
		rotation.onRestoreInstanceState(savedState, index);
		
		radius = savedState.getFloat(this.getClass().getSimpleName() + "_radius" + index);
		circumferance = savedState.getFloat(this.getClass().getSimpleName() + "_circumferance" + index);
		rotVelocity_z = savedState.getFloat(this.getClass().getSimpleName() + "_rotVelocity_z" + index);
		
		matrix.setValues(savedState.getFloatArray(this.getClass().getSimpleName() + "matrix" + index));
	}
	
	public void setLocation(loc_2D loc) {
		position.set(loc);
	}
	
	public void setLinVelocity(loc_2D linVelocity) {
		linVelocity.set(linVelocity);
	}
	
	public void setRotation(float rot_z) {
		rotation.setRot_z(rot_z);
	}
	
	public void setRotVelocity(float rotVelocity_z) {
		this.rotVelocity_z = rotVelocity_z;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public boolean checkForCollisionPoint(loc_2D point) {
		
		if(radius >= Math.hypot(point.getloc_x() - position.getloc_x(), point.getloc_y() - position.getloc_y())) {
			return true;
		} else {
			return false;
		}
	}
	
	public void handleBoundaryCollision(int bounds_x_min, int bounds_x_max, int bounds_y_min, int bounds_y_max, float collisionFriction) {
		
		if (position.getloc_x() - radius <= bounds_x_min) {
			linVelocity.setloc_x(linVelocity.getloc_x() * -collisionFriction);
			position.setloc_x(bounds_x_min + radius);
			
			rotVelocity_z = (rotVelocity_z + ((linVelocity.getloc_y() / circumferance) * 360)) * collisionFriction;
			//Log.d(TAG, "Collision with xmin");
		}
		if (position.getloc_x() + radius >= bounds_x_max) {
			linVelocity.setloc_x(linVelocity.getloc_x() * -collisionFriction);
			position.setloc_x(bounds_x_max - radius);
			
			rotVelocity_z = (rotVelocity_z - ((linVelocity.getloc_y() / circumferance) * 360)) * collisionFriction;
			//Log.d(TAG, "Collision with xmax");
		}
		
		if (position.getloc_y() - radius <= bounds_y_min) {
			linVelocity.setloc_y(linVelocity.getloc_y() * -collisionFriction);
			position.setloc_y(bounds_y_min + radius);
			
			rotVelocity_z = (rotVelocity_z - ((linVelocity.getloc_x() / circumferance) * 360)) * collisionFriction;
			//Log.d(TAG, "Collision with ymin");
		}
		if (position.getloc_y() + radius >= bounds_y_max) {
			linVelocity.setloc_y(linVelocity.getloc_y() * -collisionFriction);
			position.setloc_y(bounds_y_max - radius);
			
			rotVelocity_z = (rotVelocity_z + ((linVelocity.getloc_x() / circumferance) * 360)) * collisionFriction;
			//Log.d(TAG, "Collision with ymax");
		}
		
	}
	
//	public boolean checkForCollisionLine(float lineEndPoint_1_x, float lineEndPoint_1_y, float lineEndPoint_2_x, float lineEndPoint_2_y) {
//		
//		Vector U = new Vector()
//		
//		float cos0 = U.get(0) * V.get(0) + U.get(1) * U.get(1);
//		
//		
//		return true;
//	}

	public void update(float timeStep, loc_2D acceleration) {
		
		/* Acceleration */
		linVelocity.set(linVelocity.sum(acceleration.multConst(timeStep)));
		
		/* Velocity */
		position.set(position.sum(linVelocity.multConst(timeStep)));
		
		rotation.setRot_z((rotation.getRot_z() + rotVelocity_z * timeStep));
		
//		Log.d(TAG, "rotational Velocity of ball: " + rotVelocity_z);
//		Log.d(TAG, "ball rotational Position: " + rotation.getRot_z());
		
//		radius = (float)Math.sin((float)time) * (float)10 + (float)15;
		
		/* Position */		
	}
	
	public void draw(Canvas canvas) {
		//Log.d(TAG, "ball rendered: " + position.getloc_x() + ", " + position.getloc_y() + " ( " + matrix.toString());
		
		matrix.reset();
		matrix.postScale(radius / ((float) bitmap.getWidth() / 2),radius / ((float) bitmap.getHeight() / 2));
		matrix.postRotate(rotation.getRot_z(), radius, radius);		
		matrix.postTranslate(position.getloc_x() - radius, position.getloc_y() - radius);

		//matrix.postRotate(rotation.getRot_z(), position.getloc_x(), position.getloc_y());
		//matrix.postScale(bitmap.getWidth() / (radius * 2), bitmap.getHeight() / (radius * 2), position.getloc_x(), position.getloc_y());
		
		
		//canvas.setMatrix(matrix);
		canvas.drawBitmap(bitmap, matrix, null);
		
		//canvas.setMatrix(null);
	}
}