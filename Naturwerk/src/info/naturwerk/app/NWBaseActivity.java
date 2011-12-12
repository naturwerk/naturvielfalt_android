package info.naturwerk.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NWBaseActivity extends Activity {
	NWApplication nwApplication;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    nwApplication = (NWApplication) getApplication();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) { // <5>
	
	  switch (item.getItemId()) {
	  case R.id.itemPrefs:
	    startActivity(new Intent(this, NWPrefsActivity.class)
	        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
	    break;
	  case R.id.itemToggleService:
	    if (nwApplication.isServiceRunning()) {
	      stopService(new Intent(this, NWUpdateService.class));
	    } else {
	      startService(new Intent(this, NWUpdateService.class));
	    }

	  }
	  return true;
	}
}