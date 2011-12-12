package info.naturwerk.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class NWPrefsActivity extends PreferenceActivity {
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.prefs);
	  }	

}
