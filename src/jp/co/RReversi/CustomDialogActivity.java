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
 * �p�X�_�C�A���O
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
        //�L�[���g���ăp�����[�^���擾
        final String player = intent.getStringExtra("player");
		// �����ݒ�
        textPass.setText(player);
        // �����F�ݒ�
        if (Constants.STONE_BLACK.equals(player)) {
    		textPass.setTextColor(Color.BLACK);
        } else {
    		textPass.setTextColor(Color.WHITE);
        }
		// �^�C�}�̐ݒ�
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
		// �^�C�}�[�𖾎��I�ɏI��
		timer.cancel();
	}
}
