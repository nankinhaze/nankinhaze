package jp.nankinhaze;

import java.util.Vector;

import android.graphics.Canvas;
import android.util.Log;
public class Hand {
	private static final String TAG = "Hand"; 
	Vector cards = new Vector();
	float offset = 48;
	float posx = 0;
	float posy = 0;

	public Hand() {
	}

	public void placeCard(Card c) {
		cards.addElement(c);
	}

	public int getNumberOfCard() {
		return cards.size();
	}

	public void clearCard() {
		cards.clear();
	}

	public void setPos(float x, float y) {
		posx = x;
		posy = y;
	}

	public void setOffset(float o) {
		offset = o;
	}

	public void paint(Canvas cv) {
		for (int i = 0; i < cards.size(); i++) {
			((Card)cards.elementAt(i)).paint(cv, posx,  posy + offset * i);
		}
	}
}
