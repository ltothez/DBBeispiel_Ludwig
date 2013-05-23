package de.perna.dbhelperdemo;

import java.io.File;

import de.perna.dbhelperdemo.DBUtil;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DBHelper {
	private static final String TAG = "DBHelper";
	// DB constants
	public static final String  DB_FILE_PATH	= "/sdcard/";
	private static final String DB_NAME			= "demo_db";
	//private static final int DB_VERSION			= 1; // Schema version

	// Table A Schema constants
	private static final String TABLE_NAME_A = "table_a";
	private static final String TABLE_A_COL_1 = "col1";
	private static final String TABLE_A_COL_2 = "col2";
	private static final String TABLE_A_COL_TIMESTAMP = "timestamp";
	private static final String TABLE_A_CREATE = "create table " + TABLE_NAME_A
											+ " (_id integer not null primary key autoincrement, "
											+ TABLE_A_COL_1 + " INT(5), " 
											+ TABLE_A_COL_2 + " TEXT, "
											+ TABLE_A_COL_TIMESTAMP + " INT )";

	// Table A Schema constants
	private static final String TABLE_NAME_B = "table_b";
	private static final String TABLE_B_COL_1 = "col1";
	private static final String TABLE_B_COL_2 = "col2";
	private static final String TABLE_B_COL_TIMESTAMP = "timestamp";
	private static final String TABLE_B_CREATE = "create table " + TABLE_NAME_B
											+ " (_id integer not null primary key autoincrement, "
											+ TABLE_B_COL_1 + " INT(5), " 
											+ TABLE_B_COL_2 + " TEXT, "
											+ TABLE_B_COL_TIMESTAMP + " INT )";

	private SQLiteDatabase database; 

	public DBHelper() {
		try {
			database = SQLiteDatabase.openDatabase(DB_FILE_PATH + File.separator + DB_NAME, null,SQLiteDatabase.OPEN_READWRITE);
		}
		catch (SQLiteException ex) {
			Log.e(TAG, "error -- " + ex.getMessage(), ex);
			// error means tables does not exits
			database = SQLiteDatabase.openDatabase(DB_FILE_PATH + File.separator + DB_NAME, null,SQLiteDatabase.CREATE_IF_NECESSARY);
			createTables();
		}
		finally {
			DBUtil.safeCloseDataBase(database);
		}
	}

	private void createTables() {
		database.execSQL(TABLE_A_CREATE);
		database.execSQL(TABLE_B_CREATE);
	}

	public void close() {
		DBUtil.safeCloseDataBase(database);
	}

	public SQLiteDatabase getWritableDatabase() {
		database = SQLiteDatabase.openDatabase(DB_FILE_PATH + File.separator + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
		return database;
	}
	
	/** Clears a table */
	public static void clearDB(SQLiteDatabase db, String tablename) {
		db.execSQL("DELETE FROM " + tablename + " WHERE timestamp > 0");
		Log.d("clearDB", tablename + "-DB cleared");
	}

	/** Load TABLE_A Data */
	public static String loadTblA(SQLiteDatabase db) {
		long duration = System.currentTimeMillis();
		String tableData = new String();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_A, null);
			c.moveToFirst();
			// Check if our result was valid.
			if (c.getCount() != 0) {
				int Column1 = c.getColumnIndex("col1");
				int Column2 = c.getColumnIndex("col2");
				int Column3 = c.getColumnIndex("timestamp");

				tableData = "";

				// Loop through all Results
				do {
					int col1 = c.getInt(Column1);
					String col2 = c.getString(Column2);
					double TIME = c.getDouble(Column3);

					tableData = tableData + "COL_1: " + col1 + "\n" 
							+ "COL_2: " + col2 + "\n" + TIME + "\n\n";

				} while (c.moveToNext());
				DBUtil.safeCloseCursor(c);
			} else {
				tableData = "No Data";
				DBUtil.safeCloseCursor(c);
			}
		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			//
		}

		Log.d("Loading", "TABLE_A loaded");
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
		return tableData;
	}

	/** Load TABLE_B Data */
	public static String loadTblB(SQLiteDatabase db) {
		long duration = System.currentTimeMillis();
		String tableData = new String();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_B, null);
			c.moveToFirst();
			// Check if our result was valid.
			if (c.getCount() != 0) {
				int Column1 = c.getColumnIndex("col1");
				int Column2 = c.getColumnIndex("col2");
				int Column3 = c.getColumnIndex("timestamp");

				tableData = "";

				// Loop through all Results
				do {
					int col1 = c.getInt(Column1);
					String col2 = c.getString(Column2);
					double TIME = c.getDouble(Column3);

					tableData = tableData + "COL_1: " + col1 + "\n" 
							+ "COL_2: " + col2 + "\n" + TIME + "\n\n";

				} while (c.moveToNext());
				DBUtil.safeCloseCursor(c);
			} else {
				tableData = "No Data";
				DBUtil.safeCloseCursor(c);
			}
		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			//
		}

		Log.d("Loading", "TABLE_B loaded");
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
		return tableData;
	}

	/** Add Item TABLE_A */
	public static void addTblAItem(SQLiteDatabase db, int col1, String col2) {
		long duration = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(TABLE_A_COL_1, col1);
		values.put(TABLE_A_COL_2, col2);
		values.put(TABLE_A_COL_TIMESTAMP, System.currentTimeMillis());
		db.insert(TABLE_NAME_A, null, values);  // 
		Log.d("TABLE_A", "inserted values=" + values);
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
	}

	/** Add Item TABLE_B */
	public static void addTblBItem(SQLiteDatabase db, int spalte_1, String spalte_2) {
		long duration = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(TABLE_B_COL_1, spalte_1);
		values.put(TABLE_B_COL_2, spalte_2);
		values.put(TABLE_B_COL_TIMESTAMP, System.currentTimeMillis());
		db.insert(TABLE_NAME_B, null, values);
		Log.d("TABLE_B", "inserted values=" + values);
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
	}

	/** Return ID matching searchstring */
	public static int[] search(SQLiteDatabase db, String searchstring) {
		long duration = System.currentTimeMillis();
		int[] result = new int[2];
		String where = TABLE_A_COL_2 + " = '" + searchstring + "'";
		Log.d("SQL-WHERE", where);
		Cursor c = db.query(TABLE_NAME_A, new String[] {"_id", TABLE_A_COL_2}, where, null, null, null, null);
		c.moveToFirst();
		// Check if our result was valid.
		if (c.getCount() != 0) {
			result[0] = c.getInt(c.getColumnIndex("_id"));
			result[1] = c.getInt(c.getColumnIndex("col2"));
		}
		DBUtil.safeCloseCursor(c);
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
		return result;
	}

	/** Update Item in Table B */
	public static void updateTblB(SQLiteDatabase db, int id, int col1, String col2) {
		long duration = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(TABLE_B_COL_1, col1);
		values.put(TABLE_B_COL_2, col2);
		values.put(TABLE_B_COL_TIMESTAMP, System.currentTimeMillis());
		db.update(TABLE_NAME_B, values, "_id=" + id, null);
		Log.d("UPDATE-VALUES", values.toString());
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
	}
	
	/** Get TABLE_A Count */
	public static void count_TblA(SQLiteDatabase db) {
		long duration = System.currentTimeMillis();
		try {
			Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_A, null);
			c.moveToFirst();
			// Check if our result was valid.
			if (c.getCount() != 0) {
				Log.d("Items",""+c.getCount());
				DBUtil.safeCloseCursor(c);
			} else {
				DBUtil.safeCloseCursor(c);
			}
		} catch (Exception e) {
			Log.e("Error", "Error", e);
		} finally {
			//
		}
		Log.d("Querytime", ""+(System.currentTimeMillis() - duration));
	}
	
	
}