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
package com.fleurey.android.light;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fleurey.android.light.backgroundmanager.LightService;
import com.fleurey.android.light.flashmanager.FlashManager;

public class MainActivity extends Activity {

	private ImageButton mImageButton;
	private boolean on = false;
	private boolean noFlash = false;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if(LightService.ACTION_SERVICE_STOPPED.equals(intent.getAction())) {
	        	Log.d("DEB", "intent");
	        	mImageButton.setBackgroundResource(R.drawable.img_background_off);
	        	on = false;
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!FlashManager.isFlashAvailable(getApplicationContext())) {
			noFlash = true;
		}
		mImageButton = (ImageButton) findViewById(R.id.button);
		mImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleStatus();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateStatus();
		updateBackground();
		registerReceiver(mReceiver, new IntentFilter(LightService.ACTION_SERVICE_STOPPED));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}
	
	private void toggleStatus() {
		if (on) {
			stopService(new Intent(getApplicationContext(), LightService.class));
			on = false;
		} else {
			if (noFlash) {
				Toast.makeText(getApplicationContext(), R.string.no_flash, Toast.LENGTH_LONG).show();
				return;
			}
			startService(new Intent(getApplicationContext(), LightService.class));
			on = true;
		}
		updateBackground();
	}
	
	private void updateStatus() {
		on = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(LightService.SERVICE_RUNNING, false);
	}
	
	private void updateBackground() {
		if (on) {
			mImageButton.setBackgroundResource(R.drawable.img_background_on);
		} else {
			mImageButton.setBackgroundResource(R.drawable.img_background_off);
		}
	}
}
