package jp.nankinhaze;

import static jp.nankinhaze.Constants.GAMENUMBER;
import static jp.nankinhaze.Constants.GAMERECORD;
import static jp.nankinhaze.Constants.NUMBEROFMOVES;
import static jp.nankinhaze.Constants.TABLE_NAME;
import static jp.nankinhaze.Constants.TIME;
import static jp.nankinhaze.Constants.USER;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jp.nankinhaze.R;
//import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
//import android.widget.Button;
import android.widget.Toast;

public class MySurfaceView extends SurfaceView
		implements SurfaceHolder.Callback {
	private static final String TAG = "MySerfaceView"; 
	private SurfaceHolder holder = null;
	private Context myContext;
	private float left;  //�N���b�N���W
	private float top;
	private float selectleft; // �O��I�����̃N���b�N���W
	private float selecttop;
	private float leftOffset = 0;
	private float topOffset = 0;	
	private Pack pack;
	public Pile[] piles;
	private Hand hand = new Hand();
	private Card pointedCard = null;
	private final int nOfPile = 16;
	private final int COLUMN = 1;
	private final int FREE_CELL = 2;
	private final int HOME_CELL = 3;
	private int selpile; // �I�����ꂽPile
	private int despile;
	private int soupile;
	private int nofsc; // �I������Ă���pile�̑I������Ă���J�[�h�̖���
	private int l, m;
	private int tb, fb, nt;
	private int mc;
	private int mp; // �n�߂ɓ�������J�[�h�̈ʒu
	private long seed;
	private int cardsLeft; //�c��̃J�[�h����
	private float scale = 1.0F; //��ʂ̃X�P�[�����O
	private float shiftx = 0.0F; //��ʂ̉E�����ւ̃V�t�g
	private Paint paint = new Paint();
	private Paint paintText = new Paint();
	private final int NO_SELECT = 1;
	private final int POINT = 2;
	private final int SELECT = 3;
	private final int SELECTPOINT = 4;
	private final int MOVE = 5;
	private int condition = NO_SELECT;
	private int penevent = 0;
	private final int EVENT_DOWN = 1;
	private final int EVENT_UP = 2;
	private final int EVENT_MOVE = 3;
	private AlertDialog.Builder builder;
	private int c;      // �I�����ꂽpile
	private int selc;	//�@�I�����ꂽ�J�[�h�ʒu
	private ArrayList<Card> cardbuffer = new ArrayList<Card>(); // �������ړ����̈ꎞ�o�b�t�@
	public ArrayList<String> record = new ArrayList<String>(); // undo�p�̈ړ��L�^
	public int recordIndex; // undo redo�p�C���f�b�N�X	
	private boolean moved = false;
	private int surfaceHeight;
	private Resources res;
	private Freecell activity;
//	private boolean v = true; 
	private RecordData records;
	
	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		myContext = context;
		activity = (Freecell)myContext;
		initSurface();
		res=context.getResources();
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		myContext = context;
		activity = (Freecell)myContext;
		initSurface();
		res=context.getResources();
	}

	public MySurfaceView(Context context) {
		super(context);
		myContext = context;
		activity = (Freecell)myContext;
		initSurface();
		res=context.getResources();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int fprmat, int width, int height) {
		WindowManager wm = (WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
//Log.d("widthPixels",    String.valueOf(displayMetrics.widthPixels));
//Log.d("heightPixels",   String.valueOf(displayMetrics.heightPixels));
// ��ʕ����\���T�C�Y�𒲐�
		int minWidth;
		if (width <= height) {
			minWidth = width;
		} else {
			minWidth = height;
		}
		surfaceHeight = height;
		scale = ((float)minWidth / (float)720) * 0.9f;
		if (width > height) {
			scale = scale * 1.23f;
		}
//		Log.d(TAG, "surfaceChanged" + ", minWidth = " + String.valueOf(minWidth) + ", scale = " + String.valueOf(scale));
		shiftx = ((width / scale) - 720) / 2;
		drawSurface();
//		Log.d(TAG, "surfaceChanged" + ", width = " + String.valueOf(width) + ", height = " + String.valueOf(height));
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}

	public void initSurface(){
		holder = this.getHolder();
		holder.addCallback(this);
		paint.setARGB (255, 149, 165, 166);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.STROKE);
		paintText.setTextSize(24.0f);
		paintText.setTypeface(Typeface.SANS_SERIF);
		paintText.setARGB(255, 255, 255, 255);
	}
	
	public long getSeed() {
		return seed;
	}

	public void setSeed(long i) {
		seed = i;
	}

	public void initPile() {
		pack = new Pack(myContext);
		pack.shuffle(seed);
		makePiles();
		for (int i = 0; i < 52; i++) {
			piles[i % 8].placeCard(pack.pickCard());
		}
		record.clear();
		recordIndex = -1;
		activity.undoSetEnabled(false);
		activity.redoSetEnabled(false);
	}

	public void initGame() {
		condition = NO_SELECT;
	}
	
	public void setPile(String sd[]) {
		String[] pd;
		pack = new Pack(myContext);
		makePiles();
// �J�[�h�̕��т̕���
		nofsc = 0;
		for (int i = 0; i < 16; i++) {
			pd = sd[i].split("/");
		    for (int j=0; j < pd.length; j++) {
			    if (pd[0].length() == 4) {
			    	int s = Integer.parseInt(pd[j].substring(0,1));
			    	int n = Integer.parseInt(pd[j].substring(1,3));
			    	piles[i].placeCard(pack.pickCard(s, n));
			    	if (pd[j].substring(3,4).equals("1")) {
			    		nofsc += 1;
			    		piles[i].setSelect(j);
			    		condition = SELECT;
			    		selpile = i;
			    	}
		    	}
		    }
		}
		Log.d(TAG, "setpile recordIndex = " + String.valueOf(recordIndex) +"recordsize = " + String.valueOf(record.size()));
		if (recordIndex > -1)
			activity.undoSetEnabled(true);
		else
			activity.undoSetEnabled(false);
		if (record.size() -1 != recordIndex)
			activity.redoSetEnabled(true);
		else
			activity.redoSetEnabled(false);			
//		Log.d(TAG, "setpile nofsc = " + String.valueOf(nofsc));
	}

	void makePiles() {
		piles = new Pile[nOfPile];
		for (int i = 0; i < 8; i++) {
			piles[i] = new Pile();
			piles[i].offset = 48;
			piles[i].posx = 80 * i + 48;
			piles[i].posy = 160;
			piles[i].areawidth = 64;
			piles[i].areaheight = 96;
			piles[i].type = COLUMN;
		}
		for (int i = 8; i < 12;i++) {
			piles[i] = new Pile();
			piles[i].posx = 80 * (i-8) + 8;
			piles[i].posy = 40;
			piles[i].areawidth = 64;
			piles[i].areaheight = 96;
			piles[i].type = FREE_CELL;
		}
		for (int i = 12; i < 16;i++) {
			piles[i] = new Pile();
			piles[i].posx = 80 * (i-8) + 88;
			piles[i].posy = 40;
			piles[i].areawidth = 64;
			piles[i].areaheight = 96;
			piles[i].type = HOME_CELL;
		}
	}

	// ��ʂ̕`��
	public void drawSurface(){
// pile�̖����ɂ��I�t�Z�b�g�𒲐�
		int ah = (int)(surfaceHeight / scale - 140 - 96 - 10);
		int nmc = 7;  //�@�����l
		for (int i =0; i < 8; i++) {
			if (nmc < piles[i].getNumberOfCard()) {
				nmc = piles[i].getNumberOfCard();
			}
		}
		int cof = ah / nmc;
		if (48 < cof) {
			cof = 48;
		}
//Log.d(TAG, "drawSurface nmc = " + String.valueOf(nmc) + " ah = " + String.valueOf(ah) + " cof = " + String.valueOf(cof) );
		for (int i = 0; i < 8; i++) {
			piles[i].offset = cof;
			piles[i].rePos();
		}
		Canvas canvas = holder.lockCanvas();
// �w�i
		canvas.drawColor(0xff27ae60);
		canvas.scale(scale, scale);
		canvas.translate(shiftx, 0);
		cardsLeft = 52;
		for (int i =12; i <16; i++) {
			cardsLeft = cardsLeft - piles[i].getNumberOfCard();
		}
		canvas.drawText("Freecell   " + res.getString(R.string.game_no) + " " + String.valueOf(seed) + "    " 
		+  res.getString(R.string.cards_left) + " " +  String.valueOf(cardsLeft) + "    "
		+  res.getString(R.string.moves) + " " +  String.valueOf(recordIndex + 1)
		,0.0f,32.0f, paintText);
// �z�[���Z���A�t���[�Z���A�p�C���̈ʒu
		for (int i = 0; i < 4; i++) {
			canvas.drawRect((80 * (i) + 8), 40, (80 * (i) + 8) + 63, 40 + 95, paint);
			canvas.drawRect((80 * (i) + 408), 40, (80 * (i) + 408) + 63, 40 + 95, paint);
		}
		for (int i = 0; i < 8; i++) {
			canvas.drawRect((80 * (i) + 48), 160, (80 * (i) + 48) + 63, 160 + 95, paint);
		}
// �J�[�h
		for (int i = 0; i < 16; i++) {
			piles[i].paint(canvas);
		}		
// �|�C���g����Ă���J�[�h
		if ((condition == POINT || condition == SELECTPOINT) && pointedCard != null) {
			if (Prefs.getBigimage(getContext())) {
				if (Prefs.getDominanthand(getContext()).equals("right")) {
					pointedCard.bigPaint(canvas, -40f);
				} else {
					pointedCard.bigPaint(canvas, 40f);					
				}
				
			} else {
				pointedCard.paint(canvas);
			}
		}
// �ړ����̃J�[�h
		if (condition == MOVE) {
			hand.setPos(left - leftOffset, top - topOffset);
			hand.paint(canvas);
		}
		holder.unlockCanvasAndPost(canvas);
	}
	
	// �^�b�`�C�x���g�𔻕ʂ��ă}�g���b�N�X��
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		left = event.getX()/scale - shiftx;
		top = event.getY()/scale;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			penevent = EVENT_DOWN;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			penevent = EVENT_UP;
			break;
		case MotionEvent.ACTION_MOVE:
			penevent = EVENT_MOVE;
		}
		if (Prefs.getAutoplay(getContext())) {
			matrixAuto();
		}else {
			matrix();
		}
		drawSurface();
		return true;
	}

	// �蓮�ړ����̏ꍇ�̃C�x���g�}�g���b�N�X
	void matrix() {
		Log.d(TAG, "matrix" + ", condition = " + String.valueOf(condition) + ", penevent = " + String.valueOf(penevent));
		switch (condition) {
		case NO_SELECT:
			switch (penevent) {
			case EVENT_DOWN:
				c = checkPile(left, top);  // �����ꂽ�p�C��
				if (c < 0 || 11 < c || piles[c].getNumberOfCard() == 0)
					;
				else {
					pointedCard = piles[c].getCard(selc);
					checkPoint();
					condition = POINT;
				}
				break;
			}
			break;
		case POINT:
			switch (penevent) {
			case EVENT_UP:
				select();
				break;
			case EVENT_MOVE:
				if ((Math.abs(left - selectleft) > 10) || (Math.abs(top - selecttop) > 10)) {
					select();					
					for (int i = 0; i < nOfPile; i++) {
						piles[i].moving = true;
					}				
					condition = MOVE;					
				}
				break;
			}
			break;
		case MOVE:
			switch (penevent) {
			case EVENT_UP:
				for (int j = 0; j < nOfPile; j++) {
					piles[j].moving = false;
				}
				checkMove();
				condition = NO_SELECT;
				break;
			}
			break;
		case SELECT:
			switch (penevent) {
			case EVENT_DOWN:
				c = checkPile(left, top);  // �����ꂽ�p�C��
				if (c < 0)
					break;
				else if ((c < 12) && (piles[c].getNumberOfCard() != 0)) {
					pointedCard = piles[c].getCard(selc);
					checkPoint();
				} else {
					pointedCard = null;					
				}
				condition = SELECTPOINT;
				break;
			}
			break;
		case SELECTPOINT:
			switch (penevent) {
			case EVENT_UP:
				checkMove();
				condition = NO_SELECT;
				break;
			case EVENT_MOVE:
				if ((Math.abs(left - selectleft) > 10) || (Math.abs(top - selecttop) > 10)) {
					if (piles[c].getNumberOfCard() != 0) { 
						piles[selpile].setSelect(-1);
						hand.clearCard();
						nofsc = 0;
						select();					
						for (int i = 0; i < nOfPile; i++) {
							piles[i].moving = true;
						}				
						condition = MOVE;
					}
				}
				break;
			}
			break;
		}
	}

