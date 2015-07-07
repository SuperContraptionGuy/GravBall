package com.example.gravballalpha.Physics;

import android.os.Bundle;

public class loc_2D {
	
	/* Location Section */
	
	private float loc_x = 0;
	private float loc_y = 0;
	
	
	public void onSaveInstanceState(Bundle savedState, int index) {
		
		savedState.putFloat(this.getClass().getSimpleName() + "_loc_x" + index, loc_x);
		savedState.putFloat(this.getClass().getSimpleName() + "_loc_y" + index, loc_y);

	}
	
	public void onRestoreInstanceState(Bundle savedState, int index) {
		
		loc_x = savedState.getFloat(this.getClass().getSimpleName() + "_loc_x" + index);
		loc_y = savedState.getFloat(this.getClass().getSimpleName() + "_loc_y" + index);

	}
	
	public void setXY(float x, float y) {
		this.loc_x = x;
		this.loc_y = y;
	}
	
	public void set(loc_2D a) {
		this.setXY(a.getloc_x(), a.getloc_y());
	}
	
	public float getloc_x() {
		return this.loc_x;
	}
	public void setloc_x(float loc_x) {
		this.loc_x = loc_x;
	}
	
	public float getloc_y() {
		return this.loc_y;
	}
	public void setloc_y(float loc_y) {
		this.loc_y = loc_y;
	}
	
	public loc_2D sum(loc_2D b) {
		
		loc_2D sum = new loc_2D();
		
		sum.setXY(this.getloc_x() + b.getloc_x(), this.getloc_y() + b.getloc_y());
		
		return sum;
	}
	
	public loc_2D diff(loc_2D b) {
		
		loc_2D sum = new loc_2D();
		
		sum.setXY(this.getloc_x() - b.getloc_x(), this.getloc_y() - b.getloc_y());
		
		return sum;
	}
	
	public loc_2D mult(loc_2D b) {
		
		loc_2D sum = new loc_2D();
		
		sum.setXY(this.getloc_x() * b.getloc_x(), this.getloc_y() * b.getloc_y());
		
		return sum;
	}
	
	public loc_2D divideConstant(float a) {
		
		loc_2D result = new loc_2D();
		
		result.setXY(this.getloc_x() / a, this.getloc_y() / a);
		
		return result;
	}
	public loc_2D multConst(float b) {
		
		loc_2D sum = new loc_2D();
		
		sum.setXY(this.getloc_x() * b, this.getloc_y() * b);
		
		return sum;
		
	}
	
}