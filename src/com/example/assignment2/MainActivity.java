package com.example.assignment2;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.EditText;


public class MainActivity extends Activity implements SensorEventListener, LocationListener {

	// Declare variables
	private GoogleMap map;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private LocationManager locationManager;
	private Handler locationHandler = new Handler();
	private float accelX, accelY, accelZ;
	private String bestProvider;
	private Criteria providerCriteria;
	private InfoWindow customInfoWindow;
	private Geocoder geocoder;
	private List<Address> addresses;
	private DatabaseHandler dbHandler;
	private List<UserLocationData> markerList;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		// Instantiate GoogleMap object
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// Set the map's custom info window adapter
		customInfoWindow = new InfoWindow(MainActivity.this);
		map.setInfoWindowAdapter(customInfoWindow);
		
		// Set up database handler and database object
		dbHandler = new DatabaseHandler(this);
		db = dbHandler.getWritableDatabase();		
		
		// Retrieve all markers currently in the database on app creation
		markerList = dbHandler.getAllEntries();
		
		// Iterate through all markers and add them to the map
		for (int i = 0; i < markerList.size(); i++) {
			map.addMarker(new MarkerOptions ().position(
					new LatLng(markerList.get(i).getLat(), markerList.get(i).getLat()))
						.title(String.valueOf(markerList.get(i).getID())));
		}
		
		// Instantiate sensor manager object to access sensor services on an Android device
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		// Retrieve the accelerometer object if it exists
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			List<Sensor> mySensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			for (int i = 0; i < mySensors.size(); i++) {
				accelerometer = mySensors.get(i);
			}
		}
		
		// Instantiate a location manager object that will be used for getting latitude and longitude values
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Instantiate Criteria object to pass the type of desired accuracy expected from the location provider
		providerCriteria = new Criteria();
		providerCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		// Initialize the best provider variable and use this as an argument to requestLocationUpdates() so we
		// can get our location updates from this specified provider
		bestProvider = locationManager.getBestProvider(providerCriteria, true);
		
		// Register for location updates
		// Set the callback to fire when at least 1 minute has elapsed and 100 meters have been traveled
		locationManager.requestLocationUpdates(bestProvider, 60000, 100, MainActivity.this);

		// Get initial latitude and longitude from the location returned by getLastKnownLocation()
		//initialLocation = locationManager.getLastKnownLocation(bestProvider);
		//latLong = new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude());
		
		// Use reverse-geocoding to retrieve the human readable address associated with a location
		geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/********** LocationListener Methods **********/

	@Override
	public void onLocationChanged(final Location arg0) {	
		try {
			addresses = geocoder.getFromLocation(arg0.getLatitude(), arg0.getLongitude(), 1);
		}
		catch (IOException e) {
			System.out.println("Could not find any addresses associated with the location <latitude=" + arg0.getLongitude() + ", longitude=" + arg0.getLongitude() + ">");
		}

		final ContentValues values = new ContentValues();
		values.put(DatabaseHandler.getColumnNameAccelX(), accelX);
		values.put(DatabaseHandler.getColumnNameAccelY(), accelY);
		values.put(DatabaseHandler.getColumnNameAccelZ(), accelZ);
		values.put(DatabaseHandler.getColumnNameOrientation(), getResources().getConfiguration().orientation);
		values.put(DatabaseHandler.getColumnNameLatitude(), arg0.getLatitude());
		values.put(DatabaseHandler.getColumnNameLongitude(), arg0.getLongitude());
		
		// Build address line
		String address = "";
		for (int i = 0; i <= 2; i++) {
			if (addresses.get(0).getAddressLine(i) != null) {
				address += addresses.get(0).getAddressLine(i) + " ";
			}
		}
		values.put(DatabaseHandler.getColumnNameAddress(), address);
		
		// Create an alert dialog that will prompt the user to enter their activity
		final AlertDialog.Builder userActivityPrompt = new AlertDialog.Builder(this);
		userActivityPrompt.setTitle("What were you doing?");
		
		// Create an editable text field used to accept user input for their activity
		final EditText userActivityText = new EditText(this);
		userActivityPrompt.setView(userActivityText);
		
		// Register a listener for when users click the "OK" button on the dialog window
		userActivityPrompt.setPositiveButton("OK", new DialogInterface.OnClickListener () {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				values.put(DatabaseHandler.getColumnNameActivity(), userActivityText.getText().toString());
				
				// Insert record into the UserLocations table and return the ID
				final long recordID = db.insert(DatabaseHandler.getTableName(), null, values);
				
				// Add the marker to the map
				map.addMarker(new MarkerOptions ().position(
						new LatLng(arg0.getLatitude(), arg0.getLongitude()))
							.title(String.valueOf(recordID)));
				dialog.dismiss();
			}
			
		});
		
		// Register a listener for when users click the "Cancel" button on the dialog window
		userActivityPrompt.setNegativeButton("Cancel", new DialogInterface.OnClickListener () {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				values.put(DatabaseHandler.getColumnNameActivity(), "");
				
				// Insert record into the UserLocations table and return the ID
				final long recordID = db.insert(DatabaseHandler.getTableName(), null, values);
				
				// Add the marker to the map
				map.addMarker(new MarkerOptions ().position(
						new LatLng(arg0.getLatitude(), arg0.getLongitude()))
							.title(String.valueOf(recordID)));
				dialog.cancel();
			}
		});
		
		// Show the dialog prompt
		userActivityPrompt.show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	/********** SensorListener Methods **********/
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(final SensorEvent arg0) {
		// TODO Auto-generated method stub
		
		// Add to the thread's message queue
		locationHandler.post(new Runnable() {
			public void run() {
				if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					accelX = arg0.values[0];
					accelY = arg0.values[1];
					accelZ = arg0.values[2];
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// Register the sensor manager listener whenever the application is resumed - we don't want the
		// sensor service to continue consuming unnecessary power while the application is not in use
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// Release the reference to the database object when the application is destroyed
		if (db != null) {
			db.close();
		}
	}
}
