package com.example.assignment2;

import java.util.List;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener, LocationListener {

	// Declare variables
	private GoogleMap map;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private LocationManager locationManager;
	private Handler locationHandler = new Handler();
	private LatLng latLong;
	private double latitude = 0; 
	private double longitude = 0;
	private float x, y, z;
	private String bestProvider;
	private Criteria providerCriteria;
	private Location initialLocation;
	private Marker initialMarker;
	//private LinearLayout locationDataContainer, longLatContainer, accelDataContainer, infoWindowLayout;
	//private TextView userActivityView, userLocationView, deviceOrientationView;
	//private TextView latitudeView, longitudeView, xyzLabelView, xyzView;
	private InfoWindow customInfoWindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("MyApp", "Files Path: " + this.getFilesDir().getAbsolutePath());
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		// Set the map's custom info window adapter
		customInfoWindow = new InfoWindow();
		map.setInfoWindowAdapter(customInfoWindow);
		
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			List<Sensor> mySensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
			for (int i = 0; i < mySensors.size(); i++) {
				accelerometer = mySensors.get(i);
			}
		}

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Instantiate Criteria object to pass the type of desired accuracy expected from the location provider
		providerCriteria = new Criteria();
		providerCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		// Initialize the best provider variable and use this as an argument to requestLocationUpdates() so we
		// can get our location updates from this specified provider
		bestProvider = locationManager.getBestProvider(providerCriteria, true);
		
		// Register for location updates
		locationManager.requestLocationUpdates(bestProvider, 10, 0, MainActivity.this);

		// Get initial latitude and longitude from the location returned by getLastKnownLocation()
		initialLocation = locationManager.getLastKnownLocation(bestProvider);
		latLong = new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude());
		
		// Add a marker with this initial location
		initialMarker = map.addMarker(new MarkerOptions ().position(latLong).title("I'm Here!"));
		
		/*
		 * 
		// Initialize text views to be used for the custom info window adapter
		userActivityView = new TextView(MainActivity.this);
		userLocationView = new TextView(MainActivity.this);
		deviceOrientationView = new TextView(MainActivity.this);
		longLatContainer = new LinearLayout(MainActivity.this);
		accelDataContainer = new LinearLayout(MainActivity.this);
		
		latitudeView = new TextView(MainActivity.this);
		longitudeView = new TextView(MainActivity.this);
		xyzLabelView = new TextView(MainActivity.this);
		xyzView = new TextView(MainActivity.this);
		
		longLatContainer.addView(latitudeView);
		longLatContainer.addView(longitudeView);
		
		accelDataContainer.addView(xyzLabelView);
		accelDataContainer.addView(xyzView);
		
		locationDataContainer = new LinearLayout(MainActivity.this);
		locationDataContainer.setOrientation(LinearLayout.HORIZONTAL);
		locationDataContainer.addView(longLatContainer);
		locationDataContainer.addView(accelDataContainer);
		
		infoWindowLayout = new LinearLayout(MainActivity.this);
		infoWindowLayout.addView(userActivityView);
		infoWindowLayout.addView(userLocationView);
		infoWindowLayout.addView(deviceOrientationView);
		infoWindowLayout.addView(locationDataContainer);
		
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/********** LocationListener Methods **********/

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		this.latitude = arg0.getLatitude();
		this.longitude = arg0.getLongitude();
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

	/********** LocationListener Methods **********/
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(final SensorEvent arg0) {
		// TODO Auto-generated method stub
		// Add to message queue
		locationHandler.post(new Runnable() {
			public void run() {
				if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					x = arg0.values[0];
					y = arg0.values[1];
					z = arg0.values[2];
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/********** InfoWindowAdapter Methods **********/
	
	/*
	@Override
	public View getInfoContents(Marker clickedMarker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker clickedMarker) {
		// TODO Auto-generated method stub
		userActivityView.setText("Grabbing coffee w/ Chad");
		userLocationView.setText("Location: " + "foo");
		deviceOrientationView.setText("Dvice Orientation: " + "bar");
		latitudeView.setText(Double.toString(latitude));
		longitudeView.setText(Double.toString(longitude));
		xyzView.setText("< X: " + Float.toString(x) + ", Y: " + Float.toString(y) + ", Z: " + Float.toHexString(z));
		return infoWindowLayout;
	}
	*/
}
