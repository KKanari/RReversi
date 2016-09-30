/**
 * 
 */
package jp.co.RReversi;

import jp.co.RReversi.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 設定画面処理
 */
public class ReversiOption extends Activity {
	
	// 定数

	
	// 各ボタン
	private ImageButton btn_android;
	private ImageButton btn_person;
	private ImageButton btn_black;
	private ImageButton btn_white;
// 2010/5/17 remove
//	private ImageButton btn_hplink;
	private Button btn_ok;
	private Button btn_cancel;
	
	//　設定値
	private String mode;
	private String stone;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.option);
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
		
	    //インテントからのパラメータ取得
        mode="";
        stone="";
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
        	mode=extras.getString("mode");
        	stone=extras.getString("stone");
            // 項目設定
            setMode(mode);
            setStone(stone);
        }
	}

	/**
	 *  戻り値の設定
	 */
	private void setRequest() {
		//戻り値パラメータの保存            
        SharedPreferences pref=getSharedPreferences(
            "PREVIOUS_RESULT",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString("mode",mode);
        editor.putString("stone",stone);
        editor.commit();
        //アクティビティの終了           
        setResult(RESULT_OK); 
	}

	/**
     * モード設定
     * @param mode
     */
    public boolean setMode(String mode){
    	if (Constants.MODE_ANDROID.equals(mode)) {
        	// android
    		btn_android.setBackgroundResource(R.drawable.button_android_on);
        	btn_android.setTag(Constants.ANDROID_ON);
        	// person
        	btn_person.setBackgroundResource(R.drawable.button_person_off);
        	btn_person.setTag(Constants.PERSON_OFF);
    	} else if (Constants.MODE_PERSON.equals(mode)){
    		btn_person.setBackgroundResource(R.drawable.button_person_on);
        	btn_person.setTag(Constants.PERSON_ON);
        	// person
        	btn_android.setBackgroundResource(R.drawable.button_android_off);
        	btn_android.setTag(Constants.ANDROID_OFF);
    	} else {
    		//　変化なし
    	}
		return false;
    }
    
	/**
     * モード切替処理
     * @param iButton イメージボタンオブジェクト
     */
    public boolean setMode(ImageButton iButton){
    	if (Constants.ANDROID_OFF.equals(iButton.getTag().toString())) {
        	// android
    		iButton.setBackgroundResource(R.drawable.button_android_on);
        	iButton.setTag(Constants.ANDROID_ON);
        	mode = Constants.MODE_ANDROID;
        	// person
        	btn_person.setBackgroundResource(R.drawable.button_person_off);
        	btn_person.setTag(Constants.PERSON_OFF);
    	} else if (Constants.PERSON_OFF.equals(iButton.getTag().toString())){
    		iButton.setBackgroundResource(R.drawable.button_person_on);
        	iButton.setTag(Constants.PERSON_ON);
        	mode = Constants.MODE_PERSON;
        	// person
        	btn_android.setBackgroundResource(R.drawable.button_android_off);
        	btn_android.setTag(Constants.ANDROID_OFF);
    	} else {
    		//　変化なし
    	}
		return false;
    }
    
    /**
     * 自石設定処理
     * @param iButton イメージボタンオブジェクト
     */
    public boolean setStone(String stone){
    	if (Constants.STONE_BLACK.equals(stone)) {
        	// BLACK
    		btn_black.setImageResource(R.drawable.button_black_on);
        	btn_black.setTag(Constants.BLACK_ON);
        	// WHITE
        	btn_white.setImageResource(R.drawable.button_white_off);
        	btn_white.setTag(Constants.WHITE_OFF);
    	} else if ("white".equals(stone)){
    		// WHITE
    		btn_white.setImageResource(R.drawable.button_white_on);
        	btn_white.setTag(Constants.WHITE_ON);
        	// person
        	btn_black.setImageResource(R.drawable.button_black_off);
        	btn_black.setTag(Constants.BLACK_OFF);
    	} else {
    		//　変化なし
    	}
		return false;
    }
	/**
     * 自石切替処理
     * @param iButton イメージボタンオブジェクト
     */
    public boolean setStone(ImageButton iButton){
    	if (Constants.BLACK_OFF.equals(iButton.getTag().toString())) {
        	// BLACK
    		iButton.setImageResource(R.drawable.button_black_on);
        	iButton.setTag(Constants.BLACK_ON);
        	stone = Constants.STONE_BLACK;
        	// WHITE
        	btn_white.setImageResource(R.drawable.button_white_off);
        	btn_white.setTag(Constants.WHITE_OFF);
    	} else if (Constants.WHITE_OFF.equals(iButton.getTag().toString())){
    		// WHITE
    		iButton.setImageResource(R.drawable.button_white_on);
        	iButton.setTag(Constants.WHITE_ON);
        	stone = Constants.STONE_WHITE;
        	// person
        	btn_black.setImageResource(R.drawable.button_black_off);
        	btn_black.setTag(Constants.BLACK_OFF);
    	} else {
    		//　変化なし
    	}
		return false;
    }
	/**
	 * イベント生成
	 */
	private void createEvent() {
		btn_android = (ImageButton) findViewById(R.id.imageButtonAndroid);
		btn_person = (ImageButton) findViewById(R.id.imageButtonPerson);
		btn_black = (ImageButton) findViewById(R.id.imageButtonBlack);
		btn_white = (ImageButton) findViewById(R.id.imageButtonWhite);
// 2010/5/17 remove
//		btn_hplink = (ImageButton) findViewById(R.id.imageButtonHPLink);
	    btn_ok = (Button) findViewById(R.id.buttonOk);
	    btn_cancel = (Button) findViewById(R.id.buttonCancel);
	    btn_android.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setMode(btn_android);
	        }
	    });
	    btn_person.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setMode(btn_person);
	        }
	    });
	    btn_black.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setStone(btn_black);
	        }
	    });
	    btn_white.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setStone(btn_white);
	        }
	    });

// 2010/5/17 remove
//	    btn_hplink.setOnClickListener(new OnClickListener() {
//	        public void onClick(View v) {
//	        	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//	        	startActivity(intent);
//	        }
//	    });

	    btn_ok.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	setRequest();
	            finish();
	        }
	    });
	    btn_cancel.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	        }
	    });
	}
}
