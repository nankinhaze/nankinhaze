package jp.nankinhaze;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelXorXfermode;
import android.graphics.Rect;
import android.util.Log;

public class Card {
	private static final String TAG = "Card"; 

	public int suit;
	private final int CLUB = 1;
	private final int DIAMOND = 2;
	private final int HEART = 3;
	private final int SPADE = 4;

	public int color;
	private final int RED = 1;
	private final int BLACK = 2;

	public int number; // 1-13:A-K
	public boolean faceup = true;
	public boolean inv = false;
	public float posx = 0;
	public float posy = 0;
	public boolean select = false;
	
	Bitmap FacePicture;
	Bitmap BackPicture;
//	Bitmap InvFacePicture;
//	private Paint paint = new Paint(); 
	private Paint fill_paint = new Paint();
	
	public Card(int s, int n, Bitmap fbitmap, Bitmap bbitmap) {
		suit = s;
		number = n;
		if (suit == DIAMOND || suit == HEART)
		    color = RED;
		else
			color = BLACK;
		FacePicture = fbitmap;
		BackPicture = bbitmap;
//		InvFacePicture = fbitmap;
//		paint.setXfermode(new PixelXorXfermode(0xFFFFFF)); 

		fill_paint.setStyle(Paint.Style.STROKE);
		fill_paint.setStrokeWidth(2);
		fill_paint.setColor(Color.WHITE);
	
	}
	
	public void paint(Canvas cv) {
		if (faceup)
			if (! inv)
				cv.drawBitmap(FacePicture, posx, posy, null);
			else
				cv.drawBitmap(FacePicture, posx, posy, null);
		else
			cv.drawBitmap(FacePicture, posx, posy, null);
		if (select) {
			cv.drawRect(posx - 1, posy - 1, posx + FacePicture.getWidth() + 1, posy + FacePicture.getHeight() + 1, fill_paint);			
		}
	}
	
	public void paint(Canvas cv, float x, float y) {

		
		cv.drawBitmap(FacePicture, x, y, null);

		//ïKóvÇ©ÅH
		
		cv.drawRect(x - 1, y - 1, x + FacePicture.getWidth() + 1, y + FacePicture.getHeight() + 1, fill_paint);			

	
	}

	public void bigPaint(Canvas cv, float f) {
		int w = FacePicture.getWidth();
		int h = FacePicture.getHeight();
		Rect src = new Rect(0, 0, w, h);
		Rect dst = new Rect((int)posx - (int)(0.2 * w) + (int)f, (int)posy - (int)(0.2 * h), (int)posx + (int)(1.2 * w) + (int)f, (int)posy + (int)(1.2 * h));
        cv.drawBitmap(FacePicture, src, dst, null);		
	}
	
	public boolean isArea(float x, float y) {
		return  ( ((posx < x) & (x < posx + FacePicture.getWidth())) &
				((posy < y) & (y <posy + FacePicture.getHeight())));
	}
}


