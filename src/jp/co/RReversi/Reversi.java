package jp.co.RReversi;

import jp.co.RReversi.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * メイン画面処理
 */
public class Reversi extends Activity {
	
	// 設定画面との連携
	private static final int REQUEST_VAL = 0;
	// メニューID
	private static final int MENU_ID_NEW = (Menu.FIRST + 1);
	private static final int MENU_ID_RECORD = (Menu.FIRST + 2);
	private static final int MENU_ID_OPTION = (Menu.FIRST + 3);
	// ボタンオブジェクト
	private ImageButton btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_17,btn_18;
	private ImageButton btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_27,btn_28;
	private ImageButton btn_31,btn_32,btn_33,btn_34,btn_35,btn_36,btn_37,btn_38;
	private ImageButton btn_41,btn_42,btn_43,btn_44,btn_45,btn_46,btn_47,btn_48;
	private ImageButton btn_51,btn_52,btn_53,btn_54,btn_55,btn_56,btn_57,btn_58;
	private ImageButton btn_61,btn_62,btn_63,btn_64,btn_65,btn_66,btn_67,btn_68;
	private ImageButton btn_71,btn_72,btn_73,btn_74,btn_75,btn_76,btn_77,btn_78;
	private ImageButton btn_81,btn_82,btn_83,btn_84,btn_85,btn_86,btn_87,btn_88;
	// 石数
	private TextView textCountBlack,textCountWhite;
	// 星・矢印表示
	private ImageView imageBlackLeft,imageBlackRight;
	private ImageView imageWhiteLeft,imageWhiteRight;
	// 盤面
	private static Board board; 
	// ゲームオブジェクト
	private static Game game;
	// テーブル
	private SQLiteDatabase lightDB;
	// 石数
	private int countBlack = 0;
	private int countWhite = 0;
	// スレッド
	private Thread thread = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView leftText = (TextView) findViewById(R.id.left_text);
        final TextView rightText = (TextView) findViewById(R.id.right_text);        
        PackageManager pm = getPackageManager();
        PackageInfo info = null;
		try {
			// タイトル変更
			info = pm.getPackageInfo("jp.co.RReversi", 0);
			leftText.setText(getString(R.string.app_name));
			rightText.setText("ver" + info.versionName);
		} catch (NameNotFoundException e) {
        	Toast tst = Toast.makeText(getApplicationContext(), "nameエラーです", Toast.LENGTH_SHORT);
			tst.setGravity(Gravity.CENTER, 0, 0);
			tst.show();
		}
		
		// 初期DB設定処理
		initTable();
		
		// 初期設定
		// ボード初期化
		board = new Board();
		board.init();
		
		// イベント生成
		createEvent();
		game = new Game(this, board);
		
