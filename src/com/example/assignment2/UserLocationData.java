package com.example.assignment2;

import java.util.List;

import android.location.Address;

public class UserLocationData {
	
	// Private members
	private int _id;
	private float _accelX;
	private float _accelY;
	private float _accelZ;
	private int _orientation;
	private double _lat;
	private double _long;
	private String _address;
	private String _activity;
	
	// Empty constructor
	public UserLocationData() {
		// Don't initialize private members because having _id = 0 or _accelX = 0 could be legitimate values for these attributes
	}
	
	// Full-blown constructor
	public UserLocationData(float aX, float aY, float aZ, int orientation, double latitude, double longitude, List<Address> address) {
		this._accelX = aX;
		this._accelY = aY;
		this._accelZ = aZ;
		this._orientation = orientation;
		this._lat = latitude;
		this._long = longitude;
		for(int i = 0; i < address.size(); i++) {
			this._address += address.get(i).toString() + " | ";
		}
	}

	// Get the ID
	public int getID() {
		return this._id;
	}
	
	// Set the ID
	public void setID(int id) {
		this._id = id;
	}
	
	// Get the force on the X axis
	public float getAccelX() {
		return this._accelX;
	}
	
	// Set the force on the X axis
	public void setAccelX(float x) {
		this._accelX = x;
	}
	
	// Get the force on the Y axis
	public float getAccelY() {
		return this._accelY;
	}
	
	// Set the force on the Y axis
	public void setAccelY(float y) {
		this._accelY = y;
	}
	
	// Get the force on the Z axis
	public float getAccelZ() {
		return this._accelZ;
	}
	
	// Set the force on the Z axis
	public void setAccelZ(float z) {
		this._accelZ = z;
	}
	
	// Get the orientation
	public int getOrientation() {
		return _orientation;
	}

	// Set the orientation
	public void setOrientation(int orient) {
		this._orientation = orient;
	}

	// Get the latitude
	public double getLat() {
		return this._lat;
	}
	
	// Set the latitude
	public void setLat(double latitude) {
		this._lat = latitude;
	}
	
	// Get the longitude
	public double getLong() {
		return this._long;
	}
	
	// Set the longitude
	public void setLong(double longitude) {
		this._long = longitude;
	}
	
	// Get the address
	public String getAddress() {
		return _address;
	}
	
	// Set the address
	public void setAddress(String address) {
		this._address = address;
	}

	// Get the user activity
	public String getActivity() {
		return _activity;
	}

	// Set the user activity
	public void setActivity(String activity) {
		this._activity = activity;
	}
}
