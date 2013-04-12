package com.cruzroja.creuroja;

import com.google.android.gms.maps.model.LatLng;

public class Location {
	public LatLng mPosition;
	public String mIcono;
	public String mContenido;
	
	public Location(double lat, double longit, String icon, String content){
		mPosition = new LatLng(lat, longit);
		mIcono = icon;
		mContenido = content;
	}
}
