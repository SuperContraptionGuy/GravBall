package com.example.gravballalpha;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;



public class GameLoopThread extends Thread {
	
	private static final String TAG = GameLoopThread.class.getSimpleName();
	
	private Canvas canvas;
	
	private long startTime;
	private long frameStartTime;
	private long frameElapsedTime;
	private long targetElapsedTime;
	private int targetFPS = 60;
	private int FPS;
	private int renderedFrames = 0;
	
	private SurfaceHolder surfaceHolder;
	private GamePanel gamePanel;
	private boolean running = true;
	private long tick = 0L;
	
	
	
	public GameLoopThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) throws InterruptedException {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;

	}
	
	public void onSaveInstanceState(SharedPreferences.Editor editor) {
		
		editor.putLong(this.getClass().getSimpleName() + "_startTime", startTime);
		editor.putInt(this.getClass().getSimpleName() + "_renderedFrames", renderedFrames);
		editor.putLong(this.getClass().getSimpleName() + "_tick", tick);
	}
	
	public void onRestoreInstanceState(SharedPreferences preferences) {
		
		startTime = preferences.getLong(this.getClass().getSimpleName() + "_startTime", 0);
		renderedFrames = preferences.getInt(this.getClass().getSimpleName() + "_renderedFrames", 0);
		tick = preferences.getLong(this.getClass().getSimpleName() + "_tick", 0);
	}
	
	public void restart() {
		tick = 0;
		startTime = System.currentTimeMillis();
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
	private long getElapsedTime(long startTime) {
		return System.currentTimeMillis() - startTime;
	}
	
	@SuppressLint("WrongCall")
	@Override
	public void run() {
		Log.d(TAG, "GameLoopThread run() starting...");
		startTime = System.currentTimeMillis();
		
		while(running) {	//The Game Loop
			tick++;
			
			frameStartTime = System.currentTimeMillis();
			
			targetElapsedTime = (int)(tick * ((float)1 / targetFPS * 1000));

			gamePanel.onUpdate(tick, frameStartTime - startTime, 1000 / (float) targetFPS, FPS);	// Update Game State every tick
			
			
			if(!(getElapsedTime(startTime) >= targetElapsedTime)) {	// Skip render stage if we are not achieving frame rate
				//Log.d(TAG, "Rendering... " + "Elapsed Time: " + getElapsedTime(startTime) + ", Target Time: " + targetElapsedTime);
				
				try {
					canvas = this.surfaceHolder.lockCanvas();
					synchronized(surfaceHolder) {
	
						this.gamePanel.onDraw(canvas);	// Render
						
					}
				} finally {
					if(this.canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				
				renderedFrames++;
				
				if(getElapsedTime(startTime) < targetElapsedTime) {
					while(getElapsedTime(startTime) < targetElapsedTime) {
						//Log.d(TAG, "Sleeping... " + "Elapsed Time: " + getElapsedTime(startTime) + ", Target Time: " + targetElapsedTime);
					}
				} else {
					//Log.d(TAG, "OverTime. " + "Elapsed Time: " + getElapsedTime(startTime) + ", Target Time: " + targetElapsedTime);
				}
				
				
				frameElapsedTime = System.currentTimeMillis() - frameStartTime;
				
				try {
					FPS = 1000 / (int)frameElapsedTime;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "A Very Fast Frame just happened...");
					e.printStackTrace();
				}
				
			} else {
				//Log.d(TAG, "Render Skipped. " + "Elapsed Time: " + getElapsedTime(startTime) + ", Target Time: " + targetElapsedTime);
			}
			
			
			if(running) {
				//Log.d(TAG, "FPS: " + FPS + ", Target: " + targetFPS + ", Tick: " + tick);
			} else {
				//Log.d(TAG, "last run. Stopping...");
			}
			
			
		}
		Log.d(TAG, "GameLoop stopped. Rendered " + renderedFrames + " Frames and had " + tick + " Ticks.");
	}
}