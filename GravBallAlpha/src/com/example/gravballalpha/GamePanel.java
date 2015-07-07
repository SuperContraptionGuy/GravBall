package com.example.gravballalpha;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gravballalpha.MainGame.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = GamePanel.class.getSimpleName();
	private GameLoopThread thread;
	private GameMechanics game;
	
	
	public GamePanel(Context context) {
		super(context);
		
		getHolder().addCallback(this);	//tells android to send the input here
		
		setFocusable(true); // dido
	}
	
	public void onSaveInstanceState(SharedPreferences.Editor editor) {
		
		game.onSaveInstanceState(editor);
		thread.onSaveInstanceState(editor);
	}
	
	public void onRestoreInstatnceState(SharedPreferences preferences) {
		
		game.onRestoreInstanceState(preferences);
		thread.onRestoreInstanceState(preferences);
	}
	
	private void startThread() {
		try {
			thread = new GameLoopThread(getHolder(), this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		thread.start();
	}
	
	private void stopThread() {
		thread.setRunning(false);
		boolean retry = true;
		while(retry) {
			try {
				thread.join();	//stop the game loop
				retry = false;
			} catch(InterruptedException e) {
				//do nothing and try again
			}
		}
		
//		thread = null;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {	//if the screen orientation changes
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {	//on startup
		Log.d(TAG, "Created...");
		
		
		game = new GameMechanics(getContext(), this.getWidth(), this.getHeight());
		game.startGame();
		
		Log.d(TAG, "Starting GameLoopThread...");
		startThread();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {	//on stopping
		Log.d(TAG, "Destroying Surface...");
		stopThread();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	//when the screen is touched
		
		//Log.d(TAG, "Touched.");
		
		if (game.gameRunning) {
			boolean value = game.onTouched(event);
			
			if (value) {
				return value;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	public void onBackPressed() {
		
	}
	
	public void onUpdate(long tick, long time, float timeStepMs, int FPS) {	//called by the MainThread to update game state
		
		float timeStep = timeStepMs / (float) 1000;
		
		//Update State
		
		//if (game.gameRunning) {
			game.update(tick, time, timeStep, FPS);
		//}
		
		
	}
	
	private Paint paint = new Paint();
	
	@Override
	public void onDraw(Canvas canvas) {	//called by the mainThread to draw to the screen
		if (canvas != null) {
			//Render
			
			//if (game.gameRunning)
				game.render(canvas, paint);
			
		}
	}
}

