package com.example.gravballalpha.Physics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.example.gravballalpha.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class PhysicsEngine {
	
	private static final String TAG = PhysicsEngine.class.getSimpleName();
	
	private Context context;
	
	private List<StaticObject> staticObjects;
	private List<DynamicObject> dynamicObjects;
	private List<Constraint> constraints;
	
	public loc_2D gravity;
	
	private int bounds_x_min;
	private int bounds_x_max;
	private int bounds_y_min;
	private int bounds_y_max;
	
	private float collisionFriction = (float) 0.9;

	public PhysicsEngine(Context context, int bounds_x_min, int bounds_y_min, int bounds_x_max, int bounds_y_max) {
		
		this.context = context;
		
		this.bounds_x_min = bounds_x_min;
		this.bounds_x_max = bounds_x_max;
		this.bounds_y_min = bounds_y_min;
		this.bounds_y_max = bounds_y_max;
		
		gravity = new loc_2D();
		
		Log.d(TAG, "PhysicsEngine Inisiated. Bounds Rect: (" + this.bounds_x_min + ", " + this.bounds_y_min + ", " + this.bounds_x_max + ", " + this.bounds_y_max + ")");
		
		staticObjects = new ArrayList<StaticObject>();
		dynamicObjects = new ArrayList<DynamicObject>();
		constraints = new ArrayList<Constraint>();
		
	}
	
	public void onSaveInstanceState(Bundle savedState) {
		
		int i = 0;
		
		for (DynamicObject object : dynamicObjects) {
			if (object != null) {
				object.onSaveInstanceState(savedState, i);
				
				i++;
			}
		}
		
		i = 0;
		for (StaticObject object : staticObjects) {
			if (object != null) {
				object.onSaveInstanceState(savedState, i);
				
				i++;
			}
		}
		
		savedState.putInt(this.getClass().getSimpleName() + "_dynamicObjects", dynamicObjects.size());
		savedState.putInt(this.getClass().getSimpleName() + "_staticObjects", staticObjects.size());
		
		gravity.onSaveInstanceState(savedState, 0);
		
		savedState.putInt(this.getClass().getSimpleName() + "_bounds_x_min", bounds_x_min);
		savedState.putInt(this.getClass().getSimpleName() + "_bounds_y_min", bounds_y_min);
		savedState.putInt(this.getClass().getSimpleName() + "_bounds_x_max", bounds_x_max);
		savedState.putInt(this.getClass().getSimpleName() + "_bounds_y_max", bounds_y_max);
		
		savedState.putFloat(this.getClass().getSimpleName() + "_collisionFriction", collisionFriction);
	}
	
	public void onRestoreInstanceState(Bundle savedState) {
		
		loc_2D tmpLoc = new loc_2D();
		
		for (int index = 0; index == savedState.getInt(this.getClass().getSimpleName() + "_dynamicObjects"); index++) {
			
			int objectIndex = addDynamicObject(tmpLoc);
			DynamicObject object = getDynamicObject(objectIndex);
			
			object.onRestoreInstanceState(savedState, index);
		}
		
		for (int index = 0; index == savedState.getInt(this.getClass().getSimpleName() + "_staticObjects"); index++) {
			
			int objectIndex = addStaticObject(tmpLoc);
			StaticObject object = getStaticObject(objectIndex);
			
			object.onRestoreInstanceState(savedState, index);
		}
		
		
		gravity.onRestoreInstanceState(savedState, 0);
		
		bounds_x_min = savedState.getInt(this.getClass().getSimpleName() + "_bounds_x_min");
		bounds_y_min = savedState.getInt(this.getClass().getSimpleName() + "_bounds_y_min");
		bounds_x_max = savedState.getInt(this.getClass().getSimpleName() + "_bounds_x_max");
		bounds_y_max = savedState.getInt(this.getClass().getSimpleName() + "_bounds_y_max");
		
		collisionFriction = savedState.getFloat(this.getClass().getSimpleName() + "_collisionFriction");
	}
	
	public int addStaticObject(loc_2D position) {
		
		staticObjects.add(new StaticObject(context));
		int staticObjectIndex = staticObjects.size() - 1;
		
		StaticObject staticObject = staticObjects.get(staticObjectIndex);
		staticObject.position.set(position);
		
		return staticObjectIndex;
	}
	
	public void removeStaticObject(int index) {
		
		staticObjects.remove(index);
	}
	
	public int addDynamicObject(loc_2D position) {
		
		dynamicObjects.add(new DynamicObject(context));
		int dynamicObjectIndex = dynamicObjects.size() - 1;
		
		DynamicObject dynamicObject = dynamicObjects.get(dynamicObjectIndex);
		dynamicObject.position.set(position);
		
		return dynamicObjectIndex;
	}
	
	public void removeDynamicObject(int index) {
		dynamicObjects.remove(index);
	}
	
	public int addConstraint(boolean object1IsDynamic, boolean object2IsDynamic, int object1Index, int object2Index) {
		
		if (object1IsDynamic) {
			if (object2IsDynamic) {
				constraints.add(new Constraint<DynamicObject, DynamicObject>(dynamicObjects.get(object1Index), dynamicObjects.get(object2Index)));
				//Log.d(TAG, "dynamic, dynamic");
			} else if (!object2IsDynamic) {
				constraints.add(new Constraint<DynamicObject, StaticObject>(dynamicObjects.get(object1Index), staticObjects.get(object2Index)));
				//Log.d(TAG, "dynamic, static");
			}
		} else if (!object1IsDynamic) {
			if (object2IsDynamic) {
				constraints.add(new Constraint<StaticObject, DynamicObject>(staticObjects.get(object1Index), dynamicObjects.get(object2Index)));
				//Log.d(TAG, "static, dynamic");
			} else if (!object2IsDynamic) {
				constraints.add(new Constraint<StaticObject, StaticObject>(staticObjects.get(object1Index), staticObjects.get(object2Index)));
				//Log.d(TAG, "static, static");
			}
		}
		
		int constraintIndex = constraints.size() - 1;
		//Log.d(TAG, "constraint added: " + constraintIndex);
		
		return constraintIndex;
	}
	
	public void removeConstraintObject(int objectIndex) {
		if (constraints.size() > 0) {
			constraints.remove(objectIndex);
			//Log.d(TAG, "constraint Removed");
		}
	}
	
	public loc_2D getDynamicObjectPosition(int index) {
		return dynamicObjects.get(index).position;
	}
	
	public void setGravity(loc_2D gravity) {
		this.gravity.set(gravity);
	}
	
	
	
	public DynamicObject getDynamicObject(int index) {
		
		return dynamicObjects.get(index);
	}
	
	public StaticObject getStaticObject(int index) {
		
		return staticObjects.get(index);
	}
	
	
	
	public boolean checkCollisionPoint(int objectIndex, loc_2D point) {
		
		DynamicObject object = dynamicObjects.get(objectIndex);
		
		return object.checkForCollisionPoint(point);
	}
	
	public boolean checkCollisionCircle(int dynamicObjectIndex, int staticObjectIndex) {
		
		DynamicObject dynamicObject = dynamicObjects.get(dynamicObjectIndex);
		StaticObject staticObject = staticObjects.get(staticObjectIndex);
		
		if (Math.hypot(staticObject.position.getloc_x() - dynamicObject.position.getloc_x(), staticObject.position.getloc_y() - dynamicObject.position.getloc_y()) <= dynamicObject.radius + staticObject.radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public void update(long tick, long time, float timeStep, int FPS) {
		
//		for (Constraint object : constraints) {
//			
//			object.update();
//		}
		
		try {
			synchronized(dynamicObjects) {
				for (DynamicObject object : dynamicObjects) {
					
					object.update(timeStep, gravity);
					object.handleBoundaryCollision(bounds_x_min, bounds_x_max, bounds_y_min, bounds_y_max, collisionFriction);
				}
			}
		} finally {
			
		}
		
		try {
			synchronized(constraints) {
				for (Constraint object : constraints) {
					
					object.update();
				}
			}
		} finally {
			
		}
		
	}
	
	public void render(Canvas canvas) {
		
		for (DynamicObject object : dynamicObjects) {
			if (object != null) {
				object.draw(canvas);
			}
		}
		
		for (StaticObject object : staticObjects) {
			if (object != null) {
				object.draw(canvas);
			}
		}
		
	}

}
