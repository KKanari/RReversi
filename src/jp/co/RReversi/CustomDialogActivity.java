package jp.co.RReversi;

import java.util.Timer;
import java.util.TimerTask;

import jp.co.RReversi.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * パスダイアログ
 */
public class CustomDialogActivity extends Activity {
	
	private Timer timer = null;
	private TimerTask timerTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Be sure to call the super class.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_dialog);
		final TextView textPass = (TextView) findViewById(R.id.textPassPlayer);
		Intent intent = getIntent();
        //キーを使ってパラメータを取得
        final String player = intent.getStringExtra("player");
		// 文字設定
        textPass.setText(player);
        // 文字色設定
        if (Constants.STONE_BLACK.equals(player)) {
    		textPass.setTextColor(Color.BLACK);
        } else {
    		textPass.setTextColor(Color.WHITE);
        }
		// タイマの設定
		timer = new Timer(true);
		timerTask = new TimerTask() {
						@Override
						public void run() {
							finish();
						}
					};
		timer.schedule(timerTask, 2000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// タイマーを明示的に終了
		timer.cancel();
	}
}
