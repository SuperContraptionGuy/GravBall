package com.example.gravballalpha.MainGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.gravballalpha.DrawableObject;
import com.example.gravballalpha.MenuScreen.Button;
import com.example.gravballalpha.MenuScreen.MenuWindow;

public class PauseMenu extends DrawableObject {

	private MenuWindow exitTextWindow;
	private MenuWindow scoreDisplayWindow;
	private Button menuButton;
	private Button playAgainButton;
	
	public PauseMenu(Context context) {
		super(context);
		
		exitTextWindow = new MenuWindow(context);
		scoreDisplayWindow = new MenuWindow(context);
		
		menuButton = new Button(context);
		playAgainButton = new Button(context);
		
	}
	
	@Override
	public void render(Canvas canvas, Paint paint) {
		
		exitTextWindow.render(canvas, paint);
		scoreDisplayWindow.render(canvas, paint);
		
		menuButton.render(canvas, paint);
		playAgainButton.render(canvas, paint);
		
	}

}