// �����ړ����̏ꍇ�̃C�x���g�}�g���b�N�X
	void matrixAuto() {
		Log.d(TAG, "matrixauto" + ", condition = " + String.valueOf(condition) + ", penevent = " + String.valueOf(penevent));
		final float sin = 0.34202f; //20 digree
		final float cos = 0.93969f;
		float topMoved;
		float leftMoved;
		switch (condition) {
		case NO_SELECT:
			switch (penevent) {
			case EVENT_DOWN:
				c = checkPile(left, top);  // �����ꂽ�p�C��
				if (c < 0 || 11 < c || piles[c].getNumberOfCard() == 0)
					;
				else { 
					pointedCard = piles[c].getCard(selc);
					checkPoint();
					condition = POINT;
				}
				break;
			}
			break;
		case POINT:
			switch (penevent) {
			case EVENT_UP:
				select();
				checkMove1(true);
				condition = NO_SELECT;
				break;
			case EVENT_MOVE:
				if (Prefs.getOnehand(getContext())) {
					float y = top - selecttop;
					float x = left - selectleft;
					if (Prefs.getDominanthand(getContext()).equals("right")) {
						topMoved = x * sin + y * cos;
						leftMoved = x * cos - y * sin;
					} else {
						topMoved = x * -sin + y * cos;
						leftMoved = x * cos - y * -sin;
					}
				}else {
					topMoved = top - selecttop;
					leftMoved = left - selectleft;
				}
				if (topMoved < -10) {
					select();
					condition = NO_SELECT;
					autoMoveToHome();
				} else if (topMoved > 10) {
					select();					
					for (int i = 0; i < nOfPile; i++) {
						piles[i].moving = true;
					}				
					condition = MOVE;
				} else if (leftMoved > 10) {
					select();
					checkMove1(true);
					condition = NO_SELECT;
				} else if (leftMoved < -10) {
					select();
					checkMove1(false);
					condition = NO_SELECT;
				}
				break;
			}
			break;
		case MOVE:
			switch (penevent) {
			case EVENT_UP:
				for (int j = 0; j < nOfPile; j++) {
					piles[j].moving = false;
				}
				checkMove();
				condition = NO_SELECT;
				break;
			}
			break;
		}		
	}
	
	void checkPoint() {
		// MOVE���Ɋ��炩�Ɉړ�����悤�ɃN���b�N�ʒu�ƃJ�[�h�̃I�t�Z�b�g���L��
		leftOffset = left -(piles[c].getCard(selc)).posx;	
		topOffset = top - (piles[c].getCard(selc)).posy;
		// ����͈͓���MOVE�͖����Ƃ��邽�߃N���b�N�ʒu���L��
		selectleft = left;
		selecttop = top;
		return;
	}
	
	void select() {
		//�J�[�h������z�[���Z���łȂ�������I��
		if ((piles[c].getNumberOfCard() > 0) && (piles[c].type != HOME_CELL)) {
			nofsc = 0;
		//c��pile�̔ԍ�,�I�������J�[�h�̈ʒu��piles[c].selcard ������0�n�܂�,pile�̃J�[�h�̖�����piles[c].getNumberOfCard()
			if (selc == piles[c].getNumberOfCard() - 1) {
		//1���̃J�[�h��I��
				piles[c].setSelect(selc);
				if (penevent == EVENT_MOVE) {
					hand.placeCard(piles[c].getCard());
				}
				nofsc = 1;
				condition = SELECT;
			} else {
		// �A�����đI���\�����`�F�b�N
				boolean b = true;
				for (int i =  selc; i < (piles[c].getNumberOfCard() - 1); i++) {
					if (!(((piles[c].getCard(i)).color) !=
					((piles[c].getCard(i + 1)).color) &&
					((piles[c].getCard(i)).number ==
					((piles[c].getCard(i + 1)).number + 1)))) {
						b = false;
					}
				}
				if (b) {
		//�����J�[�h���I���\�ȏꍇ
					for (int i =  selc; i < (piles[c].getNumberOfCard()); i++) {
						piles[c].setSelect(i);													
						if (penevent == EVENT_MOVE) {
							hand.placeCard(piles[c].getCard(i));
							Log.d(TAG, "matrix" + ", hand.placeCard = " + String.valueOf(i));					
						}
					}			
					nofsc = piles[c].getNumberOfCard() - selc;
					condition = SELECT;
				} else {
		//�����J�[�h���I���ł��Ȃ��ꍇ�͏��1���݂̂�I���AMOVE�͖���
					piles[c].setSelect(piles[c].getNumberOfCard() - 1);																				
					nofsc = 1;
					condition = SELECT;
				}
			}		
			selpile = c;
		}
	}

	boolean canConnect(Card cardDown , Card cardUp) {
		boolean ret;
		if (cardDown.color != cardUp.color && cardDown.number == cardUp.number + 1)
			ret = true;
		else
			ret = false;
		return ret;
	}
	
	boolean canContinue(Card cardDown , Card cardUp) {
		boolean ret;
		if (cardDown.suit == cardUp.suit && cardDown.number == cardUp.number - 1)
			ret = true;
		else
			ret = false;
		return ret;
	}
	
	void checkMove() {
		// �ړ��������͑I������ condition = NO_SELECT
		piles[selpile].setSelect(-1);
		hand.clearCard();
		int cm = checkPile(left, top);  // �����ꂽ�p�C��
		if ((cm < 0) || (selpile == cm)) {
			lastCheck();
			return;
		}
		despile = cm; // �s����pile
		moved = false;
		if (moveToHome()) {
		}
		else if (moveToFree()) {
		} else if(piles[despile].type == COLUMN) {
			checkToColumn();
			if (mp != 0) {
				moved = true;
				moveToColumn();
			}
		}
		lastCheck();
	}

