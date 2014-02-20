package jp.nankinhaze;

import jp.nankinhaze.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {

		private static final String OPT_AUTOPLAY = "autoplay";
		private static final boolean OPT_AUTOPLAY_DEF = true;
		private static final String OPT_BIGIMAGE = "bigimage";
		private static final boolean OPT_BIGIMAGE_DEF = true;
		private static final String OPT_ONEHAND = "onehand";
		private static final boolean OPT_ONEHAND_DEF = false;
		private static final String OPT_RECORD = "record";
		private static final boolean OPT_RECORD_DEF = false;

		@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      addPreferencesFromResource(R.xml.settings);
	   }
		
	   public static boolean getAutoplay(Context context) {
	      return PreferenceManager.getDefaultSharedPreferences(context)
	            .getBoolean(OPT_AUTOPLAY, OPT_AUTOPLAY_DEF);
	   }
	   
	   public static boolean getBigimage(Context context) {
		      return PreferenceManager.getDefaultSharedPreferences(context)
		            .getBoolean(OPT_BIGIMAGE, OPT_BIGIMAGE_DEF);
	   }
	   
	   public static String getDominanthand(Context context) {
		      return PreferenceManager.getDefaultSharedPreferences(context)
		            .getString("dominant_hand", "right");
	   }

	   public static boolean getOnehand(Context context) {
		      return PreferenceManager.getDefaultSharedPreferences(context)
		            .getBoolean(OPT_ONEHAND, OPT_ONEHAND_DEF);
	   }
	   	   
	   public static boolean getRecord(Context context) {
		      return PreferenceManager.getDefaultSharedPreferences(context)
		            .getBoolean(OPT_RECORD, OPT_RECORD_DEF);
	   }

}
