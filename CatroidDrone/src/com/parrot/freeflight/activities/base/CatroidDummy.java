/**
 * 
 */
package com.parrot.freeflight.activities.base;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.parrot.freeflight.R;
import com.parrot.freeflight.R.id;
import com.parrot.freeflight.activities.SettingsDialog;
import com.parrot.freeflight.service.DroneControlService;

import android.R.layout;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
 * @author GeraldW
 * 
 */
public class CatroidDummy extends Activity {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
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
				} else {
					topView.setBackgroundColor(0xFFFF0000);
					topView.invalidate();
					isDroneConnected = false;
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
					moveUp(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					moveUp(0);
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
					moveDown(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					moveDown(0);
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
					moveLeft(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					moveLeft(0);
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
					moveRight(power);
					return true;
				case MotionEvent.ACTION_UP:
					// RELEASED
					moveRight(0);
					return true;
				}
				return false;
			}
		});

	}

	public float getConvertedValue(int intVal) {
		float floatVal = 0.0f;
		floatVal = .01f * intVal;
		return floatVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
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
		try {
			getApplicationContext().unbindService(mConnection);
		} catch (IllegalArgumentException e) {
			// Exception will be ignored
		}
		Toast.makeText(getApplicationContext(), "Disconnected to Drone",
				Toast.LENGTH_SHORT).show();
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

	public void doLeftFlip() {
		Log.d("Drone", "doLeftFlip");
		droneControlService.doLeftFlip();
	}

	public void moveLeft(final float power) {
		Log.d("Drone", "turnLeft");
		droneControlService.turnLeft(power);
	}

	public void moveRight(final float power) {
		Log.d("Drone", "turnRight");
		droneControlService.turnRight(power);
	}

	public void moveUp(final float power) {
		Log.d("Drone", "moveUp");
		droneControlService.moveUp(power);
	}

	public void moveDown(final float power) {
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

}
