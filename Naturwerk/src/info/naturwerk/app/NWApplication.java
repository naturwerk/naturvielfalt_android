package info.naturwerk.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class NWApplication extends Application implements OnSharedPreferenceChangeListener {
	private static final String TAG = NWApplication.class.getSimpleName();
	private SharedPreferences prefs;
	private boolean serviceRunning;
	private NWDataBase nwDataBase;
	private NWObservation observation;
	private int category;

	public NWObservation getObservation() {
		return observation;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		this.nwDataBase = new NWDataBase(this);
		Log.i(TAG, "onCreated");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		; // who cares
	}

	public boolean isServiceRunning() {
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

	public NWDataBase getNWDataBase() {
		return nwDataBase;
	}
	
	public boolean setObservation(int id) {
		observation = new NWObservation();
		return this.getNWDataBase().createObservation(observation, id);
	}

	public synchronized void fetchServerData() {
		try {

			URL nwURL = new URL(getPrefs().getString("apiRoot", "http://devel.naturvielfalt.ch/api/organisms"));

			JSONArray jArr = new JSONArray(getJSON(nwURL));
			ContentValues values = new ContentValues();

			for (int i = 0; i < jArr.length(); i++) {
				JSONObject item = jArr.getJSONObject(i);
				Log.d(TAG, "Name:" + item.getString("name"));
				values.put(NWDataBase.C_NAME, item.getString("name"));
				Log.d(TAG, "Count:" + item.getString("count"));
				values.put(NWDataBase.C_COUNT, item.getString("count"));
				Log.d(TAG, "Inventory ID:" + item.getString("inventory_type_id"));
				values.put(NWDataBase.C_INVENTORY_TYPE_ID, item.getString("inventory_type_id"));
				this.getNWDataBase().insertCategoryOrIgnore(values);

				int category_id = Integer.parseInt(item.getString("inventory_type_id"));
				URL catURL = new URL(nwURL.toString() + "/" + category_id);

				Log.d(TAG, "Getting JSON:" + catURL.toString());

				if (category_id == 7) // too much data
					continue;
				
				JSONArray jCatArr = new JSONArray(getJSON(catURL));

				for (int j = 0; j < jCatArr.length(); j++) {
					JSONObject catItem = jCatArr.getJSONObject(j);
					if (category_id != 16) // fauna
					{
						ContentValues faunaCon = new ContentValues();
						Log.v(TAG, "FAUNA, ID:" + catItem.getString("id"));
						faunaCon.put(NWDataBase.FAUNA_INVENTORY_TYPE_ID, catItem.getString("id"));
						faunaCon.put(NWDataBase.FAUNA_NAME, catItem.getString("name"));
						faunaCon.put(NWDataBase.FAUNA_FAMILY, catItem.getString("family"));
						faunaCon.put(NWDataBase.FAUNA_PROTECTION, catItem.getString("protection_ch"));
						faunaCon.put(NWDataBase.FAUNA_CSCF_NR, catItem.getString("cscf_nr"));
						faunaCon.put(NWDataBase.FAUNA_NAME_DE, catItem.getString("name_de"));
						faunaCon.put(NWDataBase.FAUNA_GENUS, catItem.getString("genus"));
						faunaCon.put(NWDataBase.FAUNA_SPECIES, catItem.getString("species"));
						faunaCon.put(NWDataBase.FAUNA_CLASS_ID, catItem.getString("fauna_class_id"));
						this.getNWDataBase().insertFaunaOrIgnore(faunaCon);
						faunaCon.clear();
					} else // flora
					{
						ContentValues faunaCon = new ContentValues();
						faunaCon.put(NWDataBase.FLORA_INVENTORY_TYPE_ID, catItem.getString("id"));
						faunaCon.put(NWDataBase.FLORA_NAME, catItem.getString("name"));
						faunaCon.put(NWDataBase.FLORA_FAMILY, catItem.getString("familie"));
						faunaCon.put(NWDataBase.FLORA_GATTUNG, catItem.getString("gattung"));
						faunaCon.put(NWDataBase.FLORA_ART, catItem.getString("art"));
						faunaCon.put(NWDataBase.FLORA_NAME_DE, catItem.getString("name_de"));
						faunaCon.put(NWDataBase.FLORA_IS_NEOPHYTE, catItem.getString("is_neophyte"));
						faunaCon.put(NWDataBase.FLORA_STATUS, catItem.getString("status"));
						this.getNWDataBase().insertFloraOrIgnore(faunaCon);
						faunaCon.clear();
					}
				}
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String getJSON(URL nwURL) throws IOException {
		URLConnection nwConn = nwURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(nwConn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = in.readLine()) != null) {
			sb.append(line + "\n");
		}

		in.close();
		return sb.toString();
	}

	
}
