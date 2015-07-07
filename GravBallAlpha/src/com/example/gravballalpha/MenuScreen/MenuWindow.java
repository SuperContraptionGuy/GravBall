package com.example.gravballalpha.MenuScreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.gravballalpha.DrawableObject;

public class MenuWindow extends DrawableObject {

	private RectF box;
	
	private int round;
	
	private int alpha;
	private int red;
	private int green;
	private int blue;
	
	private String text;
	private Rect textBounds;
	private int textSize;
	
	public MenuWindow(Context context) {
		super(context);

		box = new RectF();
		textBounds = new Rect();
	}
	
	public void setBox(float left, float top, float right, float bottom) {
		box.set(left, top, right, bottom);
	}
	
	public void setColor(int a, int r, int b, int g) {
		this.alpha = a;
		this.red = r;
		this.green = g;
		this.blue = b;
	}
	
	public void setTextSize(int size) {
		Paint paint = new Paint();
		
		this.textSize = size;
		
		paint.getTextBounds(text, 0, text.length(), textBounds);
	}
	
	public void setText(String text) {
		Paint paint = new Paint();
		
		this.text = text;
		
		paint.getTextBounds(this.text, 0, this.text.length(), textBounds);
	}
	
	public void setRound(int r) {
		this.round = r;
	}
	
	@Override
	public void render(Canvas canvas, Paint paint) {
		
		paint.setColor(Color.argb(alpha, red, green, blue));
		canvas.drawRoundRect(box, round, round, paint);
		canvas.drawText(
					text, 
					(box.right - box.left) / 2 - (textBounds.right - textBounds.left) / 2 + box.left, 
					(box.bottom - box.top) / 2 - (textBounds.bottom - textBounds.top) / 2 + box.top, 
					paint
				);
	}

}
