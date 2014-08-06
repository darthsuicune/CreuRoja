package net.creuroja.android.model;

import android.content.ContentResolver;

import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public interface LocationList {
	public List<Location> getLocations();
	public String save(ContentResolver cr);
}
