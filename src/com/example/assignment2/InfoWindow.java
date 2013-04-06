package com.example.assignment2;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements InfoWindowAdapter {
	
	private LinearLayout locationDataContainer, longLatContainer, accelDataContainer, infoWindowLayout;
	private TextView userActivityView, userLocationView, deviceOrientationView;
	private TextView latitudeView, longitudeView, xyzLabelView, xyzView;
	private LayoutParams infoWindowLayoutParams;
	private DatabaseHandler dbHandler;
	private Context applicationContext;
	
	public InfoWindow (Context applicationContext_) {
		// Take in the global application context
		applicationContext = applicationContext_;
		
		// Set database handler
		dbHandler = new DatabaseHandler(applicationContext);
		
		// Initialize views to be used for the custom info window adapter
		userActivityView = new TextView(applicationContext);
		userLocationView = new TextView(applicationContext);
		deviceOrientationView = new TextView(applicationContext);
		longLatContainer = new LinearLayout(applicationContext);
		accelDataContainer = new LinearLayout(applicationContext);
		
		latitudeView = new TextView(applicationContext);
		longitudeView = new TextView(applicationContext);
		xyzLabelView = new TextView(applicationContext);
		xyzLabelView.setText("Accelerometer Data:");
		xyzView = new TextView(applicationContext);
		
		longLatContainer.setOrientation(LinearLayout.VERTICAL);
		longLatContainer.addView(latitudeView);
		longLatContainer.addView(longitudeView);
		
		accelDataContainer.setOrientation(LinearLayout.VERTICAL);
		accelDataContainer.addView(xyzLabelView);
		accelDataContainer.addView(xyzView);
		
		locationDataContainer = new LinearLayout(applicationContext);
		locationDataContainer.setOrientation(LinearLayout.HORIZONTAL);
		locationDataContainer.addView(longLatContainer);
		locationDataContainer.addView(accelDataContainer);
		
		infoWindowLayout = new LinearLayout(applicationContext);
		infoWindowLayout.addView(userActivityView);
		infoWindowLayout.addView(userLocationView);
		infoWindowLayout.addView(deviceOrientationView);
		infoWindowLayout.addView(locationDataContainer);
		
		infoWindowLayoutParams = new LayoutParams(100, 100);
		infoWindowLayout.setLayoutParams(infoWindowLayoutParams);
		infoWindowLayout.setOrientation(LinearLayout.VERTICAL);
		infoWindowLayout.setBackgroundColor(Color.WHITE);
	}
	
	// Populates the info window with user location data
	public void populateInfoWindow (int primaryKey) {		
		UserLocationData markerData = dbHandler.getEntry(primaryKey);
		userActivityView.setText("Activity: " + markerData.getActivity());
		userLocationView.setText("Location: " + markerData.getAddress());
		String orientation = "";
		if (markerData.getOrientation() == 1) {
			orientation = "Portrait";
		}
		else if (markerData.getOrientation() == 2) {
			orientation = "Landscape";
		}
		deviceOrientationView.setText("Device Orientation: " + orientation);
		latitudeView.setText("Latitude: " + String.valueOf(markerData.getLat()));
		longitudeView.setText("Longitude: " + String.valueOf(markerData.getLong()));
		xyzView.setText("<X: " + markerData.getAccelX() + ", Y: " + markerData.getAccelY() + ", Z: " + markerData.getAccelZ() + ">");
	}
	
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// Returns a custom info window that will display on the map when a marker is touched
	@Override
	public View getInfoWindow(Marker arg0) {
		int id = Integer.valueOf(arg0.getTitle());
		populateInfoWindow(id);
		return infoWindowLayout;
	}
}
