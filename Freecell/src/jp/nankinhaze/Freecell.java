// git test
package jp.nankinhaze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import jp.nankinhaze.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
//import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import com.loopj.android.http.*;
import android.text.InputType;
import static jp.nankinhaze.Constants.TABLE_NAME;
import static android.provider.BaseColumns._ID;
import static jp.nankinhaze.Constants.TIME;
import static jp.nankinhaze.Constants.USER;
import static jp.nankinhaze.Constants.GAMENUMBER;
import static jp.nankinhaze.Constants.NUMBEROFMOVES;
import static jp.nankinhaze.Constants.GAMERECORD;

public class Freecell extends Activity {
	private static final String TAG = "Freecell"; 
	private String path = "freecell.txt";
	Activity activity;
	MySurfaceView myview;
    private static boolean cont = false;
    private long seed;
    static final int FREECELL_CODE = 0;
    
    private RecordData records;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String[] sp = new String[16];
		BufferedReader br = null;
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		activity = this;
		setContentView(R.layout.main);
		myview = (MySurfaceView)this.findViewById(R.id.myview1);
		// ここで保存データを取得する。
		if (!cont) {  // 継続でなければ
			myview.setseed((long)(Math.random()*1000000 + 1));
			myview.initpile(); // Pileを初期化
			cont = true;
		}
		else { // 継続ならば
			try {
				Log.d(TAG, "readfile");
				br = new BufferedReader(new InputStreamReader(this.openFileInput(path)));			
				seed = Long.parseLong(br.readLine());
				myview.setseed(seed);				
				for (int np = 0; np < 16; np++) {
	    			sp[np] = br.readLine();
	    			Log.d(TAG, "read:" + sp[np]);		
	    		}
				myview.recordIndex = Integer.parseInt(br.readLine());
				int nor = Integer.parseInt(br.readLine());
				myview.record.clear();
	    		for (int i = 0; i < nor; i++) {
	    			myview.record.add(br.readLine());
	    		}
			} catch (Exception e) {
				Log.d(TAG, "readfile_exception");
			} finally {
				try {
					br.close();
				}
				catch  (Exception e) {		
					Log.d(TAG, "readfile_close_exception");
				}
			}
			myview.setpile(sp);			
		}
	}

	@Override
	protected void onResume() {
	    super.onResume();
		Log.d(TAG, "onResume");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		// ここで保存する。	
		BufferedWriter bw = null;
		try {
			Log.d(TAG, "writefile");
    		bw = new BufferedWriter(new OutputStreamWriter(this.openFileOutput(path, Context.MODE_PRIVATE)));
    		PrintWriter pw = new PrintWriter(bw); 		
    		seed = myview.getseed();
			pw.println(Long.toString(seed));
			Log.d(TAG, "write:" + Long.toString(seed));			
    		for (int np = 0; np < 16; np++) {
    			int nc = myview.piles[np].getNumberOfCard();
    			String str = "";
    			for (int i = 0; i < nc; i++) {
    				int s = (myview.piles[np].getCard(i)).suit;
    				int n = (myview.piles[np].getCard(i)).number;
    				str = str + Integer.toString(s * 100 + n);
    				if (myview.piles[np].getCard(i).select) {
        				str = str + "1";   					
    				} else {
        				str = str + "0";		
    				}
    				str = str + "/";
    			}
    			pw.println(str);
    			Log.d(TAG, "write:" + str);	
    		}
    		int nor = myview.record.size();
    		pw.println(Integer.toString(myview.recordIndex));
			pw.println(Integer.toString(nor));
    		for (int i = 0; i < nor; i++) {
    			pw.println((String)myview.record.get(i));
    		}
		} catch (Exception e) {
			Log.d(TAG, "writefile_exception");
		} finally {
			try {
				bw.close();
			}
			catch  (Exception e) {		
			}
		}
	}
	
	public void doAction(View view) {
        final EditText editView = new EditText(Freecell.this);
        editView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        editView.setText(Long.toString(myview.getseed()));
        new AlertDialog.Builder(Freecell.this).setIcon(android.R.drawable.ic_dialog_info)
        .setTitle(getString(R.string.setup_game))
        //setViewにてビューを設定します。
        .setView(editView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int whichButton) {
            	 long seed;
            		 seed = Long.parseLong(editView.getText().toString());
            		 Log.d(TAG, "seed:" + Long.toString(seed));			
         			             		 
            		 if ((0 < seed && seed <= 1000000) || seed == -1 || seed == -2) { 
            		 	 myview.setseed(seed);
            		 	 myview.initpile();
            		 	 myview.initgame();
            		 	 myview.drawSurface();
                	 }
             }
        })
        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        })
        .show();
	}
	
	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      super.onCreateOptionsMenu(menu);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.menu, menu);
	      return true;
	   }

	   @Override
	   public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.settings:
	    	  startActivity(new Intent(this, Prefs.class));
	    	  return true;
	      // More items go here (if any) ...
	      }
	      return false;
	   }

	   public void setting(View view) {
	    	  startActivity(new Intent(this, Prefs.class));
	   }

	   public void newGame(View view) {
			myview.setseed((long)(Math.random()*1000000));
			myview.initpile(); // Pileを初期化
			myview.initgame();
			myview.drawSurface();
	   }

	   public void selectGame(View view) {
		   doAction(myview);
	   }

	   public void redo(View view) {
		   myview.redo();
	   }

	   // undoを押した時
	   public void undo(View view) {
		   myview.undo();
	   }

	   // Histryを押した時　ヒストリー一覧画面を呼び出す
	   public void startHistory(View view) {
		   Intent intent = new Intent(this, jp.nankinhaze.Records.class);
		   this.startActivityForResult(intent, FREECELL_CODE);
	   }
	   
	   // ヒストリ一覧から戻った時
	   @Override
	   protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   records = new RecordData(this);
		   SQLiteDatabase db = records.getWritableDatabase();
		   String id;
		   super.onActivityResult(requestCode, resultCode, intent);
		   if (requestCode == FREECELL_CODE) {
			   if (resultCode == Activity.RESULT_OK) {
				   try {
					   id = (String)intent.getExtras().getCharSequence(Intent.EXTRA_TEXT);
				   } catch (NullPointerException e) {
					   id = e.getMessage();
				   }
			        Cursor c = db.query(TABLE_NAME, new String[] { GAMENUMBER, NUMBEROFMOVES, GAMERECORD },
			        		 "_id == " + id , null, null, null, null);
			        boolean isnotEof = c.moveToFirst();
			        if (isnotEof) {
			           	int gamenumber = c.getInt(0);
			           	int numberofmoves = c.getInt(1);
		        		String gamerecord = c.getString(2);
//		        		Toast toast = Toast.makeText(this, String.valueOf(gamenumber), Toast.LENGTH_SHORT);
//						toast.show();
		    			myview.setseed(gamenumber);
           		 	 	myview.initpile();
           		 	 	myview.initgame();
          		 	 	String[] rcd = gamerecord.split("\n");
        	    		for (int i = 0; i < numberofmoves; i++) {
        	    			myview.record.add(rcd[i]);
        	    		}
           		 	 	redoSetEnabled(true);
			        }
			        c.close();
			        db.close();
			   } else {
//				   Toast toast = Toast.makeText(this, "no result...", Toast.LENGTH_SHORT);
//				   toast.show();
			   }
		   }
	   }
	   
	   public void redoSetEnabled(boolean enabled) {
           Button btn5 = (Button)Freecell.this.findViewById(R.id.button5);  
		   if (enabled)
			   btn5.setEnabled(true);
		   else
			   btn5.setEnabled(false);
	   }

	   public void undoSetEnabled(boolean enabled) {
           Button btn1 = (Button)Freecell.this.findViewById(R.id.button1);  
		   if (enabled)
			   btn1.setEnabled(true);
		   else
			   btn1.setEnabled(false);
	   }
	   
	   final AsyncHttpClient client = new AsyncHttpClient();
	   
	   void test() {
	   Log.d(TAG, "test");
//　ファイルのダウンロード	   
	   client.get("http://monolio.com/dev/csv/download2.php", new AsyncHttpResponseHandler() {
	       @Override
	       public void onSuccess(String response) {
	   // ここに通信が成功したときの処理をかく
	    	   Log.d(TAG, "onSuccess");
	    	   System.out.println(response);
	    	   System.out.println("onSuccess");
	       }
	      @Override
	       public void onFailure(Throwable error, String response) {
	    	  // ここに通信が失敗したときの処理をかく
	   	   Log.d(TAG, "onFailure");
	   	   System.out.println("onFailure");
	       }
	   });

	   
// ファイルのアップロード	   
/*
	   RequestParams params = new RequestParams();
	   try {
	   		params.put("upfile", openFileInput(path), path); // Upload a File
	   } catch(Exception e) {
    	   Log.d(TAG, "Exception");
	   }
	   
   AsyncHttpClient client = new AsyncHttpClient();
	   client.post("http://192.168.1.5/test/upload.php", params, new AsyncHttpResponseHandler() {
	       @Override
	       public void onSuccess(String response) {
	    	   Log.d(TAG, "onSuccess");
	    	   System.out.println(response);
	       }
	      @Override
	       public void onFailure(Throwable error, String response) {
	   	   Log.d(TAG, "onFailure");
		   System.out.println(response);
	       }
	   });
*/
	   }
}