// �����ړ�
	void checkMove1(boolean direction) {
		// �ړ��������͑I������ condition = NO_SELECT
		piles[selpile].setSelect(-1);
		hand.clearCard();
//		int cm = checkPile(left, top);  // �����ꂽ�p�C��
		int cm = selpile;  // �����ꂽ�p�C��
		if (cm < 0) {
			lastCheck();
			return;
		}
		despile = cm; // �s����pile �����l�͉����ꂽ�p�C��
		while (true) {
			moved = false;
			if (direction) { // �E���
				despile = despile + 1;
				if (11 <despile) {
					despile = 0;
				}
				if ((7 < selpile && selpile < 12) && (7 < despile && despile < 12)) {
						if (selpile < despile) {
							despile = 0;
						} else {
//							despile = selpile;
							break;
						}
				}
			} else {  // ������
				despile = despile - 1;
				if (despile < 0) {
					despile = 11;
				}
				if ((7 < selpile && selpile < 12) && (7 < despile && despile < 12)) {

					if (selpile < despile) {
//						despile = 7;
						break;
					} else {
//						despile = selpile;
						despile = 7;
					}
				}
			}
 Log.d(TAG, "chkmove1" + ", selpile = " + String.valueOf(selpile) + ", despile = " + String.valueOf(despile));
			if (despile == cm) {
//			if (despile == selpile) {
				break;
			}
			if (piles[despile].type == FREE_CELL) {
				moveToFree();
			} else if(piles[despile].type == COLUMN) {
				checkToColumn();
				if (mp != 0) {
					moved = true;
					moveToColumn();
				}
			}
			if (moved) {
				break;
			}
		}
		lastCheck();
	}

	void lastCheck() {
		nofsc = 0;							//�z�[���Z���ւ̎����ړ�
		while (checkToHome()) {
			moveCard(soupile, despile, 1);
			drawSurface();
		}
		checkEnd();
		checkDeadlock();
	}
	
	void checkDeadlock() {
		Log.d(TAG, "checkDeadlock");
		if (isDeadlock()) {
			builder = new AlertDialog.Builder(myContext); 
			builder.setTitle(R.string.you_deadlock);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {      
				}
			});
        builder.show();
		}
	}

