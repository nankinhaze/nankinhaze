/***
 * Excerpted from "Hello, Android! 3e",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package jp.nankinhaze;

import static android.provider.BaseColumns._ID;

//import java.text.SimpleDateFormat;
//import java.util.Date;

import static jp.nankinhaze.Constants.TABLE_NAME;
import static jp.nankinhaze.Constants.TIME;
import static jp.nankinhaze.Constants.USER;
import static jp.nankinhaze.Constants.GAMENUMBER;
import static jp.nankinhaze.Constants.NUMBEROFMOVES;
import static jp.nankinhaze.Constants.GAMERECORD;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
// ...
//import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class Records extends ListActivity {
   // ...
   private static String[] FROM = { _ID, GAMENUMBER, TIME, NUMBEROFMOVES, };
   private static String ORDER_BY = GAMENUMBER + " ASC";
   private static int[] TO = { R.id.rowid, R.id.gamenumber, R.id.time, R.id.numberofmoves, };
   private RecordData records;
   private int selectPosition = -1;
   private CharSequence selectId;
   private View view;
   SQLiteDatabase db;
   SimpleCursorAdapter adapter;
   Cursor cursor;
   private int gd = 0, dd = 0, md = 0, order = 0;
   private int item = 0;
   private String[] orderByItem = {"GAMENUMBER ", "TIME ", "NUMBEROFMOVES "};
   private String[] orderByDirection = {"ASC", "DESC"};
   private CharSequence gameNo;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main1);
      records = new RecordData(this);
      try {
         cursor = getRecords();
         showEvents(cursor);
      } finally {
         records.close();
      }
   }

   private Cursor getRecords() {
      // Perform a managed query. The Activity will handle closing
      // and re-querying the cursor when needed.
      db = records.getReadableDatabase();
//      Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
      Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, orderByItem[item] + orderByDirection[order]);
      startManagingCursor(cursor);
      return cursor;
   }

   private void showEvents(Cursor cursor) {
      // Set up data binding
      adapter = new SimpleCursorAdapter(this,
            R.layout.item1, cursor, FROM, TO);
      setListAdapter(adapter);
   }

   public void gameNo(View view) {
	   if (gd == 0) {
		   gd = 1;
	   } else {
		   gd = 0;
	   }
	   item = 0;
	   order = gd;
	   try {
		   cursor = getRecords();
		   adapter.changeCursor(cursor);
	   } finally {
	       records.close();
	   }
   }

   public void date(View view) {
	   if (dd == 0) {
		   dd = 1;
	   } else {
		   dd = 0;
	   }
	   item = 1;
	   order = dd;
	   try {
		   cursor = getRecords();
		   adapter.changeCursor(cursor);
	   } finally {
	       records.close();
	   }
   }

   public void moves(View view) {
	   if (md == 0) {
		   md = 1;
	   } else {
		   md = 0;
	   }
	   item = 2;
	   order = md;
	   try {
		   cursor = getRecords();
		   adapter.changeCursor(cursor);
	   } finally {
	       records.close();
	   }
   }

   
   
   public void delete(View v) {

		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.delete_game_no) + gameNo)
		.setNegativeButton(getString(R.string.delete_label), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {


				
				   //	   Log.d("records ", "delete1 ");
				   if (selectPosition >= 0) {
					   db = records.getReadableDatabase();
			Log.d("records ", "delete3 " + ", _ID = " + selectId);
					   db.delete(TABLE_NAME, "_ID = " + selectId, null);
					      try {
					         cursor = getRecords();
					         adapter.changeCursor(cursor);
					       } finally {
					          records.close();
					       }
				   }
				
				
				
				
				
			}
		})
		.setPositiveButton(getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		   }
		})
		.show();
	   
	   
	   
	   
	   
	   
	   
	   
	   
/*	   
	   //	   Log.d("records ", "delete1 ");
	   if (selectPosition >= 0) {
		   db = records.getReadableDatabase();
Log.d("records ", "delete3 " + ", _ID = " + selectId);
		   db.delete(TABLE_NAME, "_ID = " + selectId, null);
		      try {
		         cursor = getRecords();
		         adapter.changeCursor(cursor);
		       } finally {
		          records.close();
		       }
	   }
*/
   }
   
   public void load(View v) {
	   if (selectPosition >= 0) {
//		   TextView text = (TextView)view.findViewById(R.id.rowid);
		   Intent returnIntent = new Intent();
		   returnIntent.putExtra(Intent.EXTRA_TEXT, selectId);
		   this.setResult(Activity.RESULT_OK, returnIntent);
		   this.finish();
	   }
   }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TextView text = (TextView)v.findViewById(R.id.rowid);

		selectId = text.getText();
		gameNo = ((TextView)v.findViewById(R.id.gamenumber)).getText();
		selectPosition = position;
		view = v;
		
		super.onListItemClick(l, v, position, id);

		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.game_no1) + gameNo)
		.setNegativeButton(getString(R.string.load_label), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				load(view);
			}
		})
		.setNeutralButton(getString(R.string.delete_label), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				delete(view);
			}
		})
		.setPositiveButton(getString(R.string.cancel_label), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		   }
		})
		.show();
	}
}
