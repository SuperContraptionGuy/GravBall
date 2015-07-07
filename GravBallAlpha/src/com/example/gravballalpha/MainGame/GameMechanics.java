package com.example.gravballalpha.MainGame;

import com.example.gravballalpha.R;
import com.example.gravballalpha.Physics.DynamicObject;
import com.example.gravballalpha.Physics.PhysicsEngine;
import com.example.gravballalpha.Physics.StaticObject;
import com.example.gravballalpha.Physics.loc_2D;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.os.Bundle;
import android.util.Log;

public class GameMechanics {
	
	private static final String TAG = GameMechanics.class.getSimpleName();
	
	private int width;
	private int height;
	
	public boolean gameRunning;
	
	public enum gameState {
		
	}
	
	private int extraPointsZoneHeight;
	private int deathZoneHeight;
	
	private int score;
	private float scoreMultiplier;
	
	private float sensitivityScale;
	
	private PhysicsEngine physics;
	private HUD gui;
	private Background background;
	
	private int ball;
	
	private int cursorObject;
	private int constraint;
	
	private loc_2D averageCursorVelocity;
	
	DynamicObject ballDynamicObject;
	StaticObject cursorStaticObject;
	
	private loc_2D touchPosition;
	private loc_2D touchStartPosition;
	private long touchStartTime;
	private int touchTimeout;
	
	private loc_2D locationPlaceHolder;
	
	private long tick;
	private int FPS;
	private float timeScale;
	private float scaledTimeStep;
	
	public GameMechanics(Context context, int width, int height) {	// Start Up
		Log.d(TAG, "Game Startup...");
		
		this.width = width;
		this.height = height;
		
		extraPointsZoneHeight = height / 8;
		deathZoneHeight = height / 5;
		
		
		locationPlaceHolder = new loc_2D();
		locationPlaceHolder.setXY(width / 2, height / 5);
		
		this.touchPosition = new loc_2D();
		this.touchStartPosition = new loc_2D();
		
		averageCursorVelocity = new loc_2D();
		
		
		physics = new PhysicsEngine(context, 0, 0, width, height + 100);
		physics.gravity.setloc_y(height / 2);
		
		
		ball = physics.addDynamicObject(locationPlaceHolder);
		
		cursorStaticObject = new StaticObject(context);
		ballDynamicObject = new DynamicObject(context);
		
		ballDynamicObject = physics.getDynamicObject(ball);
		
		ballDynamicObject.setRadius(width / 7 / 2);
		
		
		cursorObject = -1;
		constraint = -1;
		
		touchTimeout = 300;
		
		gui = new HUD(context, width, height);
		gui.setTextPadding(width / 24);
		gui.setTimeoutCirclePadding((int) ballDynamicObject.radius);
		gui.setTouchTimeout(touchTimeout);
		
		background = new Background(width, height);
		background.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background));
		background.setZoneHeights(extraPointsZoneHeight, deathZoneHeight);
		background.setPadding(width / 48);
		
		
		sensitivityScale = 1;
		
		score = 0;
		scoreMultiplier = 1;
		
		timeScale = (float) 1;
	}
	