		// 初期表示
		// 盤面とボードの同期
		boardSynchronization();
    }
    @Override
    public void onResume() {
    	super.onResume();
    	// ゲーム再開
    	startGameThread(game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // メニューアイテムを追加します
    	 MenuItem item0 = menu.add(Menu.NONE, MENU_ID_NEW, Menu.NONE, R.string.menu_new);
    	 MenuItem item1 = menu.add(Menu.NONE, MENU_ID_RECORD, Menu.NONE, R.string.menu_record);
    	 MenuItem item3 = menu.add(Menu.NONE, MENU_ID_OPTION, Menu.NONE, R.string.menu_option);
    	 item0.setIcon( android.R.drawable.ic_menu_rotate );
    	 item1.setIcon( android.R.drawable.ic_menu_save );
    	 item3.setIcon( android.R.drawable.ic_menu_preferences );
    	 return super.onCreateOptionsMenu(menu);
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;
        switch (item.getItemId()) {
        default:
            ret = super.onOptionsItemSelected(item);
            break;
        case MENU_ID_NEW:
        	// ゲームスレッド停止
        	stopGameThread(game);
        	// ボード初期化
    		board.reset();
    		// ボードと画面を一致させる
    		boardSynchronization();
    		imageBlackLeft.setImageResource(R.drawable.bg_green);
        	imageBlackRight.setImageResource(R.drawable.bg_green);
        	imageWhiteLeft.setImageResource(R.drawable.bg_green);
        	imageWhiteRight.setImageResource(R.drawable.bg_green);
        	imageBlackRight.setImageResource(R.drawable.arrow_right);
        	game.setNowPlayer(Constants.STONE_BLACK);
        	// ゲームスレッド開始
        	startGameThread(game);
            ret = true;
            break;
        case MENU_ID_RECORD:
        	// 勝敗画面を開く
        	openRecordWindow();
            ret = true;
            break;
        case MENU_ID_OPTION:
        	// 設定画面を開く
        	openOptionWindow();
            ret = true;
            break;
        }
        return ret;
    }
    
    /**
     * ゲームスレッド開始
     * @param game
     */
    public void startGameThread(Game game) {
		//　初期化
		thread = null;    		
    	if (isAndroidNowPlayer(game)) {
    		thread = new Thread(game);		
    		//ゲームスレッド開始
    		thread.start();    		
    	}
    }
    /**
     * ゲームスレッド停止
     * @param game
     */
    public void stopGameThread(Game game) {
		//　ゲームスレッド停止
    	thread = null;
    }
    /**
     * Androidの手番かどうか
     * @param game
     * @return true:手番/false:非手番もしくは対人モード
     */
    public boolean isAndroidNowPlayer(Game game) {
    	if (Constants.MODE_ANDROID.equals(game.getMode())
    		&& game.getPlayer2().equals(game.getNowPlayer())) {
    		return true;
    	}
		return false;
    }
    
    /**
     *  初期DB設定処理
     */
    public void initTable() {
    	// 初期DB設定 
		InitDB db = new InitDB(this);
		lightDB =  db.getWritableDatabase();
		lightDB.beginTransaction();
		try {
			String strSql = "SELECT * FROM reversi_record_tbl";
			SQLiteCursor c = (SQLiteCursor) lightDB.rawQuery(strSql, null);
			if (c.getCount() <= 0) {
				//　失敗時　要不要含めエラー処理検討
			}
			lightDB.setTransactionSuccessful();
		} finally {
			lightDB.endTransaction();
			lightDB.close();	
		}
    }
    /**
     * 盤面に設定されたカラーと座標の設定を一致させる 
     */
    public void boardSynchronization(){
    	Stone stone;
    	String color;
    	ImageButton button;
    	countBlack = 0;
    	countWhite = 0;
    	for (int x = 1; x < 9; x++) {
    		for (int y = 1; y < 9; y++) {
    			stone = board.getStone(x, y);
    			//　色取得
    			color = stone.getColor();
    			// ボタン取得
    			button = stone.getBotton();
    			if (button == null) {
    				continue;
    			}
    			// 色に応じてボタンのパラメータ変更
    			if (color == null || color.length()<0) {
    				// 石なし画像
    				button.setImageResource(R.drawable.block);
    				button.setTag(null);
    			} else if (Constants.STONE_BLACK.equals(color)) {
    				button.setImageResource(R.drawable.block_black);
    				button.setTag(Constants.STONE_BLACK);
    				countBlack++;
    			} else {
    				button.setImageResource(R.drawable.block_white);
    				button.setTag(Constants.STONE_WHITE);
    				countWhite++;
    			}
    		}
    	}
    	// 個数設定
    	textCountBlack.setText(String.valueOf(countBlack));
    	textCountWhite.setText(String.valueOf(countWhite));
    	game.setCount1(countBlack);
		game.setCount2(countWhite);
    }
    /**
     *  COM自動処理
     */
    public boolean autoPlayer() {
    	// 手番かどうかチェックする
    	if (!Constants.MODE_ANDROID.equals(game.getMode())) {
    		return false;
    	}
    	if (!isAndroidNowPlayer(game)){
    		return false;
    	}
    	// 置き石を決定する
		Ai ai = new Ai();
		Stone stone = ai.select(board, game);
		// 石反転処理を行う
    	stone.setColor(game.getNowPlayer());
    	if (!isReverseSuccess(stone,stone.getX(),stone.getY())) {
    		stone.setColor(null);
    		return false;
    	}
    	// 画面に反映させる
    	boardSynchronization();

    	// 手番チェック処理
    	checkPlayer();
    	return false;
	}
 
	/**
	 *  石反転処理
	 * @param stone
	 * @param x
	 * @param y
	 * @return true:反転した/false:反転していない
	 */
    public boolean isReverseSuccess(Stone stone, int x, int y) {
    	return board.putStone(stone, x, y);
    }
 
    /**
     * 反転関連の処理
     * @param iButton イメージボタンオブジェクト
     */
    public boolean setReverseStone(ImageButton iButton){
    	// 手番チェック（）
    	if (Constants.MODE_ANDROID.equals(game.getMode())){
    		if (!game.getPlayer1().equals(game.getNowPlayer())){
            	return false;
    		}
    	}
    	// ゲームスレッド停止
    	stopGameThread(game);
    	// 座標を特定する
    	Stone stone = board.getStoneById(iButton.getId());
    	// 石置きチェック処理
    	if (board.isFindStone(stone.getX(),stone.getY())) {
    		return false;
    	}
    	// 石反転処理を行う
    	stone.setColor(game.getNowPlayer());
    	if (!isReverseSuccess(stone,stone.getX(),stone.getY())) {
    		stone.setColor(null);
    		return false;
    	}
    	// 画面に反映させる
    	boardSynchronization();
    	
    	// 判定処理
    	checkPlayer();
    	// ゲームスレッド開始
    	startGameThread(game);
		return true;
    }
    
    /**
     * 手番チェック処理
     */
    private boolean checkPlayer() {
    	// 手番プレイヤーの判定
    	if (changePlayer()) {
    		if (Constants.STONE_BLACK.equals(game.getNowPlayer())) {
        		imageBlackRight.setImageResource(R.drawable.arrow_right);
        		imageWhiteLeft.setImageResource(R.drawable.bg_green);
    		} else {
    			imageWhiteLeft.setImageResource(R.drawable.arrow_left);
    			imageBlackRight.setImageResource(R.drawable.bg_green);
    		}
    	} else {
        	if (board.canReverse(game.getNowPlayer())){
        		//パスダイアログの表示
            	openPassDialog();
            	// ゲームスレッド開始
    			startGameThread(game);
        	} else {
        		// 手番変わらず、置けない場合は終了
            	imageBlackLeft.setImageResource(R.drawable.bg_green);
            	imageBlackRight.setImageResource(R.drawable.bg_green);
            	imageWhiteLeft.setImageResource(R.drawable.bg_green);
            	imageWhiteRight.setImageResource(R.drawable.bg_green);
        		// どちらが多いか
            	int result = 0;
        		if (game.getCount1() > game.getCount2()){
        	    	imageBlackRight.setImageResource(R.drawable.star);
        	    	if (game.getCount2() == 0) {
        	    		imageBlackLeft.setImageResource(R.drawable.star);
        	    	}
        	    	// プレイヤー1が黒石の場合
                	if (Constants.STONE_BLACK.equals(game.getPlayer1())) {
            	    	result = 1;                		
                	} else {
            	    	result = 2;
                	}
        		} else if (game.getCount1() < game.getCount2()){
        	    	imageWhiteLeft.setImageResource(R.drawable.star);
        	    	if(game.getCount1() == 0) {
        	    		imageWhiteRight.setImageResource(R.drawable.star);
        	    	}
        	    	// プレイヤー1が黒石の場合
                	if (Constants.STONE_BLACK.equals(game.getPlayer1())) {
            	    	result = 2;                		
                	} else {
            	    	result = 1;
                	}
        		} else if (game.getCount1() == game.getCount2()) {
        	    	imageBlackRight.setImageResource(R.drawable.star);
        	    	imageWhiteLeft.setImageResource(R.drawable.star);
        	    	result = 0;
        		} else {
        			return false;
        		}            		
        		// 戦績更新処理
        		updateDB(result);
        	}
    	}
    	return false;
	}
    /**
     *  手番プレイヤーの判定
     *  @return true:手番が変わった/false:変わっていない 
     */
    private boolean changePlayer() {
    	if (board.canReverse(game.getNoPlayer())){
    		game.setNowPlayer(game.getNoPlayer());
    		return true;
    	}
		return false;
	}
	/**
	 * 戦績更新処理
	 * @param result
	 */
    public void updateDB(int result){
    	InitDB db = new InitDB(this);
		lightDB =  db.getWritableDatabase();
		String strSql = "SELECT * FROM reversi_record_tbl WHERE vs_mode ='" + game.getMode() + "'";
		SQLiteCursor c = (SQLiteCursor) lightDB.rawQuery(strSql, null);
		int rowcount = c.getCount();
		c.moveToFirst();
		for (int i = 0; i < rowcount ; i++) {
            int total = c.getInt(1);
            int win = c.getInt(2);
            int lose = c.getInt(3);
            int draw = c.getInt(4);
            
    		ContentValues cv = new ContentValues();
            // 勝敗判定
            // 総合は+1
            total++;
    		cv.put("count_total", total);
    		switch (result){
	            case 0:
	            	// 引分
	            	draw++;
	            	cv.put("count_draw", draw);
	            	break;
	            case 1:
	            	// 勝利
	            	win++;
	            	cv.put("count_win", win);
	            	break;
	            case 2:
	            	// 敗北
	            	lose++;
	            	cv.put("count_lose", lose);
	            	break;
            }
    		lightDB.beginTransaction();
    		try {
    			lightDB.update("reversi_record_tbl", cv, "vs_mode = '" + game.getMode() + "'", null);
    			lightDB.setTransactionSuccessful();
    		} catch (Exception ex) {
    			Common.showDialog(this,"","戦績の更新に失敗したため、結果が反映されません");
    		} finally {
    			lightDB.endTransaction();
    		}
            c.moveToNext();
        }
		lightDB.close();
    }


    /**
     * パスダイアログ表示
     */
    private void openPassDialog(){
    	Intent intent = new Intent(Reversi.this,CustomDialogActivity.class);
    	//インテントへのパラメータ付き
        intent.putExtra("player", game.getNoPlayer());
    	startActivity(intent);
    }    
    /**
     * 勝敗画面表示
     */
    private void openRecordWindow(){
    	// updateInitDB(); // 初期化用
    	Intent intent = new Intent(Reversi.this,ReversiRecord.class);
    	startActivity (intent);    	
    }
    /**
     *  設定画面表示
     */
    private void openOptionWindow(){
    	// ゲームスレッド停止
    	stopGameThread(game);
    	Intent intent = new Intent(Reversi.this,ReversiOption.class);
    	//インテントへのパラメータ付き
        intent.putExtra("mode", game.getMode());
        intent.putExtra("stone", game.getPlayer1());
    	startActivityForResult(intent,REQUEST_VAL);

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent) {
    	if (resultCode == RESULT_OK && requestCode == REQUEST_VAL){
			//戻り値パラメータの取得
            SharedPreferences pref=getSharedPreferences(
                "PREVIOUS_RESULT",MODE_PRIVATE);
            // 設定変更処理
            game.setMode(pref.getString("mode", null));
            game.setPlayer1(pref.getString("stone", null));
            game.setPlayer2(game.getReversePlayer());
    	}
    	// ゲームスレッド開始
    	startGameThread(game);
    }    

    /**
     * 各ボタンイベント生成
     */
    private void createEvent() {
		// イベント生成
    	//　石数
    	textCountBlack = (TextView)findViewById(R.id.textCountBlack);
    	textCountWhite = (TextView)findViewById(R.id.textCountWhite);
    	
    	//　星・矢印
    	imageBlackLeft = (ImageView)findViewById(R.id.imageBlackLeft);
    	imageBlackRight = (ImageView)findViewById(R.id.imageBlackRight);
    	imageWhiteLeft = (ImageView)findViewById(R.id.imageWhiteLeft);
    	imageWhiteRight = (ImageView)findViewById(R.id.imageWhiteRight);
    	
    	/* 設定用 */
    	// 背景
    	imageBlackLeft.setImageResource(R.drawable.bg_green);
    	imageBlackRight.setImageResource(R.drawable.bg_green);
    	imageWhiteLeft.setImageResource(R.drawable.bg_green);
    	imageWhiteRight.setImageResource(R.drawable.bg_green);
    	
    	//　矢印
    	imageBlackRight.setImageResource(R.drawable.arrow_right);
    	
		btn_11 = (ImageButton) findViewById(R.id.imageButton11);
		board.getStone(1, 1).setBotton(btn_11);
		board.getStone(1, 1).setId(btn_11.getId());
		btn_11.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_11);
	        }
	    });
		// イベント生成
		btn_12 = (ImageButton) findViewById(R.id.imageButton12);
		board.getStone(1, 2).setBotton(btn_12);
		board.getStone(1,2).setId(btn_12.getId());
		btn_12.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_12);
	        }
	    });
		// イベント生成
		btn_13 = (ImageButton) findViewById(R.id.imageButton13);
		board.getStone(1, 3).setBotton(btn_13);
		board.getStone(1,3).setId(btn_13.getId());
		btn_13.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_13);
	        }
	    });
		// イベント生成
		btn_14 = (ImageButton) findViewById(R.id.imageButton14);
		board.getStone(1, 4).setBotton(btn_14);
		board.getStone(1,4).setId(btn_14.getId());
		btn_14.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_14);
	        }
	    });
		// イベント生成
		btn_15 = (ImageButton) findViewById(R.id.imageButton15);
		board.getStone(1, 5).setBotton(btn_15);
		board.getStone(1,5).setId(btn_15.getId());
		btn_15.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_15);
	        }
	    });
		// イベント生成
		btn_16 = (ImageButton) findViewById(R.id.imageButton16);
		board.getStone(1, 6).setBotton(btn_16);
		board.getStone(1,6).setId(btn_16.getId());
		btn_16.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_16);
	        }
	    });
		// イベント生成
		btn_17 = (ImageButton) findViewById(R.id.imageButton17);
		board.getStone(1, 7).setBotton(btn_17);
		board.getStone(1,7).setId(btn_17.getId());
		btn_17.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_17);
	        }
	    });
		// イベント生成
		btn_18 = (ImageButton) findViewById(R.id.imageButton18);
		board.getStone(1, 8).setBotton(btn_18);
		board.getStone(1,8).setId(btn_18.getId());
		btn_18.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_18);
	        }
	    });
		// イベント生成
		btn_21 = (ImageButton) findViewById(R.id.imageButton21);
		board.getStone(2, 1).setBotton(btn_21);
		board.getStone(2,1).setId(btn_21.getId());
		btn_21.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_21);
	        }
	    });
		// イベント生成
		btn_22 = (ImageButton) findViewById(R.id.imageButton22);
		board.getStone(2, 2).setBotton(btn_22);
		board.getStone(2,2).setId(btn_22.getId());
		btn_22.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_22);
	        }
	    });
		// イベント生成
		btn_23 = (ImageButton) findViewById(R.id.imageButton23);
		board.getStone(2, 3).setBotton(btn_23);
		board.getStone(2,3).setId(btn_23.getId());
		btn_23.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_23);
	        }
	    });
		// イベント生成
		btn_24 = (ImageButton) findViewById(R.id.imageButton24);
		board.getStone(2,4).setBotton(btn_24);
		board.getStone(2,4).setId(btn_24.getId());
		btn_24.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_24);
	        }
	    });
		// イベント生成
		btn_25 = (ImageButton) findViewById(R.id.imageButton25);
		board.getStone(2,5).setBotton(btn_25);
		board.getStone(2,5).setId(btn_25.getId());
		btn_25.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_25);
	        }
	    });
		// イベント生成
		btn_26 = (ImageButton) findViewById(R.id.imageButton26);
		board.getStone(2,6).setBotton(btn_26);
		board.getStone(2,6).setId(btn_26.getId());
		btn_26.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_26);
	        }
	    });
		// イベント生成
		btn_27 = (ImageButton) findViewById(R.id.imageButton27);
		board.getStone(2,7).setBotton(btn_27);
		board.getStone(2,7).setId(btn_27.getId());
		btn_27.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_27);
	        }
	    });
		// イベント生成
		btn_28 = (ImageButton) findViewById(R.id.imageButton28);
		board.getStone(2,8).setBotton(btn_28);
		board.getStone(2,8).setId(btn_28.getId());
		btn_28.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_28);
	        }
	    });
		// イベント生成
		btn_31 = (ImageButton) findViewById(R.id.imageButton31);
		board.getStone(3,1).setBotton(btn_31);
		board.getStone(3,1).setId(btn_31.getId());
		btn_31.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_31);
	        }
	    });
		// イベント生成
		btn_32 = (ImageButton) findViewById(R.id.imageButton32);
		board.getStone(3,2).setBotton(btn_32);
		board.getStone(3,2).setId(btn_32.getId());
		btn_32.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_32);
	        }
	    });
		// イベント生成
		btn_33 = (ImageButton) findViewById(R.id.imageButton33);
		board.getStone(3,3).setBotton(btn_33);
		board.getStone(3,3).setId(btn_33.getId());
		btn_33.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_33);
	        }
	    });
		// イベント生成
		btn_34 = (ImageButton) findViewById(R.id.imageButton34);
		board.getStone(3,4).setBotton(btn_34);
		board.getStone(3,4).setId(btn_34.getId());
		btn_34.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_34);
	        }
	    });
		// イベント生成
		btn_35 = (ImageButton) findViewById(R.id.imageButton35);
		board.getStone(3,5).setBotton(btn_35);
		board.getStone(3,5).setId(btn_35.getId());
		btn_35.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_35);
	        }
	    });
		// イベント生成
		btn_36 = (ImageButton) findViewById(R.id.imageButton36);
		board.getStone(3,6).setBotton(btn_36);
		board.getStone(3,6).setId(btn_36.getId());
		btn_36.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_36);
	        }
	    });
		// イベント生成
		btn_37 = (ImageButton) findViewById(R.id.imageButton37);
		board.getStone(3,7).setBotton(btn_37);
		board.getStone(3,7).setId(btn_37.getId());
		btn_37.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_37);
	        }
	    });
		// イベント生成
		btn_38 = (ImageButton) findViewById(R.id.imageButton38);
		board.getStone(3,8).setBotton(btn_38);
		board.getStone(3,8).setId(btn_38.getId());
		btn_38.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_38);
	        }
	    });
		// イベント生成
		btn_41 = (ImageButton) findViewById(R.id.imageButton41);
		board.getStone(4,1).setBotton(btn_41);
		board.getStone(4,1).setId(btn_41.getId());
		btn_41.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_41);
	        }
	    });
		// イベント生成
		btn_42 = (ImageButton) findViewById(R.id.imageButton42);
		board.getStone(4,2).setBotton(btn_42);
		board.getStone(4,2).setId(btn_42.getId());
		btn_42.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_42);
	        }
	    });
		// イベント生成
		btn_43 = (ImageButton) findViewById(R.id.imageButton43);
		board.getStone(4,3).setBotton(btn_43);
		board.getStone(4,3).setId(btn_43.getId());
		btn_43.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_43);
	        }
	    });
		// イベント生成
		btn_44 = (ImageButton) findViewById(R.id.imageButton44);
		board.getStone(4,4).setBotton(btn_44);
		board.getStone(4,4).setId(btn_44.getId());
		btn_44.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_44);
	        }
	    });
		// イベント生成
		btn_45 = (ImageButton) findViewById(R.id.imageButton45);
		board.getStone(4,5).setBotton(btn_45);
		board.getStone(4,5).setId(btn_45.getId());
		btn_45.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_45);
	        }
	    });
		// イベント生成
		btn_46 = (ImageButton) findViewById(R.id.imageButton46);
		board.getStone(4,6).setBotton(btn_46);
		board.getStone(4,6).setId(btn_46.getId());
		btn_46.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_46);
	        }
	    });

		// イベント生成
		btn_47 = (ImageButton) findViewById(R.id.imageButton47);
		board.getStone(4,7).setBotton(btn_47);
		board.getStone(4,7).setId(btn_47.getId());
		btn_47.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_47);
	        }
	    });
		// イベント生成
		btn_48 = (ImageButton) findViewById(R.id.imageButton48);
		board.getStone(4,8).setBotton(btn_48);
		board.getStone(4,8).setId(btn_48.getId());

		btn_48.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_48);
	        }
	    });
		// イベント生成
		btn_51 = (ImageButton) findViewById(R.id.imageButton51);
		board.getStone(5,1).setBotton(btn_51);
		board.getStone(5,1).setId(btn_51.getId());
		btn_51.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_51);
	        }
	    });
		// イベント生成
		btn_52 = (ImageButton) findViewById(R.id.imageButton52);
		board.getStone(5,2).setBotton(btn_52);
		board.getStone(5,2).setId(btn_52.getId());
		btn_52.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_52);
	        }
	    });
		// イベント生成
		btn_53 = (ImageButton) findViewById(R.id.imageButton53);
		board.getStone(5,3).setBotton(btn_53);
		board.getStone(5,3).setId(btn_53.getId());
		btn_53.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_53);
	        }
	    });
		// イベント生成
		btn_54 = (ImageButton) findViewById(R.id.imageButton54);
		board.getStone(5,4).setBotton(btn_54);
		board.getStone(5,4).setId(btn_54.getId());
		btn_54.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_54);
	        }
	    });
		// イベント生成
		btn_55 = (ImageButton) findViewById(R.id.imageButton55);
		board.getStone(5,5).setBotton(btn_55);
		board.getStone(5,5).setId(btn_55.getId());
		btn_55.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_55);
	        }
	    });
		// イベント生成
		btn_56 = (ImageButton) findViewById(R.id.imageButton56);
		board.getStone(5,6).setBotton(btn_56);
		board.getStone(5,6).setId(btn_56.getId());
		btn_56.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_56);
	        }
	    });
		// イベント生成
		btn_57 = (ImageButton) findViewById(R.id.imageButton57);
		board.getStone(5,7).setBotton(btn_57);
		board.getStone(5,7).setId(btn_57.getId());
		btn_57.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_57);
	        }
	    });
		// イベント生成
		btn_58 = (ImageButton) findViewById(R.id.imageButton58);
		board.getStone(5,8).setBotton(btn_58);
		board.getStone(5,8).setId(btn_58.getId());
		btn_58.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_58);
	        }
	    });
		// イベント生成
		btn_61 = (ImageButton) findViewById(R.id.imageButton61);
		board.getStone(6,1).setBotton(btn_61);
		board.getStone(6,1).setId(btn_61.getId());
		btn_61.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_61);
	        }
	    });
		// イベント生成
		btn_62 = (ImageButton) findViewById(R.id.imageButton62);
		board.getStone(6,2).setBotton(btn_62);
		board.getStone(6,2).setId(btn_62.getId());
		btn_62.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_62);
	        }
	    });
		// イベント生成
		btn_63 = (ImageButton) findViewById(R.id.imageButton63);
		board.getStone(6,3).setBotton(btn_63);
		board.getStone(6,3).setId(btn_63.getId());
		btn_63.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_63);
	        }
	    });
		// イベント生成
		btn_64 = (ImageButton) findViewById(R.id.imageButton64);
		board.getStone(6,4).setBotton(btn_64);
		board.getStone(6,4).setId(btn_64.getId());
		btn_64.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_64);
	        }
	    });
		// イベント生成
		btn_65 = (ImageButton) findViewById(R.id.imageButton65);
		board.getStone(6,5).setBotton(btn_65);
		board.getStone(6,5).setId(btn_65.getId());
		btn_65.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_65);
	        }
	    });
		// イベント生成
		btn_66 = (ImageButton) findViewById(R.id.imageButton66);
		board.getStone(6,6).setBotton(btn_66);
		board.getStone(6,6).setId(btn_66.getId());
		btn_66.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_66);
	        }
	    });
		// イベント生成
		btn_67 = (ImageButton) findViewById(R.id.imageButton67);
		board.getStone(6,7).setBotton(btn_67);
		board.getStone(6,7).setId(btn_67.getId());
		btn_67.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_67);
	        }
	    });
		// イベント生成
		btn_68 = (ImageButton) findViewById(R.id.imageButton68);
		board.getStone(6,8).setBotton(btn_68);
		board.getStone(6,8).setId(btn_68.getId());
		btn_68.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_68);
	        }
	    });
		// イベント生成
		btn_71 = (ImageButton) findViewById(R.id.imageButton71);
		board.getStone(7,1).setBotton(btn_71);
		board.getStone(7,1).setId(btn_71.getId());
		btn_71.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_71);
	        }
	    });
		// イベント生成
		btn_72 = (ImageButton) findViewById(R.id.imageButton72);
		board.getStone(7,2).setBotton(btn_72);
		board.getStone(7,2).setId(btn_72.getId());
		btn_72.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_72);
	        }
	    });
		// イベント生成
		btn_73 = (ImageButton) findViewById(R.id.imageButton73);
		board.getStone(7,3).setBotton(btn_73);
		board.getStone(7,3).setId(btn_73.getId());
		btn_73.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_73);
	        }
	    });
		// イベント生成
		btn_74 = (ImageButton) findViewById(R.id.imageButton74);
		board.getStone(7,4).setBotton(btn_74);
		board.getStone(7,4).setId(btn_74.getId());
		btn_74.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_74);
	        }
	    });
		// イベント生成
		btn_75 = (ImageButton) findViewById(R.id.imageButton75);
		board.getStone(7,5).setBotton(btn_75);
		board.getStone(7,5).setId(btn_75.getId());
		btn_75.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_75);
	        }
	    });
		// イベント生成
		btn_76 = (ImageButton) findViewById(R.id.imageButton76);
		board.getStone(7,6).setBotton(btn_76);
		board.getStone(7,6).setId(btn_76.getId());
		btn_76.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_76);
	        }
	    });
		// イベント生成
		btn_77 = (ImageButton) findViewById(R.id.imageButton77);
		board.getStone(7,7).setBotton(btn_77);
		board.getStone(7,7).setId(btn_77.getId());
		btn_77.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_77);
	        }
	    });
		// イベント生成
		btn_78 = (ImageButton) findViewById(R.id.imageButton78);
		board.getStone(7,8).setBotton(btn_78);
		board.getStone(7,8).setId(btn_78.getId());
		btn_78.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_78);
	        }
	    });
		// イベント生成
		btn_81 = (ImageButton) findViewById(R.id.imageButton81);
		board.getStone(8,1).setBotton(btn_81);
		board.getStone(8,1).setId(btn_81.getId());
		btn_81.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_81);
	        }
	    });
		// イベント生成
		btn_82 = (ImageButton) findViewById(R.id.imageButton82);
		board.getStone(8,2).setBotton(btn_82);
		board.getStone(8,2).setId(btn_82.getId());
		btn_82.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_82);
	        }
	    });
		// イベント生成
		btn_83 = (ImageButton) findViewById(R.id.imageButton83);
		board.getStone(8,3).setBotton(btn_83);
		board.getStone(8,3).setId(btn_83.getId());
		btn_83.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_83);
	        }
	    });
		// イベント生成
		btn_84 = (ImageButton) findViewById(R.id.imageButton84);
		board.getStone(8,4).setBotton(btn_84);
		board.getStone(8,4).setId(btn_84.getId());
		btn_84.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {

	        	setReverseStone(btn_84);
	        }
	    });
		// イベント生成
		btn_85 = (ImageButton) findViewById(R.id.imageButton85);
		board.getStone(8,5).setBotton(btn_85);
		board.getStone(8,5).setId(btn_85.getId());
		btn_85.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_85);
	        }
	    });
		// イベント生成
		btn_86 = (ImageButton) findViewById(R.id.imageButton86);
		board.getStone(8,6).setBotton(btn_86);
		board.getStone(8,6).setId(btn_86.getId());
		btn_86.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_86);
	        }
	    });
		// イベント生成
		btn_87 = (ImageButton) findViewById(R.id.imageButton87);
		board.getStone(8,7).setBotton(btn_87);
		board.getStone(8,7).setId(btn_87.getId());
		btn_87.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_87);
	        }
	    });
		// イベント生成
		btn_88 = (ImageButton) findViewById(R.id.imageButton88);
		board.getStone(8,8).setBotton(btn_88);
		board.getStone(8,8).setId(btn_88.getId());
		btn_88.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setReverseStone(btn_88);
	        }
	    });
	}
 /* 
	// 初期化用
	// DB更新処理
    public void updateInitDB(){
		InitDB db = new InitDB(this);
		lightDB =  db.getWritableDatabase();
		String strSql = "SELECT * FROM reversi_record_tbl WHERE vs_mode ='" + game.getMode() + "'";
		SQLiteCursor c = (SQLiteCursor) lightDB.rawQuery(strSql, null);
		int rowcount = c.getCount();
		c.moveToFirst();
		for (int i = 0; i < rowcount ; i++) {
            int total = c.getInt(1);
            int win = c.getInt(2);
            int lose = c.getInt(3);
            int draw = c.getInt(4);
            
    		ContentValues cv = new ContentValues();
            total = 5;
    		cv.put("count_total", total);
        	draw = 1;
        	cv.put("count_draw", draw);
        	win = 3;
        	cv.put("count_win", win);
        	lose = 1;
        	cv.put("count_lose", lose);
    		lightDB.beginTransaction();
    		try {
    			lightDB.update("reversi_record_tbl", cv, "vs_mode = '" + game.getMode() + "'", null);
    			lightDB.setTransactionSuccessful();
    		} finally {
    			lightDB.endTransaction();
    		}
            c.moveToNext();
        }
		lightDB.close();
    }*/
}