package it.uniroma2.gqm.model;

public enum GoalType {
	OG(0), 
	MG(1);
	
	private final int id;
	
	private GoalType(int id) { 
		 this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getString() {
		if(id == 0)
			return "Organizational Goal";
		else
			return "Measurement Goal";
	}
	
	public String toString() {
		return Integer.toString(id);
	}
	
	/*public static boolean isMG(Goal g) {
		return g.getType().intValue() == GoalType.MG.getId();
	}
	
	public static boolean isOG(Goal g) {
		return g.getType().intValue() == GoalType.OG.getId();
	}*/
}
