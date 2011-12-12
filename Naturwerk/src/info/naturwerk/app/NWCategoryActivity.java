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

public class NWCategoryActivity extends NWBaseActivity {
	private static final String TAG = NWCategoryActivity.class.getSimpleName();
	Cursor cursor;
	ListView listCategory;
	SimpleCursorAdapter adapter;
	static final String[] FROM = { NWDataBase.C_NAME, NWDataBase.C_COUNT };
	static final int[] TO = { R.id.textCategoryName, R.id.textCategoryCount };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		listCategory = (ListView) findViewById(R.id.listCategory);
		listCategory.setOnItemClickListener(new CategoryOnItemClickedListener());
		Log.i(TAG, "onCreated");

	}

	@Override
	protected void onResume() {
		super.onResume();
		this.setupList();

	}

	private void setupList() { // <5>
		// Get the data
		cursor = nwApplication.getNWDataBase().getCategories();
		startManagingCursor(cursor);

		adapter = new SimpleCursorAdapter(this, R.layout.category_row, cursor, FROM, TO);
		listCategory.setAdapter(adapter);
	}

	public class CategoryOnItemClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
			((NWApplication) getApplicationContext()).setCategory((int)id);

			Intent intent = new Intent(NWCategoryActivity.this, NWFaunaActivity.class);
			startActivity(intent);

		}
	}

}
