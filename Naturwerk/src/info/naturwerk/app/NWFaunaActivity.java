package info.naturwerk.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NWFaunaActivity extends NWBaseActivity {
	private static final String TAG = NWFaunaActivity.class.getSimpleName();
	Cursor cursor;
	ListView listCategory;
	SimpleCursorAdapter adapter;
	static final String[] FROM_FAUNA = { NWDataBase.FAUNA_NAME_DE,
			NWDataBase.FAUNA_SPECIES };
	static final String[] FROM_FLORA = { NWDataBase.FLORA_NAME_DE,
			NWDataBase.FLORA_NAME };
	static final int[] TO = { R.id.textFaunaName, R.id.textFaunaLatinName };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fauna);
		listCategory = (ListView) findViewById(R.id.listFauna);
		listCategory.setOnItemClickListener(new CategoryOnItemClickedListener());
		Log.i(TAG, "onCreated");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.setupList();

	}

	private void setupList() {
		// Get the data
		int category = ((NWApplication) getApplicationContext())
				.getCategory();

		if (category != 16) {
			cursor = nwApplication.getNWDataBase().getFauna(category);
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(this, R.layout.fauna_row, cursor,
					FROM_FAUNA, TO);
		} else {
			cursor = nwApplication.getNWDataBase().getFloras();
			startManagingCursor(cursor);
			adapter = new SimpleCursorAdapter(this, R.layout.fauna_row, cursor,
					FROM_FLORA, TO);

		}
		listCategory.setAdapter(adapter);
	}
	
	public class CategoryOnItemClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
			((NWApplication) getApplicationContext()).setObservation((int)id);

			Intent intent = new Intent(NWFaunaActivity.this, NWDetailActivity.class);
			startActivity(intent);

		}
	}

}
