package com.fleurey.android.light.flashmanager;

public interface FlashControler {

	public enum LightPower {
		OFF, ON;
	}
	
	public boolean isFlashAvailable();
	public void setPower(LightPower value);
	public LightPower getPower();
	
}
