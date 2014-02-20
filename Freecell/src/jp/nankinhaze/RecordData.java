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
import static jp.nankinhaze.Constants.TABLE_NAME;
import static jp.nankinhaze.Constants.TIME;
import static jp.nankinhaze.Constants.USER;
import static jp.nankinhaze.Constants.GAMENUMBER;
import static jp.nankinhaze.Constants.NUMBEROFMOVES;
import static jp.nankinhaze.Constants.GAMERECORD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordData extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "records.db";
   private static final int DATABASE_VERSION = 1;

   /** Create a helper object for the Events database */
   public RecordData(Context ctx) {
      super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
    		  + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
    		  + GAMENUMBER + " INTEGER NOT NULL UNIQUE, " 
    		  + TIME + " TEXT NOT NULL," 
    		  + USER + " TEXT NOT NULL,"
    		  + NUMBEROFMOVES + " INTEGER NOT NULL,"
    		  + GAMERECORD + " TEXT NOT NULL"
    		  + ");");
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion,
         int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      onCreate(db);
   }
}