// ��������J�[�h���������Ɓi�I���j���`�F�b�N
	boolean isDeadlock() {
// �t���[�Z���ƃ^�u���[�ɋ󂫂�����Γ�������(false)
		for (int i = 0; i < 12; i++) {
			if (piles[i].getNumberOfCard() == 0) {
				return false;
			}
		}
// �t���[�Z���ƃ^�u���[�œ������邩���`�F�b�N
		for (int i = 0; i < 12; i++) { //from
			// �t���[�Z���ƃ^�u���[�œ������邩���`�F�b�N
			for (int j = 0; j <8; j++) { // to
				if (i != j) {
					if (canConnect(piles[j].getCard(), piles[i].getCard()))
						 return false;
				}
			}
			// �z�[���Z���֓������邩���`�F�b�N
			for (int j = 12; j < 16; j++) { // to
				if (piles[j].getNumberOfCard() != 0)
					if (canContinue(piles[j].getCard(), piles[i].getCard()))
						return false;
			}
		}
		return true;
	}
	
	void checkEnd() {
		Log.d(TAG, "checkend");
		if (cardsLeft == 0) {
			
			if (Prefs.getRecord(getContext())) {
				records = new RecordData(myContext);
				try {
					addRecord();
				} finally {
					records.close();
				}
			}
			
			
			builder = new AlertDialog.Builder(myContext); 
			builder.setTitle(R.string.you_win);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {      
				}
			});
			builder.show();
		}
	}
	
	int checkPile(float x, float y) {
// �ǂ�pile�Ńy���_�E�����ꂽ���H
		for (int i = 0; i < nOfPile; i++) {
			selc = piles[i].isArea(x, y);
			if (selc != -1) {
				return i;
			}
		}
		return -1;
	}

	void autoMoveToHome() {
//***************** ������ւ�K�v���H
		piles[selpile].setSelect(-1);
		hand.clearCard();
		int cm = checkPile(left, top);  // �����ꂽ�p�C��
		if (cm < 0) {
			lastCheck();
			return;
		}
		moved = false;
		for (despile = 12; despile < 16; despile++) {
			moveToHome();
			if (moved)
				return;
		}
	}

