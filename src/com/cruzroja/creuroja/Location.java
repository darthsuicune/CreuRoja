package com.cruzroja.creuroja;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.cruzroja.creuroja.database.CreuRojaContract;
import com.cruzroja.creuroja.database.CreuRojaProvider;

public class Location {
	public static final String MARKER_NEW_LINE = "<br />";
	public static final String MARKER_STRONG = "<strong>";
	public static final String MARKER_STRONG_END = "</strong>";
	public static final String MARKER_SPACE = "&nbsp;";

	public static final String ICON_ASAMBLEA = "asamblea.png";
	public static final String ICON_BRAVO = "bravo.png";
	public static final String ICON_CUAP = "cuap.png";
	public static final String ICON_HOSPITAL = "hospital.png";
	public static final String ICON_MARITIMO = "maritimo.png";
	public static final String ICON_TERRESTRE = "terrestre.png";
	public static final int NO_ICON = 0;

	public double mLat;
	public double mLong;
	public int mIcono;
	public Contenido mContenido;

	public Location(Cursor c) {
		if (c.moveToFirst()) {
			mLat = c.getDouble(c
					.getColumnIndex(CreuRojaContract.Locations.LATITUD));
			mLong = c.getDouble(c
					.getColumnIndex(CreuRojaContract.Locations.LONGITUD));
			mIcono = c
					.getInt(c.getColumnIndex(CreuRojaContract.Locations.ICON));
			mContenido = new Contenido(c.getString(c
					.getColumnIndex(CreuRojaContract.Locations.NAME)));
			mContenido.mLugar = c.getString(c
					.getColumnIndex(CreuRojaContract.Locations.ADDRESS));
			mContenido.mDetalles = c.getString(c
					.getColumnIndex(CreuRojaContract.Locations.DETAILS));
		}
	}

	public Location(double lat, double longitud, String icon, String content) {
		mLat = lat;
		mLong = longitud;
		mIcono = getIcon(icon);
		mContenido = getContenido(content);
	}
	
	public Location(double lat, double longitud, String icon, String name, String address, String details) {
		mLat = lat;
		mLong = longitud;
		mIcono = getIcon(icon);
		mContenido = new Contenido(name, address, details);
	}

	public void saveToDb(ContentResolver cr) {
		
		ContentValues values = new ContentValues();
		if(mContenido.mLugar != null) {
			values.put(CreuRojaContract.Locations.ADDRESS, mContenido.mLugar);	
		}
		if(mContenido.mDetalles != null) {
			values.put(CreuRojaContract.Locations.DETAILS, mContenido.mDetalles);
		}
		values.put(CreuRojaContract.Locations.ICON, mIcono);
		values.put(CreuRojaContract.Locations.LATITUD, mLat);
		values.put(CreuRojaContract.Locations.LONGITUD, mLong);
		values.put(CreuRojaContract.Locations.NAME, mContenido.mNombre);
		cr.insert(CreuRojaProvider.CONTENT_LOCATIONS, values);
	}

	public int getIcon(String icon) {
		if (icon.equals(ICON_ASAMBLEA)) {
			return R.drawable.asamblea;
		} else if (icon.equals(ICON_BRAVO)) {
			return R.drawable.bravo;
		} else if (icon.equals(ICON_CUAP)) {
			return R.drawable.cuap;
		} else if (icon.equals(ICON_HOSPITAL)) {
			return R.drawable.hospital;
		} else if (icon.equals(ICON_MARITIMO)) {
			return R.drawable.maritimo;
		} else if (icon.equals(ICON_TERRESTRE)) {
			return R.drawable.terrestre;
		} else {
			return NO_ICON;
		}
	}

	public Contenido getContenido(String contenido) {
		if (!contenido.contains(MARKER_NEW_LINE)) {
			return new Contenido(contenido.replace(MARKER_STRONG, "").replace(
					MARKER_STRONG_END, ""));
		}
		String nombre = contenido
				.substring(0, contenido.indexOf(MARKER_NEW_LINE))
				.replace(MARKER_STRONG, "").replace(MARKER_STRONG_END, "");

		String lugar = contenido
				.substring(contenido.indexOf(MARKER_NEW_LINE),
						contenido.lastIndexOf(MARKER_NEW_LINE))
				.replace(MARKER_NEW_LINE, "").replaceAll(MARKER_SPACE, "");
		String horario = contenido
				.substring(contenido.lastIndexOf(MARKER_NEW_LINE))
				.replace(MARKER_NEW_LINE, "").replaceAll(MARKER_SPACE, "");
		return new Contenido(nombre, lugar, horario);
	}

	public static class Contenido {
		public String mNombre;
		public String mLugar;
		public String mDetalles;
		public String mSnippet;

		public Contenido(String nombre) {
			mNombre = nombre;
		}

		public Contenido(String nombre, String lugar, String horario) {
			mNombre = nombre;
			mLugar = lugar;
			mDetalles = horario;
			mSnippet = mLugar + MARKER_NEW_LINE + mDetalles;
		}
		
		public void createSnippet(){
			mSnippet = mLugar + MARKER_NEW_LINE + mDetalles;
		}
	}
}
