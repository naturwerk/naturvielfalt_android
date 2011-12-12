package info.naturwerk.app;

import android.os.Bundle;
import android.widget.TextView;

public class NWDetailActivity extends NWBaseActivity {
	private static final String TAG = NWDetailActivity.class.getSimpleName();
	private NWObservation obs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		obs = ((NWApplication) super.getApplication()).getObservation();
		//((TextView) findViewById(R.id.editUser)).setText(((WPApplication) super.getApplication()).getPrefs().getString("user", ""));
		
		((TextView)findViewById(R.id.detailTextFamily)).setText(obs.faunaFamily);
		((TextView)findViewById(R.id.detailTextNameDe)).setText(obs.faunaNameDe);
		((TextView)findViewById(R.id.detailTextName)).setText(obs.faunaFamily);
		
		

	}
	

}
