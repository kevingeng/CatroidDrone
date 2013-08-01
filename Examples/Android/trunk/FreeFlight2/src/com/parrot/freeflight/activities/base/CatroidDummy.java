/**
 * 
 */
package com.parrot.freeflight.activities.base;

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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author GeraldW
 *
 */
public class CatroidDummy extends Activity {

    private DroneControlService droneControlService;
	
	Button btnConnect;
	Button btnDisconnect;
	Button btnTakeoff;
	Button btnLand;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub

		setContentView(R.layout.catroiddummy);
		btnConnect = (Button)findViewById(R.id.btn_connect);
		btnConnect.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				onConnectPressed();				
			}
		});
		
		btnDisconnect = (Button)findViewById(R.id.btn_disconnect);
		btnDisconnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDisconnectPressed();
			}
		});
		btnTakeoff = (Button)findViewById(R.id.btn_takeoff);
		btnTakeoff.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				onTakeoffPressed();
			}
		});
		
		btnLand = (Button)findViewById(id.btn_land);
		btnLand.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				onLandPresed();
			}
		});
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO Auto-generated method stub

	}
	
	private void onConnectPressed(){
		Log.d("Drone", "onConnectPressed");
		bindService(new Intent(this, DroneControlService.class), mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void onDisconnectPressed(){
		Log.d("Drone", "onDisconnectPressed");

	}
	
	private void onTakeoffPressed(){
		Log.d("Drone", "onTakeoffPressed");
		droneControlService.triggerTakeOff();

	}
	
	private void onLandPresed(){
		Log.d("Drone", "onLandPresed");
		droneControlService.triggerTakeOff();

	}
	
    private ServiceConnection mConnection = new ServiceConnection()
    {

        public void onServiceConnected(ComponentName name, IBinder service)
        {
            droneControlService = ((DroneControlService.LocalBinder) service).getService();
            onDroneServiceConnected();
        }

        public void onServiceDisconnected(ComponentName name)
        {
            droneControlService = null;
        }
    };
    
    /**
     * Called when we connected to DroneControlService
     */
    protected void onDroneServiceConnected()
    {
    	if (droneControlService != null) {
            droneControlService.resume();
            droneControlService.requestDroneStatus();
        } else {
            Log.w("Drone", "DroneServiceConnected event ignored as DroneControlService is null");
        }

    	Toast.makeText(getApplicationContext(), "connected to Drone", Toast.LENGTH_SHORT).show();
        //settingsDialog = new SettingsDialog(this, this, droneControlService, magnetoAvailable);

        //applySettings(settings);

        //initListeners();
        //runTranscoding();
        
        //if (droneControlService.getMediaDir() != null) {
        //    view.setRecordButtonEnabled(true);
        //    view.setCameraButtonEnabled(true);
        //}
    }    

	
	

}
