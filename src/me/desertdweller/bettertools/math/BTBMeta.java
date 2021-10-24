package me.desertdweller.bettertools.math;

//BetterToolsBlockMeta

public class BTBMeta {
	public boolean specified;
	public int amount;
	public String customParams;
	
	public BTBMeta(boolean specified, int amount, String customParams) {
		this.specified = specified;
		this.amount = amount;
		this.customParams = customParams;
	}
	
	public boolean containsParam(String checkedParam, boolean defaultIfNull) {
		if(customParams == null)
			return defaultIfNull;
		for(String curParam : customParams.split(",")) {
			if(curParam.equalsIgnoreCase(checkedParam))
				return true;
		}
		return false;
	}
}
