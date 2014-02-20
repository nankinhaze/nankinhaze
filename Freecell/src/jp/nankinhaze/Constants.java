/***
 * Excerpted from "Hello, Android! 3e",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package jp.nankinhaze;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	public static final String TABLE_NAME = "gamerecords";

   // Columns in the Events database
   
   public static final String USER = "user";
   public static final String TIME = "time";
   public static final String GAMENUMBER = "gamenumber";
   public static final String NUMBEROFMOVES = "numberofmoves";
   public static final String GAMERECORD = "gamerecord";
}
