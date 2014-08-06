package org.creuroja.android.model;

import android.content.ContentResolver;

import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public class CRLocationList implements LocationList {
	public CRLocationList(String json) {

	}

	@Override
	public List<Location> getLocations() {
		return null;
	}

	@Override public String save(ContentResolver cr) {
		return null;
	}
}
