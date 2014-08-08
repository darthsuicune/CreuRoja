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
		try {
			createFromJson(RestWebServiceClient.getAsString(response));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("TEST", "List created");
	}

	public RailsLocationList(Cursor cursor) {
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

	@Override public Location get(long id) {
		return locationList.get((int) id);
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
				locationList.remove(location);
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
					(ContentValues[]) forInsert.toArray());
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
