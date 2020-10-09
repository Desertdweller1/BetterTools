package me.desertdweller.bettertools.math;

import me.desertdweller.bettertools.BetterTools;

public class Noise {
	private static BetterTools plugin = BetterTools.getPlugin(BetterTools.class);
	public float scale = 1;
	public float xScew = 1;
	public float yScew = 1;
	public float zScew = 1;
	public float min = 1;
	public float max = 1;
	public float frequency = 0.5f;
	public String method = "none";
	
	public Noise(String input) {
		String[] inputs = input.split(",");
		scale = Float.parseFloat(inputs[0]);
		xScew = Float.parseFloat(inputs[1]);
		yScew = Float.parseFloat(inputs[2]);
		zScew = Float.parseFloat(inputs[3]);
		min = Float.parseFloat(inputs[4]);
		max = Float.parseFloat(inputs[5]);
		frequency = Float.parseFloat(inputs[6]);
		method = inputs[7];
	}
	
	public Noise() {
		
	}
	
	public boolean getPoint(float x, float y, float z) {
		if(method.equals("none"))
			return true;
		
		double result = 0;
		if(method.equals("turb"))
			result = plugin.noiseGen.turbulence3(x/xScew/scale, y/yScew/scale, z/zScew/scale, frequency);
		if(method.equals("perlin"))
			result = plugin.noiseGen.noise3(x/xScew/scale, y/yScew/scale, z/zScew/scale);
		if(result < max && result > min)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return scale + "," + xScew + "," + yScew + "," + zScew + "," + min + "," + max + "," + frequency + "," + method;
	}
}
