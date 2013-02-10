package com.fleurey.android.light.flashmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class FlashManager implements FlashControler {

	private Context mContext;
	private Camera camera;
	private Parameters parameters;
	private LightPower currentPower;
	
	public FlashManager(Context context) {
		this.mContext = context;
	}
	
	public void release() {
		setPower(LightPower.OFF);
	}
	
	private void turnFlashON() {
		camera = Camera.open();
		parameters = camera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		camera.setParameters(parameters);
		camera.startPreview();
		currentPower = LightPower.ON;
	}
	
	private void turnFlashOFF() {
		camera.stopPreview();
		camera.release();
		currentPower = LightPower.OFF;
	}
	
	@Override
	public void setPower(LightPower value) {
		switch (value) {
		case ON:
			if (getPower() == LightPower.OFF) {
				turnFlashON();
			}
			break;
		case OFF:
			if (getPower() == LightPower.ON) {
				turnFlashOFF(); 
			}
			break;
		default: 
			break;
		}
	}

	@Override
	public LightPower getPower() {
		if (currentPower == null) {
			return LightPower.OFF;
		}
		return currentPower;
	}

	@Override
	public boolean isFlashAvailable() {
		return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

}
