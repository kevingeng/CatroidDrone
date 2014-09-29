package com.parrot.freeflight.catroid;

import java.io.File;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.widget.Toast;

import com.parrot.freeflight.activities.SettingsDialog;
import com.parrot.freeflight.drone.NavData;
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
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;
import com.parrot.freeflight.ui.SettingsDialogDelegate;
import com.parrot.freeflight.video.VideoStageRenderer;
import com.parrot.freeflight.video.VideoStageView;

public class VideoActivity extends Activity implements DeviceOrientationChangeDelegate,
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

	private GLSurfaceView mGLView;
	private SparseIntArray emergencyStringMap;
	private VideoStageView canvasView;
	private GLSurfaceView glView;
	private VideoStageRenderer renderer;

	private DroneControlService droneControlService;
	private boolean useSoftwareRendering;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isFinishing()) {
			return;
		}

		setContentView(R.layout.activity_video);

		initReceivers();
		registerReceivers();

		bindService(new Intent(this, DroneControlService.class), mConnection, Context.BIND_AUTO_CREATE);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			useSoftwareRendering = bundle.getBoolean("USE_SOFTWARE_RENDERING");
		} else {
			useSoftwareRendering = false;
		}

		// HudViewController view = new HudViewController(this,
		// useSoftwareRendering);
		showVideo();

	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			droneControlService = ((DroneControlService.LocalBinder) service).getService();
			onDroneServiceConnected();
		}

		public void onServiceDisconnected(ComponentName name) {
			droneControlService = null;
		}
	};

	/**
	 * Called when we connected to DroneControlService
	 */
	protected void onDroneServiceConnected() {
		if (droneControlService != null) {
			droneControlService.resume();
			droneControlService.requestDroneStatus();
		} else {
			Log.w("Drone", "DroneServiceConnected event ignored as DroneControlService is null");
		}

		Toast.makeText(getApplicationContext(), "connected to Drone", Toast.LENGTH_SHORT).show();
	}

	public void showVideo() {

		glView = new GLSurfaceView(this);
		glView.setEGLContextClientVersion(2);
		setContentView(glView);

		renderer = new VideoStageRenderer(this, null);

		initNavdataStrings();
		initCanvasSurfaceView();
		initGLSurfaceView();
	}

	private void initCanvasSurfaceView() {
		if (canvasView != null) {
			canvasView.setRenderer(renderer);
			// canvasView.setOnTouchListener(this);
		}
	}

	private void initGLSurfaceView() {
		if (glView != null) {
			glView.setRenderer(renderer);
			// glView.setOnTouchListener(this);
		}
	}

	private void initReceivers() {
		wifiSignalReceiver = new WifiSignalStrengthChangedReceiver(this);
		videoRecordingStateReceiver = new DroneVideoRecordingStateReceiver(this);
		droneEmergencyReceiver = new DroneEmergencyChangeReceiver(this);
		droneBatteryReceiver = new DroneBatteryChangedReceiver(this);
		droneFlyingStateReceiver = new DroneFlyingStateReceiver(this);
		droneCameraReadyChangedReceiver = new DroneCameraReadyChangeReceiver(this);
		droneRecordReadyChangeReceiver = new DroneRecordReadyChangeReceiver(this);
	}

	private void registerReceivers() {
		// System wide receiver
		registerReceiver(wifiSignalReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

		// Local receivers
		LocalBroadcastManager localBroadcastMgr = LocalBroadcastManager.getInstance(getApplicationContext());
		localBroadcastMgr.registerReceiver(videoRecordingStateReceiver, new IntentFilter(
				DroneControlService.VIDEO_RECORDING_STATE_CHANGED_ACTION));
		localBroadcastMgr.registerReceiver(droneEmergencyReceiver, new IntentFilter(
				DroneControlService.DRONE_EMERGENCY_STATE_CHANGED_ACTION));
		localBroadcastMgr.registerReceiver(droneBatteryReceiver, new IntentFilter(
				DroneControlService.DRONE_BATTERY_CHANGED_ACTION));
		localBroadcastMgr.registerReceiver(droneFlyingStateReceiver, new IntentFilter(
				DroneControlService.DRONE_FLYING_STATE_CHANGED_ACTION));
		localBroadcastMgr.registerReceiver(droneCameraReadyChangedReceiver, new IntentFilter(
				DroneControlService.CAMERA_READY_CHANGED_ACTION));
		localBroadcastMgr.registerReceiver(droneRecordReadyChangeReceiver, new IntentFilter(
				DroneControlService.RECORD_READY_CHANGED_ACTION));
	}

	private void unregisterReceivers() {
		// Unregistering system receiver
		unregisterReceiver(wifiSignalReceiver);

		// Unregistering local receivers
		LocalBroadcastManager localBroadcastMgr = LocalBroadcastManager.getInstance(getApplicationContext());
		localBroadcastMgr.unregisterReceiver(videoRecordingStateReceiver);
		localBroadcastMgr.unregisterReceiver(droneEmergencyReceiver);
		localBroadcastMgr.unregisterReceiver(droneBatteryReceiver);
		localBroadcastMgr.unregisterReceiver(droneFlyingStateReceiver);
		localBroadcastMgr.unregisterReceiver(droneCameraReadyChangedReceiver);
		localBroadcastMgr.unregisterReceiver(droneRecordReadyChangeReceiver);
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

	private void initNavdataStrings() {
		emergencyStringMap = new SparseIntArray(17);

		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_CUTOUT, R.string.CUT_OUT_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_MOTORS, R.string.MOTORS_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_CAMERA, R.string.CAMERA_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_PIC_WATCHDOG, R.string.PIC_WATCHDOG_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_PIC_VERSION, R.string.PIC_VERSION_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_ANGLE_OUT_OF_RANGE, R.string.TOO_MUCH_ANGLE_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_VBAT_LOW, R.string.BATTERY_LOW_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_USER_EL, R.string.USER_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_ULTRASOUND, R.string.ULTRASOUND_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_UNKNOWN, R.string.UNKNOWN_EMERGENCY);
		emergencyStringMap.put(NavData.ERROR_STATE_NAVDATA_CONNECTION, R.string.CONTROL_LINK_NOT_AVAILABLE);
		emergencyStringMap.put(NavData.ERROR_STATE_START_NOT_RECEIVED, R.string.START_NOT_RECEIVED);
		emergencyStringMap.put(NavData.ERROR_STATE_ALERT_CAMERA, R.string.VIDEO_CONNECTION_ALERT);
		emergencyStringMap.put(NavData.ERROR_STATE_ALERT_VBAT_LOW, R.string.BATTERY_LOW_ALERT);
		emergencyStringMap.put(NavData.ERROR_STATE_ALERT_ULTRASOUND, R.string.ULTRASOUND_ALERT);
		emergencyStringMap.put(NavData.ERROR_STATE_ALERT_VISION, R.string.VISION_ALERT);
		emergencyStringMap.put(NavData.ERROR_STATE_EMERGENCY_UNKNOWN, R.string.UNKNOWN_EMERGENCY);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO Auto-generated method stub
		unregisterReceivers();
	}
}
