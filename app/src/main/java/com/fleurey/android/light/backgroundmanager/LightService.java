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
package com.fleurey.android.light.backgroundmanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.fleurey.android.light.R;
import com.fleurey.android.light.flashmanager.FlashManager;
import com.fleurey.android.light.flashmanager.FlashManager.LightPower;

public class LightService extends Service {

	public static final String SERVICE_RUNNING = LightService.class.getName() + ".PREF_SERVICE_RUNNING";
	public static final String ACTION_SERVICE_STOPPED = LightService.class.getName() + ".ACTION_SERVICE_STOPPED";
	
	private static final String ACTION_STOP_REQUEST = LightService.class.getName() + ".ACTION_STOP_REQUEST";
	private static final int SERVICE_ID = 79290;
	
	private FlashManager mFlashManager = new FlashManager();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.getAction() != null && ACTION_STOP_REQUEST.equals(intent.getAction())) {
			stopSelfResult(startId);
		}
		mFlashManager.setPower(LightPower.ON);
		startForeground(SERVICE_ID, buildRunningNotification());
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(SERVICE_RUNNING, true).commit();
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mFlashManager.release();
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(SERVICE_RUNNING, false).commit();
		sendBroadcast(new Intent(ACTION_SERVICE_STOPPED));
		stopForeground(true);
	}

	private Notification buildRunningNotification() {
		Intent stopService = new Intent(getApplicationContext(), getClass());
		stopService.setAction(ACTION_STOP_REQUEST);
		PendingIntent pIntent = PendingIntent.getService(getApplicationContext(), 0, stopService, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		builder.setContentTitle("Light");
		builder.setContentText(getResources().getText(R.string.notification));
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setContentIntent(pIntent);
		Notification runningNotification = builder.build();
		return runningNotification;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
