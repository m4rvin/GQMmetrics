package it.uniroma2.gqm.model;

public class BinaryElement {
	
	private Goal goal;
	private int value;
	
	public BinaryElement(){}
	
	/**
	 * Create an BinaryElement object only if the Goal is an OG Goal
	 * @param g
	 * @param v
	 */
	public BinaryElement(Goal g, int v){
		
		if(g.isOG()){
			this.setGoal(g);
			this.setValue(v);
		}
	}

	public Goal getGoal() {
		return goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
