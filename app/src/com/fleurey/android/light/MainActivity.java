package com.fleurey.android.light;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fleurey.android.light.backgroundmanager.LightService;
import com.fleurey.android.light.flashmanager.FlashManager;

public class MainActivity extends Activity {

	private FlashManager mFlashManager;
	private Intent serviceIntent;
	private boolean on = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		serviceIntent = new Intent(getApplicationContext(), LightService.class);
		mFlashManager = new FlashManager(getApplicationContext());
		if (!mFlashManager.isFlashAvailable()) {
			finish();
		}
		Button testButton = (Button) findViewById(R.id.button);
		testButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!on) {
					startService(serviceIntent);
					on = true;
				} else {
					stopService(new Intent(getApplicationContext(), LightService.class));
					on = false;
				}
			}
		});
	}
}
