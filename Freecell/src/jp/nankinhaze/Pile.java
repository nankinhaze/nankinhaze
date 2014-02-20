package jp.nankinhaze;

import java.util.Vector;
import android.graphics.Canvas;
import android.util.Log;

public class Pile {
	private static final String TAG = "Pile"; 
	Vector cards = new Vector();
	public float offset = 0;
	public float posx = 0;
	public float posy = 0;
	public float areawidth = 0;
	public float areaheight = 0;
	boolean select = false;
	public int type = 0;
	int selcard;
	public boolean moving = false;
	
	public Pile() {
	}

	public void placeCard(Card c) {
		cards.addElement(c);
		rePos();
	}

	public int getNumberOfCard() {
		return cards.size();
	}

	public Card getCard() {
		return (Card)cards.lastElement();
	}

	public Card getCard(int n) {
		return (Card)cards.elementAt(n);
	}

	public Card pickCard() {
		Card c = (Card)cards.lastElement();
		cards.removeElementAt(cards.size() - 1);
		return c;
	}
	public void setSelect(int s) {
		if (s >= 0) {
			if (! cards.isEmpty()) {
				select = true;
				((Card)cards.elementAt(s)).select = true;		
				Log.d(TAG, "setSelect true");
			}
		} else {
			select = false;
			if (! cards.isEmpty()) {
				for (int i = 0; i < cards.size(); i++) {
					((Card)cards.elementAt(i)).select = false;
				}
				Log.d(TAG, "setSelect false");
			}
		}
	}

	public boolean isSelect() {
		return select;
	}

	public int isArea(float x, float y) {
		int i;
		selcard = -1;
		if (cards.size() == 0) {
			if( ((posx < x) & (x < posx + areawidth)) &
					((posy < y) & (y <posy + areaheight))) {
				selcard = 0;
			}
		} else {
			for (i = 0; i < cards.size(); i++) {
				if (((Card)cards.elementAt(i)).isArea(x, y)) {
					selcard = i;
				}
			}
		}
		return selcard;
	}

	public void rePos() {
		float x = posx;
		float y = posy;
		for (int i = 0; i < cards.size(); i++) {
			((Card)cards.elementAt(i)).posx = x;
			((Card)cards.elementAt(i)).posy = y;
			y = y + offset;
		}
	}

	public void paint(Canvas cv) {
		for (int i = 0; i < cards.size(); i++) {
			if (moving) {
				if (!((Card)cards.elementAt(i)).select) {
					((Card)cards.elementAt(i)).paint(cv);			
				} else {
					
				}
				
			} else {
				((Card)cards.elementAt(i)).paint(cv);			
			}
		}
	}
}