// �z�[���Z���ւ̈ړ�
	boolean moveToHome() {	
//		Log.d(TAG, "MoveToHome" + ", selpile = " + String.valueOf(selpile) + ", despile = " + String.valueOf(despile));
		// �s���悪�z�[���Z��
		if (piles[despile].type == HOME_CELL) {
	// �G�[�X���I������ăz�[���Z���ɃJ�[�h���Ȃ���Έړ�
			if (piles[despile].getNumberOfCard() == 0) {
				if ((piles[selpile].getCard()).number == 1) {
					moved = true;
				    moveCard(selpile, despile, 1);
				}
			} else {
	// �z�[���Z���ɃJ�[�h�����蓯��suit�̈�傫���J�[�h�ł���Έړ�
				if (canContinue(piles[despile].getCard(), piles[selpile].getCard())) {
					moved = true;
		    		moveCard(selpile, despile, 1);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
// �t���[�Z���ւ̈ړ�
	boolean moveToFree() {	
		Log.d(TAG ,"MoveToFree nofsc = " + String.valueOf(nofsc));
		if ((piles[despile].type == FREE_CELL) && (nofsc == 1)) {
			if (piles[despile].getNumberOfCard() == 0) {
				moved = true;
		    	moveCard(selpile, despile, 1);
			}
			return true;
		} else {
			return false;
		}
	}
	
	boolean checkToColumn() {
//		Log.d(TAG, "checkToColumn");
// �s���悪�J����
// �����̈ړ��\���� mc = (1 + f) x 2e �����ŁAf�̓t���[�Z���̌���e�͋��̌�(e��),�A���s��͏����B
		mp = 0;
		tb = 0;
		for (int i = 0; i < 8; i++) {
			if ((piles[i].getNumberOfCard() == 0) && (i !=despile)) {
				tb = tb + 1;
			}
		}
		fb = 0;
		for (int i = 8; i < 12; i++) {
			if (piles[i].getNumberOfCard() == 0) {
				fb = fb + 1;
			}
		}
		Log.d(TAG ,"checkTOMove fb = " + String.valueOf(fb) +" tb= "+ String.valueOf(tb));
		Log.d(TAG ,"checkTOMove nofsc = " + String.valueOf(nofsc));
		mc = (int) ((1 + fb) * Math.pow(2, tb));
// ��������J�[�h�̖����𓾂�
		if (piles[despile].getNumberOfCard() == 0) {
// �s���悪��̏ꍇ
			mp = nofsc;
		} else {
// �s����J�[�h�ƑI���J�[�h�̐擪�̔ԍ��ƐF���`�F�b�N
			if (canConnect(piles[despile].getCard(), piles[selpile].getCard(piles[selpile].getNumberOfCard() - nofsc))) {
				mp = nofsc;
			} else {
				mp = 0;
			}
		}
		Log.d(TAG ,"checkTOMove mp = " + String.valueOf(mp) +" mc= "+ String.valueOf(mc));
		if (mp > mc) {
			mp = 0;
		}
		if (mp == 0) {
			return false;
		}
	return true;
	}
	
	boolean moveToColumn() {
//		Log.d(TAG, "moveToColumn");
		moveCard(selpile, despile, mp);
		return true;
	}

// HOME CELL �ւ̎����ړ�
	boolean checkToHome() {
		Log.d(TAG, "checkToHome");
		int i, j, k, l;
		int n = 0;
		int s = 0;
		int c = 0;
		boolean d1 = false;
		boolean d2 = false;
// A������HOME���󂢂Ă���Έړ�
		d1 = false;
		for (i = 0; i < 12; i++) {
			if (piles[i].getNumberOfCard() != 0) {
				if ((piles[i].getCard()).number == 1) {
					d1 = true;
					break;
				}
			}
		}
		for (j = 12; j <16; j++) {
			if (piles[j].getNumberOfCard() == 0) {
				d2 = true;
				break;
			}
		}
		if (d1 && d2) {
			soupile = i;
			despile = j;
			return true;
		}
// 2�������ē���SUIT��A��HOME�ɂ���Έړ�   
		for (i = 0; i < 12; i++) {
			if (piles[i].getNumberOfCard() != 0)
				if ((piles[i].getCard()).number == 2) {
					s = (piles[i].getCard()).suit;
					break;
				}
		}
		for (j = 12; j <16; j++) {
			if (piles[j].getNumberOfCard() != 0) {
				if ((piles[j].getCard()).number == 1 &&
						(piles[j].getCard()).suit == s) {
					break;
				}
			}
		}
//		Log.d(TAG, "movetohome 2 i = " + String.valueOf(i) +" j = "+ String.valueOf(j));
		if ((i != 12 && j != 16)) {
			soupile = i;
			despile = j;
			return true;
		}
// 3����K�͈ႤCOLOR�̈�������l�ȏ�̃J�[�h��2���z�[���ɂ���Έړ�
		for (i = 0; i < 12; i++) {
			if (piles[i].getNumberOfCard() != 0) {
				n = (piles[i].getCard()).number;
				s = (piles[i].getCard()).suit;
				c = (piles[i].getCard()).color;
				for (j = 12; j <16; j++) {
					if (piles[j].getNumberOfCard() != 0) {
						if ((piles[j].getCard()).number == (n - 1)  &&
							(piles[j].getCard()).suit == s) {
							break;
						}
					}
				}
			}
			if (i != 12 && j != 16) {
				l = 0;
				for (k = 12; k <16; k++) {
					if (piles[k].getNumberOfCard() != 0) {
						if (piles[k].getNumberOfCard() >= (n - 1)
						&& (piles[k].getCard()).color != c) {
							l = l + 1;
						}
					}
				}
				if (l > 1) {
					soupile = i;
					despile = j;
					return true;
				}
			}
		}
		return false;
	}

	public void moveCard(int from, int to, int number) {
//		Log.d(TAG, "moveCard from = " + String.valueOf(from) +" to = "+ String.valueOf(to) +" number = "+ String.valueOf(number) + ", record = "+ String.valueOf(record.size()));
		if (recordIndex < record.size() - 1) {
			for (int i = record.size(); i > recordIndex + 1; i--)
				record.remove(i - 1); 
		}
		record.add(String.valueOf(from) + "/" + String.valueOf(to) + "/" + String.valueOf(number) + "/");
		recordIndex++;
		for (int i = 0; i < number; i++) {
			cardbuffer.add(piles[from].pickCard());
		}
		for (int i = 0; i < number; i++) {
			piles[to].placeCard((Card)cardbuffer.get(cardbuffer.size() - 1));
			cardbuffer.remove(cardbuffer.size() - 1);
		}
		activity.undoSetEnabled(true);
		activity.redoSetEnabled(false);
	}

/*
record       �� �@    [ 0 ] [ 1 ] [ 2 ]
recordsize = 3
recordIndex  -1     0     1�@�@�@�@�Q
undo move���Ă���@recordIndex--
redo recordIndex++���Ă���move

          undo      redo
game�J�n     disable   disable    recordsize = 0
move      enable    disable
undo      none      enable
redo      enable    none
undo�I��     disable   none       recprdIndex == -1
redo�I��     none      disable    recordsize -1 == recordIndex

record size = 0 
*/
	public void undo() {
		String[] s12;
		int f, t, n;
// Log.d(TAG, "undo record.size = " + record.size() +" recordIndex = "+ recordIndex);
	if (recordIndex >= 0) {
			String s = (String)record.get(recordIndex);
			s12 = s.split("/");
			f = Integer.parseInt(s12[1]);
			t = Integer.parseInt(s12[0]);
			n = Integer.parseInt(s12[2]);
			for (int i = 0; i < n; i++) {
				cardbuffer.add(piles[f].pickCard());
			}
			for (int i = 0; i < n; i++) {
				piles[t].placeCard((Card)cardbuffer.get(cardbuffer.size() - 1));
				cardbuffer.remove(cardbuffer.size() - 1);
			}
			recordIndex--;
			if (condition != NO_SELECT) {
				piles[selpile].setSelect(-1);				
			}
			condition = NO_SELECT;
			hand.clearCard();
			drawSurface();
			activity.redoSetEnabled(true);
		}
		if (recordIndex < 0) {
			activity.undoSetEnabled(false);
		}
	}
	
	public void redo() {
		Log.d(TAG, "redo record.size = " + record.size() +" recordIndex = "+ recordIndex);
		String[] s12;
		int f, t, n;
		if (recordIndex < record.size() - 1) {
			recordIndex++;
			String s = (String)record.get(recordIndex);
			Log.d(TAG, "redo record = " + s);
			s12 = s.split("/");
			f = Integer.parseInt(s12[1]);
			t = Integer.parseInt(s12[0]);
			n = Integer.parseInt(s12[2]);
			for (int i = 0; i < n; i++) {
				cardbuffer.add(piles[t].pickCard());
			}
			for (int i = 0; i < n; i++) {
				piles[f].placeCard((Card)cardbuffer.get(cardbuffer.size() - 1));
				cardbuffer.remove(cardbuffer.size() - 1);
			}
			if (condition != NO_SELECT) {
				piles[selpile].setSelect(-1);				
			}
			condition = NO_SELECT;
			hand.clearCard();
			drawSurface();
			activity.undoSetEnabled(true);
		}
		if (recordIndex == record.size() - 1) {
			activity.redoSetEnabled(false);
		}
	}

	private void addRecord() {
	    // Insert a new record into the Events data source.
	    // You would do something similar for delete and update.
	    SQLiteDatabase db = records.getWritableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
// �����Q�[��No.������������INSERT
// �����Q�[��No.������A���̎萔���傫����΁AUPDATE 
//                ���̎萔��,���Ȃ��������Ȃ�Ή������Ȃ�
		int moves = recordIndex + 1;
        Cursor c = db.query(TABLE_NAME, new String[] { NUMBEROFMOVES },
        		 "gamenumber == " + String.valueOf(seed) , null, null, null, null);
        boolean isnotEof = c.moveToFirst();
        if (isnotEof) {
        	int nom = c.getInt(0);
        	if (nom > moves) {
        		String gamerecord = "";
        		for (int i = 0; i <= recordIndex; i++ ) {
        			gamerecord = gamerecord + (String)record.get(i) + "\n";
        		}
        		ContentValues values = new ContentValues();
        	    values.put(TIME, sdf.format(new Date(System.currentTimeMillis())));
        	    values.put(USER, "user name");
        	    values.put(NUMBEROFMOVES, moves);
        	    values.put(GAMERECORD, gamerecord);
        	    db.update(TABLE_NAME, values, "gamenumber == " + String.valueOf(seed), null);
        	}
        	return;
        }
		String gamerecord = "";
		for (int i = 0; i <= recordIndex; i++ ) {
			gamerecord = gamerecord + (String)record.get(i) + "\n";
		}
	    ContentValues values = new ContentValues();
	    values.put(TIME, sdf.format(new Date(System.currentTimeMillis())));
	    values.put(USER, "user name");
	    values.put(GAMENUMBER, seed);
	    values.put(NUMBEROFMOVES, moves);
	    values.put(GAMERECORD, gamerecord);
	    db.insertOrThrow(TABLE_NAME, null, values);
	 }
}
