package info.naturwerk.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NWDataBase {
	private static final String TAG = NWDataBase.class.getSimpleName();
	static final int VERSION = 6;
	static final String DATABASE = "nwdb.db";
	static final String CATEGORY_TABLE = "category";
	static final String FLORA_TABLE = "flora";
	static final String FAUNA_TABLE = "fauna";

	// category table fields
	public static final String C_COUNT = "count";
	public static final String C_INVENTORY_TYPE_ID = "_id";
	public static final String C_NAME = "name";

	// flora table fields
	public static final String FLORA_INVENTORY_TYPE_ID = "_id"; // int
	public static final String FLORA_FAMILY = "familie"; // text
	public static final String FLORA_NAME = "name"; // text
	public static final String FLORA_GATTUNG = "gattung"; // text
	public static final String FLORA_ART = "art"; // text
	public static final String FLORA_NAME_DE = "name_de"; // text
	public static final String FLORA_IS_NEOPHYTE = "is_neophyte"; // int
	public static final String FLORA_STATUS = "status"; // texts

	// fauna item table
	public static final String FAUNA_INVENTORY_TYPE_ID = "_id"; // int
	public static final String FAUNA_FAMILY = "family"; // text
	public static final String FAUNA_NAME = "name";
	public static final String FAUNA_PROTECTION = "protection_ch"; // int
	public static final String FAUNA_CSCF_NR = "cscf_nr";
	public static final String FAUNA_NAME_DE = "name_de";
	public static final String FAUNA_GENUS = "genus";
	public static final String FAUNA_SPECIES = "species";
	public static final String FAUNA_CLASS_ID = "fauna_class_id";

	private static final String GET_ALL_CATEGORIES_ORDER_BY = C_NAME + " ASC";
	private static final String GET_ALL_FLORAS_ORDER_BY = FLORA_NAME_DE + " ASC";
	private static final String GET_ALL_FAUNAS_ORDER_BY = FAUNA_NAME_DE + " ASC";

	// DbHelper implementations
	class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(TAG, "Creating database: " + DATABASE);
			Log.i(TAG, "Creating table: " + CATEGORY_TABLE);
			db.execSQL("create table " + CATEGORY_TABLE + " (" + C_INVENTORY_TYPE_ID + " int primary key, " + C_COUNT + " int, " + C_NAME
					+ " text)");

			Log.i(TAG, "Creating table: " + FLORA_TABLE);
			db.execSQL("create table " + FLORA_TABLE + " (" + FLORA_INVENTORY_TYPE_ID + " int primary key, " + FLORA_FAMILY + " text, "
					+ FLORA_NAME + " text, " + FLORA_GATTUNG + " text, " + FLORA_ART + " text, " + FLORA_NAME_DE + " text, "
					+ FLORA_IS_NEOPHYTE + " int, " + FLORA_STATUS + " text )");

			Log.i(TAG, "Creating table: " + FAUNA_TABLE);
			db.execSQL("create table " + FAUNA_TABLE + " (" + FAUNA_INVENTORY_TYPE_ID + " int primary key, " + FAUNA_FAMILY + " text, "
					+ FAUNA_NAME + " text, " + FAUNA_PROTECTION + " int, " + FAUNA_CSCF_NR + " int, " + FAUNA_NAME_DE + " text, "
					+ FAUNA_GENUS + " text, " + FAUNA_SPECIES + " text, " + FAUNA_CLASS_ID + " int)");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.i(TAG, "Creating database: " + DATABASE);
			db.execSQL("drop table " + CATEGORY_TABLE);
			db.execSQL("drop table " + FLORA_TABLE);
			db.execSQL("drop table " + FAUNA_TABLE);
			this.onCreate(db);
		}
	}

	final DbHelper dbHelper;

	public NWDataBase(Context context) {
		this.dbHelper = new DbHelper(context);
		Log.i(TAG, "Initialized data");
	}

	public void close() {
		this.dbHelper.close();
	}

	public void insertCategoryOrIgnore(ContentValues values) {
		Log.d(TAG, "insertCategoryOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(CATEGORY_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}

	public void insertFloraOrIgnore(ContentValues values) {
		// TODO: bulk insert
		Log.d(TAG, "insertFloraOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(FLORA_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}

	public void insertFaunaOrIgnore(ContentValues values) {
		// TODO: bulk insert
		Log.d(TAG, "insertFaunaOrIgnore on " + values);
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		try {
			db.insertWithOnConflict(FAUNA_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		} finally {
			db.close();
		}
	}

	public Cursor getCategories() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(CATEGORY_TABLE, null, null, null, null, null, GET_ALL_CATEGORIES_ORDER_BY);
	}

	public Cursor getFloras() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FLORA_TABLE, null, null, null, null, null, GET_ALL_FLORAS_ORDER_BY);
	}
	
	public Cursor getFlora(int category) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FLORA_TABLE, null, null, null, null, null, GET_ALL_FLORAS_ORDER_BY);
	}

	public Cursor getFaunas() {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FAUNA_TABLE, null, null, null, null, null, GET_ALL_FAUNAS_ORDER_BY);
	}

	public Cursor getFauna(int category) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FAUNA_TABLE, null, FAUNA_CLASS_ID + " = " + Integer.toString(category), null, null, null, GET_ALL_FAUNAS_ORDER_BY);
	}
	
	public Cursor getFaunaItem(int item) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FAUNA_TABLE, null, FAUNA_INVENTORY_TYPE_ID + " = " + Integer.toString(item), null, null, null, null);
	}
	
	public Cursor getFloraItem(int item) {
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		return db.query(FLORA_TABLE, null, FLORA_INVENTORY_TYPE_ID + " = " + Integer.toString(item), null, null, null, null);
	}
	
	
	
	public boolean createObservation(NWObservation obs, int obsId)
	{
		obs.id = obsId;
		if(obs.id == 16) //got flora
		{
			Cursor c = getFauna(obs.id);
			if(!c.moveToFirst())
				return false;
			obs.floraArt = c.getString(c.getColumnIndex(FLORA_ART));
			obs.floraFamilie = c.getString(c.getColumnIndex(FLORA_FAMILY));
			obs.floraGattung = c.getString(c.getColumnIndex(FLORA_GATTUNG));
			obs.floraIsNeophyte = c.getString(c.getColumnIndex(FLORA_IS_NEOPHYTE));
			obs.floraName = c.getString(c.getColumnIndex(FLORA_NAME));
			obs.floraNameDe = c.getString(c.getColumnIndex(FLORA_NAME_DE));
			obs.floraStatus = c.getString(c.getColumnIndex(FLORA_STATUS));
		}
		else
		{
			Cursor c = getFaunaItem(obs.id);
			if(!c.moveToFirst())
				return false;
			obs.faunaClassId = c.getInt(c.getColumnIndex(FAUNA_CLASS_ID));
			obs.faunaCscfNr = c.getInt(c.getColumnIndex(FAUNA_CSCF_NR));
			obs.faunaFamily = c.getString(c.getColumnIndex(FAUNA_FAMILY));
			obs.faunaGenus = c.getString(c.getColumnIndex(FAUNA_GENUS));
			obs.faunaName = c.getString(c.getColumnIndex(FAUNA_NAME));
			obs.faunaNameDe = c.getString(c.getColumnIndex(FAUNA_NAME_DE));
			obs.faunaProtectionCh = c.getInt(c.getColumnIndex(FAUNA_PROTECTION));
			obs.faunaSpecies = c.getString(c.getColumnIndex(FAUNA_SPECIES));
			
		}
		return true;
	}

	public boolean getFloraItem(NWObservation obs)
	{
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		if(obs.id == 16) //got flora
		{
			Cursor cur = db.query(FLORA_TABLE, null, "_ID = " + Integer.toString(obs.id), null, null, null, null);
			if(!cur.moveToFirst())
				return false;
			
		}
		
		return false;
	}
	
	

	public void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(CATEGORY_TABLE, null, null);
		db.delete(FLORA_TABLE, null, null);
		db.delete(FAUNA_TABLE, null, null);
		db.close();
	}
}
