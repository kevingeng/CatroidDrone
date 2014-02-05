/**
 * 
 */
package com.parrot.freeflight.activities.base;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.parrot.freeflight.R;
import com.parrot.freeflight.R.id;
import com.parrot.freeflight.receivers.DroneConnectionChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneConnectionChangedReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneReadyReceiver;
import com.parrot.freeflight.receivers.DroneReadyReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;

/**
 * @author GeraldW
 * 
 */
public class CatroidDummy extends Activity implements
		DroneReadyReceiverDelegate, DroneFlyingStateReceiverDelegate,
		DroneConnectionChangeReceiverDelegate {

	private DroneControlService droneControlService;

	private View topView;
	private Button btnConnect;
	private Button btnDisconnect;
	private Button btnTakeoff;
	private Button btnLand;
	private Button btnUp;
	private Button btnDown;
	private Button btnLeft;
	private Button btnRigth;
	private Button btnHover;
	private SeekBar powerBar;

	private boolean isDroneConnected;
	private float power = 0.2f;

	private BroadcastReceiver droneReadyReceiver;
	private BroadcastReceiver droneConnectionChangeReceiver;
	private DroneFlyingStateReceiver droneFlyingStateReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catroiddummy);

		topView = findViewById(R.id.textViewTop);

		powerBar = (SeekBar) findViewById(R.id.seekBarPower);
		powerBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			int progressChanged = 0;

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				progressChanged = progress;
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				power = getConvertedValue(progressChanged);
				Toast.makeText(seekBar.getContext(), "Value: " + power,
						Toast.LENGTH_SHORT).show();
			}
		});

		btnConnect = (Button) findViewById(R.id.btn_connect);
		btnConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onConnectPressed()) {
					topView.setBackgroundColor(0xFF00FF00);
					topView.invalidate();
					isDroneConnected = true;
					enableButtons(true);
				} else {
					topView.setBackgroundColor(0xFFFF0000);
					topView.invalidate();
					isDroneConnected = false;
					enableButtons(false);
				}
			}
		});

		btnDisconnect = (Button) findViewById(R.id.btn_disconnect);
		btnDisconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isDroneConnected) {
					onDisconnectPressed();
					topView.setBackgroundColor(0xFFFF0000);
					topView.invalidate();
					isDroneConnected = false;
				}
			}
		});

		btnTakeoff = (Button) findViewById(R.id.btn_takeoff);
		btnTakeoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTakeoffPressed();
			}
		});

		btnLand = (Button) findViewById(id.btn_land);
		btnLand.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLandPresed();
			}
		});

		btnHover = (Button) findViewById(id.btn_Hover);
		btnHover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				power = 0;
				powerBar.setProgress(0);
			}
		});

		btnUp = (Button) findViewById(id.btn_Up);
		btnUp.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// PRESSED
					onMoveUpPressed(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					onMoveUpPressed(0);
					return true;
				}
				return false;
			}
		});

		btnDown = (Button) findViewById(id.btn_Down);
		btnDown.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// PRESSED
					onMoveDownPressed(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					onMoveDownPressed(0);
					return true;
				}
				return false;
			}
		});

		btnLeft = (Button) findViewById(id.btn_Left);
		btnLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// PRESSED
					onMoveLeftPressed(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					onMoveLeftPressed(0);
					return true;
				}
				return false;
			}
		});

		btnRigth = (Button) findViewById(id.btn_Right);
		btnRigth.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// PRESSED
					onMoveRightPressed(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					onMoveRightPressed(0);
					return true;
				}
				return false;
			}
		});

		droneReadyReceiver = new DroneReadyReceiver(this);
		droneConnectionChangeReceiver = new DroneConnectionChangedReceiver(this);
		droneFlyingStateReceiver = new DroneFlyingStateReceiver(this);

		// onConnectPressed();

		// droneFlyingStateReceiver = new
		// DroneFlyingStateReceiverDelegate(this);

		// disable buttons until we there is no connection
		enableButtons(false);

	}

	public float getConvertedValue(int intVal) {
		float floatVal = 0.0f;
		floatVal = .01f * intVal;
		return floatVal;
	}

	public void enableButtons(boolean value) {
		btnDisconnect.setEnabled(value);
		btnDown.setEnabled(value);
		btnUp.setEnabled(value);
		btnHover.setEnabled(value);
		btnLeft.setEnabled(value);
		btnRigth.setEnabled(value);
		btnTakeoff.setEnabled(value);
		btnLand.setEnabled(value);
	}

	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("Drone", "onResume");
		LocalBroadcastManager manager = LocalBroadcastManager
				.getInstance(getApplicationContext());
		manager.registerReceiver(droneReadyReceiver, new IntentFilter(
				DroneControlService.DRONE_STATE_READY_ACTION));
		manager.registerReceiver(droneConnectionChangeReceiver,
				new IntentFilter(
						DroneControlService.DRONE_CONNECTION_CHANGED_ACTION));

	}

	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		super.onStop();
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO Auto-generated method stub

	}

	private boolean onConnectPressed() {
		Log.d("Drone", "onConnectPressed");
		return bindService(new Intent(this, DroneControlService.class),
				mConnection, Context.BIND_AUTO_CREATE);
	}

	private void onDisconnectPressed() {
		Log.d("Drone", "onDisconnectPressed");

		unbindService(mConnection);

		Toast.makeText(getApplicationContext(), "Disconnected to Drone",
				Toast.LENGTH_SHORT).show();

		enableButtons(false);
	}

	private void onTakeoffPressed() {
		Log.d("Drone", "onTakeoffPressed");
		droneControlService.triggerTakeOff();
	}

	private void onLandPresed() {
		Log.d("Drone", "onLandPresed");
		droneControlService.triggerTakeOff();
	}

	// NEW ----------------------------------
	public void calibrateMagneto() {
		Log.d("Drone", "calibrateMagneto");
		droneControlService.calibrateMagneto();
	}

	public void trim() {
		Log.d("Drone", "calibrateMagneto");
		droneControlService.flatTrim();
	}

	public void doLeftFlip() {
		Log.d("Drone", "doLeftFlip");
		droneControlService.doLeftFlip();
	}

	public void onMoveLeftPressed(final float power) {
		Log.d("Drone", "turnLeft");
		droneControlService.moveLeft(power);
	}

	public void onMoveRightPressed(final float power) {
		Log.d("Drone", "turnRight");
		droneControlService.moveRight(power);
	}

	public void onMoveUpPressed(final float power) {
		Log.d("Drone", "moveUp");
		droneControlService.moveUp(power);
	}

	public void onMoveDownPressed(final float power) {
		Log.d("Drone", "moveDown");
		droneControlService.moveDown(power);
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			droneControlService = ((DroneControlService.LocalBinder) service)
					.getService();
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
			Log.w("Drone",
					"DroneServiceConnected event ignored as DroneControlService is null");
		}

		Toast.makeText(getApplicationContext(), "connected to Drone",
				Toast.LENGTH_SHORT).show();

		// to calibrate the drone
		droneControlService.flatTrim();

		// settingsDialog = new SettingsDialog(this, this, droneControlService,
		// magnetoAvailable);

		// applySettings(settings);

		// initListeners();
		// runTranscoding();

		// if (droneControlService.getMediaDir() != null) {
		// view.setRecordButtonEnabled(true);
		// view.setCameraButtonEnabled(true);
		// }
	}

	class ClearRenderer implements GLSurfaceView.Renderer {
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// Do nothing special.]

		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			gl.glViewport(0, 0, w, h);
		}

		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		}
	}

	@Override
	public void onDroneFlyingStateChanged(boolean flying) {
		Log.d("Drone", "onDroneFlyingStateChanged");

	}

	@Override
	public void onDroneReady() {
		// TODO Auto-generated method stub
		Log.d("Drone", "onDroneReady");
	}

	@Override
	public void onDroneConnected() {
		Log.d("Drone", "onDroneConnected");

	}

	@Override
	public void onDroneDisconnected() {
		Log.d("Drone", "onDroneDisconnected");

	}

}