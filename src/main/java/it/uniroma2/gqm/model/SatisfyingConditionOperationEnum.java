package it.uniroma2.gqm.model;

public enum SatisfyingConditionOperationEnum {
	LESS(false), LESS_OR_EQUAL(false), EQUAL(true), NOT_EQUAL(true), GREATER_OR_EQUAL(false), GREATER(false);
	
	private final boolean isOnlyBoolean;
	
	private SatisfyingConditionOperationEnum(boolean cond)
	{
		if(cond)
			 this.isOnlyBoolean = true;
		else
			 this.isOnlyBoolean = false;
	}
	
	public boolean isOnlyBoolean()
	{
		 return this.isOnlyBoolean;
	}
}
