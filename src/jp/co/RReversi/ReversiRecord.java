/**
 * 
 */
package jp.co.RReversi;

import jp.co.RReversi.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 戦績画面処理
 */
public class ReversiRecord extends Activity {
	
	// 対Android実績
	private TextView textTotalAndroid;
	private TextView textWinAndroid;
	private TextView textLoseAndroid;
	private TextView textDrawAndroid;
	// 対Person実績
	private TextView textTotalPerson;
	private TextView textWinPerson;
	private TextView textLosePerson;
	private TextView textDrawPerson;
	
	// Back
	private Button buttonBack;
	// DB操作オブジェクト
	private SQLiteDatabase lightDB;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.record);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final TextView leftText = (TextView) findViewById(R.id.left_text);
        final TextView rightText = (TextView) findViewById(R.id.right_text);        
        PackageManager pm = getPackageManager();
		try {
			PackageInfo info = null;
			// タイトル変更
			info = pm.getPackageInfo("jp.co.RReversi", 0);
			leftText.setText(getString(R.string.app_name));
			rightText.setText("ver" + info.versionName);
		} catch (NameNotFoundException e) {
			Toast tst = Toast.makeText(getApplicationContext(), "nameエラーです", Toast.LENGTH_SHORT);
			tst.setGravity(Gravity.CENTER, 0, 0);
			tst.show();
		}
		// イベント生成
		createEvent();
		
		// DB生成（初回のみ）
		InitDB db = new InitDB(this);
		lightDB =  db.getWritableDatabase();
		String strSql = "SELECT * FROM reversi_record_tbl";
		SQLiteCursor c = (SQLiteCursor) lightDB.rawQuery(strSql, null);
		if (c.getCount() <= 0) {
			Common.showDialog(this,"","戦績の取得に失敗したため、参照出来ません");			
		} else {
			// 表示設定
			int rowcount = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < rowcount ; i++) {
	            String mode = c.getString(0);
	            int total = c.getInt(1);
	            int win = c.getInt(2);
	            int lose = c.getInt(3);
	            int draw = c.getInt(4);
	            setValue(mode,total,win,lose,draw);
	            c.moveToNext();
	        }
		}
		lightDB.close();
    }
	
	/**
	 * 実績表示設定
	 * @param mode
	 * @param total
	 * @param win
	 * @param lose
	 * @param draw
	 */
	private void setValue(String mode, int total, int win, int lose, int draw) {
		if (Constants.MODE_ANDROID.equals(mode)) {
			textTotalAndroid.setText(String.valueOf(total));
			textWinAndroid.setText(String.valueOf(win));
			textLoseAndroid.setText(String.valueOf(lose));
			textDrawAndroid.setText(String.valueOf(draw));
		} else {
			textTotalPerson.setText(String.valueOf(total));
			textWinPerson.setText(String.valueOf(win));
			textLosePerson.setText(String.valueOf(lose));
			textDrawPerson.setText(String.valueOf(draw));
		}
	}
	
	/**
	 * イベント生成
	 */
	private void createEvent() {
		textTotalAndroid = (TextView) findViewById(R.id.textTotalAndroid);
		textWinAndroid = (TextView)findViewById(R.id.textWinAndroid);
		textLoseAndroid = (TextView)findViewById(R.id.textLoseAndroid);
		textDrawAndroid = (TextView)findViewById(R.id.textDrawAndroid);
		textTotalPerson = (TextView) findViewById(R.id.textTotalPerson);
		textWinPerson = (TextView)findViewById(R.id.textWinPerson);
		textLosePerson = (TextView)findViewById(R.id.textLosePerson);
		textDrawPerson = (TextView)findViewById(R.id.textDrawPerson);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		// 戻るボタン
		buttonBack.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	        }
	    });
	}
}
