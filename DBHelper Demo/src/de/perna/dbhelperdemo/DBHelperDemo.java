package de.perna.dbhelperdemo;

import de.perna.dbhelperdemo.DBHelper;

import android.app.TabActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

public class DBHelperDemo extends TabActivity {
	/** Called when the activity is first created. */
	TabHost mTabHost;
	Button btn1, btn2, btn3, btn4;
	TextView tv1, tv2;
	DBHelper dbHelper;
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("TABLE_A")
				.setContent(R.id.textview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("TABLE_B")
				.setContent(R.id.textview2));

		mTabHost.setCurrentTab(0);

		tv1 = (TextView) findViewById(R.id.textview1);
		tv2 = (TextView) findViewById(R.id.textview2);
		tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
		tv2.setMovementMethod(ScrollingMovementMethod.getInstance());

		// Initialize the database
		dbHelper = new DBHelper();
		db = dbHelper.getWritableDatabase();
		
		// Load the Database
		String tabledata = new String();
		tabledata = DBHelper.loadTblA(db);
		tv1.setText(tabledata);
		tabledata = DBHelper.loadTblB(db);
		tv2.setText(tabledata);

		/* Buttons */
		
		// Load Database
		final Button button1 = (Button) findViewById(R.id.Button01);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String tabledata = new String();
				tabledata = DBHelper.loadTblA(db);
				tv1.setText(tabledata);
				DBHelper.count_TblA(db); // Gesamtzahl CLF-Datensaetze
				tabledata = DBHelper.loadTblB(db);
				tv2.setText(tabledata);
			}
		});

		// Add TABLE_A Item
		final Button button2 = (Button) findViewById(R.id.Button02);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Store in DB
				DBHelper.addTblAItem(db, 26203,"4560");
				String tabledata = new String();
				tabledata = DBHelper.loadTblA(db);
				tv1.setText(tabledata);
			}
		});

		// Add TABLE_B Item
		final Button button3 = (Button) findViewById(R.id.Button03);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Store in DB
				DBHelper.addTblBItem(db, 26203,"4560");
				String tabledata = new String();
				tabledata = DBHelper.loadTblB(db);
				tv2.setText(tabledata);
			}
		});

		// Clear Database
		final Button button4 = (Button) findViewById(R.id.Button04);
		button4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Clear DB
				DBHelper.clearDB(db,"table_a"); // private static final String TABLE_NAME_A
				DBHelper.clearDB(db,"table_b"); // private static final String TABLE_NAME_B
				tv1.setText("No Data");
				tv2.setText("No Data");
			}
		});
		
		// Check if Item is present -> 0 = not present, >0 = id of Item
		final Button button5 = (Button) findViewById(R.id.Button05);
		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int[] result = new int[2];
				result = DBHelper.search(db, "4560"); // BSSID
				Log.d("Search","ID is: " + result[0]);
				Log.d("Search","COL2 is: " + result[1]);
			}
		});
		
		// Update Item
		final Button button6 = (Button) findViewById(R.id.Button06);
		button6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DBHelper.updateTblB(db, 1, 365, "2651");
				String tabledata = new String();
				tabledata = DBHelper.loadTblB(db);
				tv2.setText(tabledata);
			}
		});
		
		/* END Buttons */
	}
}