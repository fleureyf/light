/**
 * Copyright (C) 2013 Fabien Fleurey <fabien@fleurey.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fleurey.android.light.flashmanager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class FlashManager {

	public enum LightPower {
		OFF, ON;
	}
	
	private Camera camera;
	private Parameters parameters;
	private LightPower currentPower;
	
	public static boolean isFlashAvailable(Context context) {
		return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
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

	public LightPower getPower() {
		if (currentPower == null) {
			return LightPower.OFF;
		}
		return currentPower;
	}

}
