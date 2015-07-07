package com.example.gravballalpha;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	
	private GamePanel gamePanel;
	
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Creating...");
		
//		if (savedInstanceState != null) {
//			Log.d(TAG, "should restore state now");
//		} else {
//			Log.d(TAG, "savedState is NULL!!!!!");
//		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//get rid of that action bar!
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	//Give me Full Control
		
		gamePanel = new GamePanel(this);
		
		setContentView(gamePanel);	//set the view to GamePanel
		Log.d(TAG, "View Added. Giving view to GamePanel.");
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "Saving Instance...");
		
		editor = preferences.edit();
		
		gamePanel.onSaveInstanceState(editor);
	}
//	
//	@Override
//	public void onRestoreInstanceState(Bundle savedInstanceState) {
//		Log.d(TAG, "Restoring Instance...");
//		
//		super.onSaveInstanceState(savedInstanceState);
//		
//		gamePanel.onRestoreInstatnceState(savedInstanceState);
//	}
	
	@Override
	public void onBackPressed() {
		gamePanel.onBackPressed();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroying...");
		super.onDestroy();
	}
	
	@Override
	public void onStop() {
		Log.d(TAG, "Stopping...");
		super.onStop();
	}

}