//	public void onSaveInstanceState(SharedPreferences.Editor editor) {
//		
//		physics.onSaveInstanceState(editor);
//		editor.putInt(this.getClass().getSimpleName() + "_score", score);
//	}
//	
//	public void onRestoreInstanceState(SharedPreferences preferences) {
//		Log.d(TAG, "Restoring Data...");
//		
//		physics.onRestoreInstanceState(preferences);
//		score = preferences.getInt(this.getClass().getSimpleName() + "_score", 0);
//	}
	
	public void startGame() {
		gameRunning = true;
	}
	
	public void pauseGame() {
		gameRunning = false;
	}
	
	public boolean onTouched(MotionEvent event) {
		
		//Log.d(TAG, "action: " + event.getAction());
		//Log.d(TAG, "x: " + event.getX() + ", y: " + event.getY());
		
		switch(event.getAction()) {
		case(MotionEvent.ACTION_DOWN):
			//Log.d(TAG, "Touch: Action Down. " + MotionEvent.ACTION_DOWN);
		
			averageCursorVelocity.setXY(0, 0);
		
			touchPosition.setXY(event.getX(), event.getY());
			touchStartPosition.set(touchPosition);
			touchStartTime = System.currentTimeMillis();
			
			cursorObject = physics.addStaticObject(this.touchPosition);
			cursorStaticObject = physics.getStaticObject(cursorObject);

			if (ball != -1) {
				if(physics.checkCollisionCircle(ball, cursorObject) && 
						ballDynamicObject.position.getloc_y() > extraPointsZoneHeight && 
						ballDynamicObject.position.getloc_y() < height - deathZoneHeight) {
					
					constraint = physics.addConstraint(false, true, cursorObject, ball);			
				}
			}
		
			return true;
		case(MotionEvent.ACTION_MOVE):
			//Log.d(TAG, "Touch: Action Move. " + MotionEvent.ACTION_MOVE);
			
			touchPosition.setXY(event.getX(), event.getY());
			cursorStaticObject.position.set(touchPosition);
			
			averageCursorVelocity.set(averageCursorVelocity.sum(touchPosition.diff(touchStartPosition)).multConst((float) 0.5));
			
			return true;
		case(MotionEvent.ACTION_UP):
			//Log.d(TAG, "Touch: Action Up. " + MotionEvent.ACTION_UP);
			
			if (constraint != -1) {
			
				physics.removeConstraintObject(constraint);
				constraint = -1;
				
				ballDynamicObject.linVelocity.set(averageCursorVelocity.multConst(1 / ((float) (System.currentTimeMillis() - touchStartTime) / 1000)).multConst(sensitivityScale));
				
			}
		
			physics.removeStaticObject(cursorObject);
			
			cursorObject = -1;
			cursorStaticObject = null;
		
			return true;
		default:
			return false;
		}
		
	}
	
	private float gravityCurve(float startingGravity, float period, float amplitude, float time) {
		
		float increasingPeriod = period / 2;
		
		if (time % period > increasingPeriod) {
			
			//Log.d(TAG, "cos wave");
			
			return (float) Math.cos((time * Math.PI) / increasingPeriod) * (amplitude / 2) + (amplitude / 2) + (Math.round(time / period) * amplitude - amplitude) + startingGravity;
		} else {
			
			//Log.d(TAG, "flat");
			return (Math.round(time / period) * amplitude) + startingGravity;
		}
		
	}
	
	public void update(long tick, long time, float timeStep, int FPS) {	// Update Game State
		//Log.d(TAG, "Game Update...");
		
		scaledTimeStep = timeStep * timeScale;
		
		if (ball != -1) {
			
			score += ((float) scaledTimeStep * (float) 1000 * scoreMultiplier);

			
			physics.gravity.setloc_y(gravityCurve(200, 15000, 100, time));
			
			if (System.currentTimeMillis() - touchStartTime >= touchTimeout && constraint != -1) {
				physics.removeConstraintObject(constraint);
				constraint = -1;
				gui.setRenderTimeoutCircle(false);
				ballDynamicObject.linVelocity.set(averageCursorVelocity.multConst(1 / ((float) (System.currentTimeMillis() - touchStartTime) / 1000)).multConst(sensitivityScale));
			}
			
			if (ballDynamicObject.position.getloc_y() < extraPointsZoneHeight - ballDynamicObject.radius) {
				scoreMultiplier = (float) 100;
			} else {
				scoreMultiplier = 1;
			}
			
			if (ballDynamicObject.position.getloc_y() > height + ballDynamicObject.radius) {
				physics.removeDynamicObject(ball);
				ball = -1;
				ballDynamicObject = null;
				
				gameRunning = false;
			}
		}
		
		//background.setPadding((int) (Math.sin(time) * (float) 5 + 5));
		
		background.update();
		physics.update(tick, time, scaledTimeStep, FPS);
		gui.update((int) score, gravityCurve(50,  15000,  100,  time), FPS);
		
		if (constraint != -1) {
			gui.setRenderTimeoutCircle(true);
			gui.setTimeoutCircle(ballDynamicObject.position, (int) ballDynamicObject.radius);
			gui.setTimeoutCircleProgress((int) (System.currentTimeMillis() - touchStartTime));
		} else {
			gui.setRenderTimeoutCircle(false);
		}
		
		this.tick = tick;
		this.FPS = FPS;
		
		
	}
	
	public void render(Canvas canvas, Paint paint) {	// Render
		//Log.d(TAG, "Game Render...");
		
		paint.setColor(Color.BLACK);	//clear the window
		canvas.drawPaint(paint);
		
		background.render(canvas, paint);
		physics.render(canvas);
		
		gui.render(canvas, paint);
		
	}

}
