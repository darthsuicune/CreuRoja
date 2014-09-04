package net.creuroja.android.model.locations;

import android.content.ContentResolver;

import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public interface LocationList {
	public List<Location> getLocations();
	public Location getById(long id);
	public Location get(int position);
	public void save(ContentResolver cr);
	public boolean has(Location location);
	public String getLastUpdateTime();
	public void toggleLocationType(LocationType type, boolean newState);
	public boolean isVisible(int position);
}
