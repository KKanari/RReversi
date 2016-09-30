/**
 * 
 */
package jp.co.RReversi;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * @author Owner
 *
 */
public class Common {
	/**
	 * ダイアログの表示
	 * @param activity
	 * @param title
	 * @param text
	 */
    public static void showDialog(final Activity activity,
        String title,String text) {
        AlertDialog.Builder ad=new AlertDialog.Builder(activity);
        ad.setTitle(title);
        ad.setMessage(text);
//        ad.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int whichButton) {
//                activity.setResult(Activity.RESULT_OK);
//            }
//        });
        ad.create();
        ad.show();
    }
}
