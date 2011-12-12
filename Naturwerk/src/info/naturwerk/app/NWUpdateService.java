package info.naturwerk.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NWUpdateService extends Service {

	private static final String TAG = "NWUpdateService";
	static final int DELAY = 60000; // a minute
	private boolean runFlag = false;
	private Updater updater;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent intent, int flag, int startId) {
		if (!runFlag) {
			this.runFlag = true;
			this.updater.start();
			((NWApplication) super.getApplication()).setServiceRunning(true);

			Log.d(TAG, "onStarted");
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		((NWApplication) super.getApplication()).setServiceRunning(false);

		Log.d(TAG, "onDestroyed");
	}

	private class Updater extends Thread {

		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() {
			NWUpdateService updaterService = NWUpdateService.this;
			//don't loop
			//while (updaterService.runFlag) {
				Log.d(TAG, "Running background thread");
				try {
					NWApplication nwapp = (NWApplication) updaterService
							.getApplication();
					nwapp.fetchServerData();
					
					Thread.sleep(DELAY);
					//don't loop
					
				} catch (InterruptedException e) {
					updaterService.runFlag = false;
				}
			//}
		}
	}
} // Updater

