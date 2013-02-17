/**
 * Copyright (C) 2013 Fabien Fleurey <fabien@fleurey.com>
 */
package com.fleurey.android.light;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
