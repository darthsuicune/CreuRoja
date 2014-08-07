package net.creuroja.android.model;

import android.content.ContentResolver;

import org.apache.http.HttpResponse;

import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public class CRLocationList implements LocationList {
	List<Location> locationList;
	public CRLocationList(String json) {

	}

	public CRLocationList(HttpResponse response) {

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
