package net.creuroja.android.model.locations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import net.creuroja.android.model.db.CreuRojaContract;
import net.creuroja.android.model.webservice.lib.RestWebServiceClient;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denis on 19.06.14.
 */
public class RailsLocationList implements LocationList {
	private List<Location> locationList = new ArrayList<>();
	private List<Integer> mIdList = new ArrayList<>();
	private String lastUpdateTime = "";

	public RailsLocationList(HttpResponse response) {
		Log.d("TEST", "HTTP List created");
		try {
			createFromJson(RestWebServiceClient.getAsString(response));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public RailsLocationList(Cursor cursor) {
		Log.d("TEST", "Cursor list created");
		if(cursor.moveToFirst()) {
			do {
				Location location = new Location(cursor);
				locationList.add(location);
				mIdList.add(location.mRemoteId);
			} while(cursor.moveToNext());
		}
		cursor.close();
	}

	private void createFromJson(String json) throws JSONException {
		JSONArray array = new JSONArray(json);
		for(int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			Location location = new Location(object);
			locationList.add(location);
			mIdList.add(location.mRemoteId);
		}
	}

	@Override
	public List<Location> getLocations() {
		return locationList;
	}

	@Override public Location getById(long id) {
		for(Location location : locationList) {
			if(location.mId == id) {
				return location;
			}
		}
		return null;
	}

	@Override public Location get(int position) {
		return locationList.get(position);
	}

	@Override public void save(ContentResolver cr) {
		Uri uri = CreuRojaContract.Locations.CONTENT_LOCATIONS;
		LocationList currentLocations =
				new RailsLocationList(cr.query(uri, null, null, null, null));
		List<ContentValues> forInsert = new ArrayList<>();
		for(Location location : locationList) {
			if(location.newerThan(lastUpdateTime)) {
				lastUpdateTime = location.mUpdatedAt;
			}
			if(!location.mActive) {
				location.delete(cr);
			} else {
				if (currentLocations.has(location)) {
					location.update(cr);
				} else {
					forInsert.add(location.getAsValues());
				}
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
}
