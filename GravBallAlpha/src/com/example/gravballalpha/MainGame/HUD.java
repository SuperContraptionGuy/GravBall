package com.example.gravballalpha.MainGame;

import java.text.DecimalFormat;

import com.example.gravballalpha.Physics.loc_2D;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;

public class HUD {
	
	private static final String TAG = HUD.class.getSimpleName();
	
	private Typeface irresistorFont;
	private Typeface blockFont;
	
	private int width;
	private int height;
	private int padding;
	
	private int score;
	private String scoreString;
	private Rect textBounds;
	private boolean textChanged;
	private int scoreTextSize;
	
	
	private RectF touchTimeoutCircle;
	private int timeoutCirclePadding;
	private int touchTimeout;
	private float timeoutCircleProgress;
	private boolean renderTimeoutCircle;

	
	
	private float gravity;
	private int FPS;
	
	DecimalFormat formatter;

	public HUD(Context context, int width, int height) {
		
		irresistorFont = Typeface.createFromAsset(context.getAssets(), "fonts/Irresistor.ttf");
		blockFont = Typeface.createFromAsset(context.getAssets(), "fonts/DesinerBlock.ttf");
	
		formatter = new DecimalFormat("#,###");
		
		textBounds = new Rect();
		
		scoreString = new String();
		
		this.width = width;
		this.height = height;
		
		textChanged = true;
		scoreTextSize = 1;
		
		padding = 10;
		
		touchTimeoutCircle = new RectF();
		timeoutCirclePadding = 0;
		touchTimeout = 3000;
	}
	
	public void onSaveInstanceState(Bundle savedState) {
		
	}
	
	public void onRestoreInstatnceState(Bundle savedState) {
		
	}
	
	private int determineFontSize(Paint paint, String string, int goalWidth, int goalHeight, int guess) {

		int incr_text_size = 1;
		boolean found_desired_size = false;

		while (!found_desired_size){
		    paint.setTextSize(incr_text_size);// have this the same as your text size

		    paint.getTextBounds(string, 0, string.length(), textBounds);
		    
//		    Log.d(TAG, "goalBounds: (" + goalWidth + ", " + goalHeight + ")");
//		    Log.d(TAG, "textBounds: (" + textBounds.width() + ", " + textBounds.height() + ")" + ", textSize: " + incr_text_size);
		    
//			if (goalHeight >= bounds.height() && goalWidth >= bounds.width()) {
				if (goalHeight - padding * 2 <= textBounds.height() || goalWidth - padding * 2 <= textBounds.width()) {
					found_desired_size = true;
					//Log.d(TAG, "found desired size: " + (incr_text_size - 1));
				} else {
					incr_text_size++;
					//Log.d(TAG, "make bigger");
				}
				
//			} else {
//				incr_text_size--;
//				Log.d(TAG, "make smaller");
//			}
		}
		return incr_text_size - 1;
	}
	
	public void setTouchTimeout(int time) {
		this.touchTimeout = time;
	}
	
	public void setTimeoutCirclePadding(int padding) {
		this.timeoutCirclePadding = padding;
	}
	
	public void setTimeoutCircle(loc_2D position, int radius) {
		
		touchTimeoutCircle.set(
			position.getloc_x() - radius - timeoutCirclePadding, 
			position.getloc_y() - radius - timeoutCirclePadding, 
			position.getloc_x() + radius + timeoutCirclePadding, 
			position.getloc_y() + radius + timeoutCirclePadding
		);
				
	}
	
	public void setTimeoutCircleProgress(int time) {
		this.timeoutCircleProgress = time / (float) touchTimeout;
	}
	
	public void setRenderTimeoutCircle(boolean flag) {
		this.renderTimeoutCircle = flag;
	}
	
	public void setTextPadding(int padding) {
		this.padding = padding;
	}
	
	public void update(int score, float gravity, int FPS) {
		
		this.score = score;
		this.FPS = FPS;
		this.gravity = gravity;
		
		if (scoreString.length() != formatter.format(this.score).length()) {
			textChanged = true;
		}
		
		scoreString = formatter.format(this.score);
		if (scoreString.length() < 4) {
			//scoreString = "0," + scoreString;
		}
		
	}
	
	public void render(Canvas canvas, Paint paint) {
		
		if (renderTimeoutCircle) {
			paint.setARGB(150, (int) (timeoutCircleProgress * 255), (int) ((-timeoutCircleProgress + 1) * 255), 75);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((int) (timeoutCirclePadding / 3));
			canvas.drawArc(touchTimeoutCircle, -90, (-timeoutCircleProgress + 1) * 360, false, paint);
		}
		
		
		paint.setTypeface(irresistorFont);	//draw the text in custom font
		paint.setColor(Color.WHITE);
		paint.setTextSize(70);
		paint.setStyle(Style.FILL);
//		canvas.drawText("GravBall", 100,  100,  paint);

		
		paint.setTypeface(blockFont);
		if (textChanged) {

			scoreTextSize = determineFontSize(paint, scoreString,(int) (width / 2.5), (int) (height / 10), scoreTextSize);
			if (scoreTextSize > 60)
				scoreTextSize = 60;
			textChanged = false;
		}
		
		
		paint.setTextSize(scoreTextSize);
		//paint.setARGB(255, 255, 75, 75);
		canvas.drawText(scoreString, padding, -textBounds.top + padding, paint);
		
		
		
		paint.setColor(Color.RED);
		//paint.setTypeface(irresistorFont);
		paint.setTextSize(20);
//		canvas.drawText("FPS: " + FPS, 20, 200, paint);
//		canvas.drawText("gravity: " + gravity, 20, 40, paint);
	}

}
