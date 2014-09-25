package com.parrot.freeflight.catroid;

import android.os.Bundle;
import android.view.Menu;

import com.parrot.freeflight.activities.SettingsDialog;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiver;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneVideoRecordStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneVideoRecordingStateReceiver;
import com.parrot.freeflight.receivers.WifiSignalStrengthChangedReceiver;
import com.parrot.freeflight.receivers.WifiSignalStrengthReceiverDelegate;
import com.parrot.freeflight.sensors.DeviceOrientationChangeDelegate;
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.SettingsDialogDelegate;

public class VideoActivity extends ParrotActivity implements
		DeviceOrientationChangeDelegate, WifiSignalStrengthReceiverDelegate,
		DroneVideoRecordStateReceiverDelegate,
		DroneEmergencyChangeReceiverDelegate,
		DroneBatteryChangedReceiverDelegate, DroneFlyingStateReceiverDelegate,
		DroneCameraReadyActionReceiverDelegate,
		DroneRecordReadyActionReceiverDelegate, SettingsDialogDelegate {

	private WifiSignalStrengthChangedReceiver wifiSignalReceiver;
	private DroneVideoRecordingStateReceiver videoRecordingStateReceiver;
	private DroneEmergencyChangeReceiver droneEmergencyReceiver;
	private DroneBatteryChangedReceiver droneBatteryReceiver;
	private DroneFlyingStateReceiver droneFlyingStateReceiver;
	private DroneCameraReadyChangeReceiver droneCameraReadyChangedReceiver;
	private DroneRecordReadyChangeReceiver droneRecordReadyChangeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		wifiSignalReceiver = new WifiSignalStrengthChangedReceiver(this);
		videoRecordingStateReceiver = new DroneVideoRecordingStateReceiver(this);
		droneEmergencyReceiver = new DroneEmergencyChangeReceiver(this);
		droneBatteryReceiver = new DroneBatteryChangedReceiver(this);
		droneFlyingStateReceiver = new DroneFlyingStateReceiver(this);
		droneCameraReadyChangedReceiver = new DroneCameraReadyChangeReceiver(
				this);
		droneRecordReadyChangeReceiver = new DroneRecordReadyChangeReceiver(
				this);

		HudViewController view = new HudViewController(this, false);
		view.setCameraButtonEnabled(false);
		view.setRecordButtonEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

	@Override
	public void prepareDialog(SettingsDialog dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDismissed(SettingsDialog settingsDialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOptionChangedApp(SettingsDialog dialog,
			EAppSettingProperty property, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneRecordReadyChanged(boolean ready) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCameraReadyChanged(boolean ready) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneFlyingStateChanged(boolean flying) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneBatteryChanged(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneEmergencyChanged(int code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneRecordVideoStateChanged(boolean recording,
			boolean usbActive, int remainingTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWifiSignalStrengthChanged(int strength) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOrientationChanged(float[] orientation,
			float magneticHeading, int magnetoAccuracy) {
		// TODO Auto-generated method stub

	}

}
