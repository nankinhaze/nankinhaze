package jp.nankinhaze;

import java.util.Vector;

import jp.nankinhaze.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/////////// トランプの一組
public class Pack {
	private static final String TAG = "Pack"; 
	Bitmap[][] bitmap = new Bitmap[5][14];
	Bitmap bbitmap;
	long x=1;
	Vector cards = new Vector();
	public Pack(Context context) {
		int suit;
		int number;
		BitmapFactory.Options opt = new BitmapFactory.Options(); 
		opt.inScaled = false; //bitmapのリサイズをなしとする。
		
 		bitmap[1][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c1, opt);
		bitmap[1][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c2, opt);
		bitmap[1][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c3, opt);
		bitmap[1][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c4, opt);
		bitmap[1][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c5, opt);
		bitmap[1][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c6, opt);
		bitmap[1][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c7, opt);
		bitmap[1][8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c8, opt);
		bitmap[1][9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c9, opt);
		bitmap[1][10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c10, opt);
		bitmap[1][11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c11, opt);
		bitmap[1][12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c12, opt);
		bitmap[1][13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.c13, opt);
		bitmap[2][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d1, opt);
		bitmap[2][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d2, opt);
		bitmap[2][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d3, opt);
		bitmap[2][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d4, opt);
		bitmap[2][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d5, opt);
		bitmap[2][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d6, opt);
		bitmap[2][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d7, opt);
		bitmap[2][8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d8, opt);
		bitmap[2][9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d9, opt);
		bitmap[2][10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d10, opt);
		bitmap[2][11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d11, opt);
		bitmap[2][12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d12, opt);
		bitmap[2][13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.d13, opt);
		bitmap[3][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h1, opt);
		bitmap[3][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h2, opt);
		bitmap[3][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h3, opt);
		bitmap[3][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h4, opt);
		bitmap[3][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h5, opt);
		bitmap[3][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h6, opt);
		bitmap[3][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h7, opt);
		bitmap[3][8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h8, opt);
		bitmap[3][9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h9, opt);
		bitmap[3][10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h10, opt);
		bitmap[3][11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h11, opt);
		bitmap[3][12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h12, opt);
		bitmap[3][13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.h13, opt);
		bitmap[4][1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s1, opt);
		bitmap[4][2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s2, opt);
		bitmap[4][3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s3, opt);
		bitmap[4][4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s4, opt);
		bitmap[4][5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s5, opt);
		bitmap[4][6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s6, opt);
		bitmap[4][7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s7, opt);
		bitmap[4][8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s8, opt);
		bitmap[4][9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s9, opt);
		bitmap[4][10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s10, opt);
		bitmap[4][11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s11, opt);
		bitmap[4][12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s12, opt);
		bitmap[4][13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.s13, opt);
		
		bbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.back, opt);

		for (number = 1; number < 14 ;number++) {
			for (suit = 1; suit < 5; suit++) {
				cards.addElement(new Card(suit, number, bitmap[suit][number], bbitmap));
			}
		}
	}

    private int rand() {
        x = x * 214013 + 2531011;
        return (int)(x >> 16) & 32767;
    }

    void srand(long s) {
        x = s;
    }
	
	public void shuffle(long seed) {
		int i, j;
		Card cc;
		int left;
		String[] pd;
		String[] m;
		Vector temp = new Vector();
		if (seed == -1 || seed == -2) {
			if (seed == -1) {
				String[] m1 = { 			
					"1010/2010/3010/4010/1120/2120/3120/4120",
					"1030/2030/3030/4030/1100/2100/3100/4100",
					"1050/2050/3050/4050/1080/2080/3080/4080",
					"1070/2070/3070/4070/1060/2060/3060/4060",
					"1090/2090/3090/4090/1040/2040/3040/4040",
					"1110/2110/3110/4110/1020/2020/3020/4020",
					"1130/2130/3130/4130"
				};
				m = m1;
			} else {
				String[] m2 = { 			
					"4010/3010/2010/1010/4070/3070/2070/1070",
					"4130/3130/2130/1130/4060/3060/2060/1060",
					"4120/3120/2120/1120/4050/3050/2050/1050",
					"4110/3110/2110/1110/4040/3040/2040/1040",
					"4100/3100/2100/1100/4030/3030/2030/1030",
					"4090/3090/2090/1090/4020/3020/2020/1020",
					"4080/3080/2080/1080"
				};
				m = m2;
			}
			for ( i = 0; i < 7; i++) {
				pd = m[i].split("/");
			    for (j=0; j < pd.length; j++) {
				    if (pd[j].length() == 4) {
				    	int s = Integer.parseInt(pd[j].substring(0,1));
				    	int n = Integer.parseInt(pd[j].substring(1,3));
				    	temp.addElement(pickCard(s, n));
				    }
//					Log.d(TAG, "shuffle loop :");			
			    }
			 }
		} else {
			srand(seed);
			left = cards.size();
			for (i = 0; i < 52; i++) {
				j = rand() % left;
				temp.addElement(cards.elementAt(j));
				left--;
				cards.setElementAt(cards.elementAt(left), j);
				cards.removeElementAt(left);
			}
		}
		Log.d(TAG, "shuffle 1:");			
		for (i = 0; i < temp.size(); i++) {
			cards.addElement(temp.elementAt(i));
		}
		Log.d(TAG, "shuffle 2:");			
	}
	
	public Card pickCard() {
		Card c = (Card)cards.firstElement();
		cards.removeElementAt(0);
		return c;
	}

	public Card pickCard(int s, int n) {
		Card c = null;
		int i;
		for (i = 0; i < cards.size(); i++) {
			if (((Card)cards.elementAt(i)).number == n &&
					((Card)cards.elementAt(i)).suit == s	) {
				break; 			
			}
		}
		c = (Card)cards.elementAt(i);
		cards.removeElementAt(i);
		return c;
	}
}
