package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import net.creuroja.android.model.db.CreuRojaContract;
import net.creuroja.android.model.webservice.lib.RestWebServiceClient;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by denis on 19.06.14.
 */
public class RailsLocationList implements LocationList {
	private List<Location> mLocationList = new ArrayList<>();
	private List<Integer> mIdList = new ArrayList<>();
	private String lastUpdateTime = "";
	private Map<LocationType, Boolean> mToggledLocations;
	private SharedPreferences prefs;

	public RailsLocationList(HttpResponse response, SharedPreferences prefs) {
		try {
			createFromJson(RestWebServiceClient.getAsString(response));
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		init(prefs);
	}

	public RailsLocationList(Cursor cursor, SharedPreferences prefs) {
		if(cursor.moveToFirst()) {
			do {
				Location location = new Location(cursor);
				mLocationList.add(location);
				mIdList.add(location.mRemoteId);
			} while(cursor.moveToNext());
		}
		cursor.close();
		init(prefs);
	}

	private void init(SharedPreferences prefs) {
		this.prefs = prefs;
		mToggledLocations = new HashMap<>();
		for(LocationType type : LocationType.values()) {
			mToggledLocations.put(type, type.getViewable(prefs));
		}
	}

	private void createFromJson(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		for(int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			Location location = new Location(object);
			mLocationList.add(location);
			mIdList.add(location.mRemoteId);
		}
	}

	@Override
	public List<Location> getLocations() {
		List<Location> result = new ArrayList<>();
		for(Location location : mLocationList) {
			if(mToggledLocations.get(location.mType)) {
				result.add(location);
			}
		}
		return result;
	}

	@Override public Location getById(long id) {
		for(Location location : mLocationList) {
			if(location.mRemoteId == id) {
				return location;
			}
		}
		return null;
	}

	@Override public Location get(int position) {
		return mLocationList.get(position);
	}

	@Override public void save(ContentResolver cr) {
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		LocationList currentLocations =
				new RailsLocationList(cr.query(uri, null, null, null, null), prefs);
		List<ContentValues> forInsert = new ArrayList<>();
		for(Location location : mLocationList) {
			if(location.newerThan(lastUpdateTime)) {
				lastUpdateTime = location.mUpdatedAt;
			}
			if(location.mActive) {
				if (currentLocations.has(location)) {
					location.update(cr);
				} else {
					forInsert.add(location.getAsValues());
				}
			} else {
				location.delete(cr);
			}
		}
		if(forInsert.size() > 0) {
			cr.bulkInsert(CreuRojaContract.Locations.CONTENT_LOCATIONS,
					forInsert.toArray(new ContentValues[forInsert.size()]));
		}
	}

	public boolean has(Location location) {
		for(Integer current : mIdList) {
			if(current == location.mRemoteId) {
				return true;
			}
		}
		return false;
	}

	@Override public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	@Override public void toggleLocationType(LocationType type, boolean newState) {
		mToggledLocations.put(type, newState);
	}

	@Override public boolean isVisible(int position) {
		return mLocationList.get(position).isVisible(prefs);
	}
}
