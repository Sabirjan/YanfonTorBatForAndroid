package com.izqisoft.yanfonbat;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity {

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		if (isNetworkAvailable(this)) {
			mhandler.sendEmptyMessageDelayed(100, 3000);
		} else {
			ConfirmDialog dialog = new ConfirmDialog(WelcomeActivity.this,
					new YesNoConfirmListener() {

						@Override
						public void confirmYes() {

							Intent intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
							startActivity(intent);
					}

						@Override
						public void confirmNo() {
							finish();
							System.exit(1);

						}
					});
			dialog.setTitle("يانفون تورى نورمال ئەمەسكەن؟");
			dialog.setYesNoText("ھەئە", "ياق");
			dialog.setContentText("يانفون تورىنى تەڭشەمسىز؟");
			dialog.show();

		}

	};

	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, Activity_WebView.class);
			startActivity(intent);
			finish();

		};

	};

	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					System.out.println(i + "===状态==="
							+ networkInfo[i].getState());
					System.out.println(i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
