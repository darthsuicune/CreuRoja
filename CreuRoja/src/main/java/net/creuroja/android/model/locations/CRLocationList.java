package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.database.Cursor;

import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public class CRLocationList implements LocationList {
	List<Location> locationList;
	public CRLocationList(String json) {
		locationList = new ArrayList<>();
	}

	public CRLocationList(HttpResponse response) {
		locationList = new ArrayList<>();
	}

	public CRLocationList(Cursor cursor) {
		locationList = new ArrayList<>();
	}

	@Override
	public List<Location> getLocations() {
		return locationList;
	}

	@Override public Location get(long id) {
		return locationList.get((int)id);
	}

	@Override public String save(ContentResolver cr) {
		return null;
	}
}
