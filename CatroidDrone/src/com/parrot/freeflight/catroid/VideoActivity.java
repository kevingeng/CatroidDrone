package com.parrot.freeflight.catroid;

import java.io.File;

import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;

import com.parrot.freeflight.activities.SettingsDialog;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.receivers.DroneAvailabilityDelegate;
import com.parrot.freeflight.receivers.DroneAvailabilityReceiver;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiver;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneConnectionChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneConnectionChangedReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneFirmwareCheckReceiver;
import com.parrot.freeflight.receivers.DroneFirmwareCheckReceiverDelegate;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneVideoRecordStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneVideoRecordingStateReceiver;
import com.parrot.freeflight.receivers.MediaReadyDelegate;
import com.parrot.freeflight.receivers.MediaReadyReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import com.parrot.freeflight.receivers.WifiSignalStrengthChangedReceiver;
import com.parrot.freeflight.receivers.WifiSignalStrengthReceiverDelegate;
import com.parrot.freeflight.sensors.DeviceOrientationChangeDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.intents.DroneStateManager;
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;
import com.parrot.freeflight.transcodeservice.TranscodingService;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.SettingsDialogDelegate;

public class VideoActivity extends ParrotActivity implements DeviceOrientationChangeDelegate,
		WifiSignalStrengthReceiverDelegate, DroneVideoRecordStateReceiverDelegate,
		DroneEmergencyChangeReceiverDelegate, DroneBatteryChangedReceiverDelegate, DroneFlyingStateReceiverDelegate,
		DroneCameraReadyActionReceiverDelegate, DroneRecordReadyActionReceiverDelegate, SettingsDialogDelegate,
		DroneAvailabilityDelegate, NetworkChangeReceiverDelegate, DroneFirmwareCheckReceiverDelegate,
		MediaReadyDelegate, DroneConnectionChangeReceiverDelegate {

	private WifiSignalStrengthChangedReceiver wifiSignalReceiver;
	private DroneVideoRecordingStateReceiver videoRecordingStateReceiver;
	private DroneEmergencyChangeReceiver droneEmergencyReceiver;
	private DroneBatteryChangedReceiver droneBatteryReceiver;
	private DroneFlyingStateReceiver droneFlyingStateReceiver;
	private DroneCameraReadyChangeReceiver droneCameraReadyChangedReceiver;
	private DroneRecordReadyChangeReceiver droneRecordReadyChangeReceiver;
	private DroneAvailabilityReceiver droneStateReceiver;
	private NetworkChangeReceiver networkChangeReceiver;
	private DroneFirmwareCheckReceiver droneFirmwareCheckReceiver;
	private MediaReadyReceiver mediaReadyReceiver;
	private DroneConnectionChangedReceiver droneConnectionChangeReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		initBroadcastReceivers();
		registerBroadcastReceivers();

		HudViewController view = new HudViewController(this, false);
		view.setCameraButtonEnabled(false);
		view.setRecordButtonEnabled(false);
	}

	protected void initBroadcastReceivers() {
		wifiSignalReceiver = new WifiSignalStrengthChangedReceiver(this);
		videoRecordingStateReceiver = new DroneVideoRecordingStateReceiver(this);
		droneEmergencyReceiver = new DroneEmergencyChangeReceiver(this);
		droneBatteryReceiver = new DroneBatteryChangedReceiver(this);
		droneFlyingStateReceiver = new DroneFlyingStateReceiver(this);
		droneCameraReadyChangedReceiver = new DroneCameraReadyChangeReceiver(this);
		droneRecordReadyChangeReceiver = new DroneRecordReadyChangeReceiver(this);
		droneStateReceiver = new DroneAvailabilityReceiver(this);
		networkChangeReceiver = new NetworkChangeReceiver(this);
		droneFirmwareCheckReceiver = new DroneFirmwareCheckReceiver(this);
		mediaReadyReceiver = new MediaReadyReceiver(this);
		droneConnectionChangeReceiver = new DroneConnectionChangedReceiver(this);
	}

	protected void registerBroadcastReceivers() {
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
		broadcastManager.registerReceiver(droneStateReceiver, new IntentFilter(
				DroneStateManager.ACTION_DRONE_STATE_CHANGED));
		broadcastManager.registerReceiver(droneFirmwareCheckReceiver, new IntentFilter(
				DroneControlService.DRONE_FIRMWARE_CHECK_ACTION));

		IntentFilter mediaReadyFilter = new IntentFilter();
		mediaReadyFilter.addAction(DroneControlService.NEW_MEDIA_IS_AVAILABLE_ACTION);
		mediaReadyFilter.addAction(TranscodingService.NEW_MEDIA_IS_AVAILABLE_ACTION);
		broadcastManager.registerReceiver(mediaReadyReceiver, mediaReadyFilter);
		broadcastManager.registerReceiver(droneConnectionChangeReceiver, new IntentFilter(
				DroneControlService.DRONE_CONNECTION_CHANGED_ACTION));

		registerReceiver(networkChangeReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));

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
	public void onOptionChangedApp(SettingsDialog dialog, EAppSettingProperty property, Object value) {
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
	public void onDroneRecordVideoStateChanged(boolean recording, boolean usbActive, int remainingTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWifiSignalStrengthChanged(int strength) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceOrientationChanged(float[] orientation, float magneticHeading, int magnetoAccuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneAvailabilityChanged(boolean isDroneOnNetwork) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetworkChanged(NetworkInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFirmwareChecked(boolean updateRequired) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMediaReady(File mediaFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneConnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDroneDisconnected() {
		// TODO Auto-generated method stub

	}

}
