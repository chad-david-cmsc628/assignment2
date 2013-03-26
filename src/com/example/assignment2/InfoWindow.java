package com.example.assignment2;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow extends Activity implements InfoWindowAdapter {
	
	private LinearLayout locationDataContainer, longLatContainer, accelDataContainer, infoWindowLayout;
	private TextView userActivityView, userLocationView, deviceOrientationView;
	private TextView latitudeView, longitudeView, xyzLabelView, xyzView;
	private String db_path = "/data/data/com.example.assignment2/databases/UserLocation.db";
	private SQLiteDB dbHandler;
	
	public InfoWindow () {
		/*
		this.setContentView(R.layout.info_window);
		userActivityView = (TextView) findViewById(R.id.userActivityView);
		userLocationView = (TextView) findViewById(R.id.userLocationView);
		deviceOrientationView = (TextView) findViewById(R.id.deviceOrientationView);
		latitudeView = (TextView) findViewById(R.id.latitudeView);
		longitudeView = (TextView) findViewById(R.id.longitudeView);
		xyzLabelView = (TextView) findViewById(R.id.xyzLabelView);
		xyzView = (TextView) findViewById(R.id.xyzView);
		longLatContainer = (LinearLayout) findViewById(R.id.longLatContainer);
		accelDataContainer = (LinearLayout) findViewById(R.id.accelDataContainer);
		infoWindowLayout = (LinearLayout) findViewById(R.layout.info_window);
		*/
	}

	public void setDatabaseHandler () {
		dbHandler = new SQLiteDB(InfoWindow.this.getApplicationContext(), db_path, null, 3, null);
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		Log.i("MyApp","Database Handler: " + db.toString());
	}
	
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public void setInfoWindow (double latitude_, double longitude_, float x_, float y_, float z_) {
		// TODO Auto-generated method stub
		userActivityView.setText("Grabbing coffee w/ Chad");
		userLocationView.setText("Location: " + "foo");
		deviceOrientationView.setText("Dvice Orientation: " + "bar");
		latitudeView.setText(Double.toString(latitude_));
		longitudeView.setText(Double.toString(longitude_));
		xyzView.setText("< X: " + Float.toString(x_) + ", Y: " + Float.toString(y_) + ", Z: " + Float.toHexString(z_));
	}
	*/

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
