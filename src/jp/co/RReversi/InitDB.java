package jp.co.RReversi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * DB関連クラス
 */
public class InitDB extends SQLiteOpenHelper {
	
	// 初期登録データ
	private String[][] INIT_DATA = {
			{Constants.MODE_ANDROID,"0","0","0","0"},
			{Constants.MODE_PERSON,"0","0","0","0"}
	};	
	/**
	 * コンストラクタ
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	InitDB(Context context) {
		super(context, "reversiDB", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		db.beginTransaction();
		try {
			// テーブル作成
			sb.append("CREATE TABLE reversi_record_tbl");
			sb.append(" (");
			sb.append(" vs_mode TEXT primary key,");
			sb.append(" count_total number not null, ");
			sb.append(" count_win number not null, ");
			sb.append(" count_lose number not null, ");
			sb.append(" count_draw number not null");
			sb.append(" );");
			db.execSQL(sb.toString());
			
			// 初期データ登録
			SQLiteStatement stmt;
			sb = new StringBuffer();
			sb.append("INSERT INTO reversi_record_tbl");
			sb.append(" values ");
			sb.append(" (?, ?, ?, ?, ?);");
			stmt = db.compileStatement(sb.toString());

			for (String[] capital : INIT_DATA) {
				stmt.bindString(1, capital[0]);
				stmt.bindString(2, capital[1]);
				stmt.bindString(3, capital[2]);
				stmt.bindString(4, capital[3]);
				stmt.bindString(5, capital[4]);
				stmt.executeInsert();
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS reversi_record_tbl");
        //onCreate(db);
	}

}
