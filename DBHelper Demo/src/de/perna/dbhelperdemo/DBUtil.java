package de.perna.dbhelperdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author preetam.palwe
 * 
 */
public class DBUtil {
	private static final String TAG = "DBUtil";

	public static void safeCloseCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	public static void safeCloseDataBase(SQLiteDatabase database) {
		if (database != null) {
			database.close();
		}
	}
}