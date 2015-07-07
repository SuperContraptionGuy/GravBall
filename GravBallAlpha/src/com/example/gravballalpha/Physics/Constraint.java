package com.example.gravballalpha.Physics;

public class Constraint<objectType1 extends Object, objectType2 extends Object> {
	
	private objectType1 object1;
	private objectType2 object2;

	public Constraint(objectType1 object1, objectType2 object2) {

		this.object1 = object1;
		this.object2 = object2;
		
	}
	
	public void update() {
		
		object2.position.set(object1.position);
		object2.linVelocity.setXY(0, 0);
		
	}

}
