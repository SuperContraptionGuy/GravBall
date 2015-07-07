package com.example.gravballalpha.Physics;

import android.os.Bundle;

public class rot_2D {
	
	/* Rotation Section */
	
	private float rot_z = 0;
	
	
	public void onSaveInstanceState(Bundle savedState, int index) {
		
		savedState.putFloat(this.getClass().getSimpleName() + "_rot_z", rot_z + index);
	}
	
	public void onRestoreInstanceState(Bundle savedState, int index) {
		
		rot_z = savedState.getFloat(this.getClass().getSimpleName() + "_rot_z" + index);
	}
	
	public float getRot_z() {
		return this.rot_z;
	}
	public void setRot_z(float rot_z) {
		this.rot_z = rot_z; //- ((rot_z % 360) * 360);	// This may not work with negative values for input of rot_z.
	}

}
