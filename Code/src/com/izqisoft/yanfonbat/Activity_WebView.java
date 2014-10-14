package com.izqisoft.yanfonbat;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class Activity_WebView extends Activity {

	private String HostUrl = "http://yanfunum.com";
	private String name = "يانفون توربەتلىرى";
	private WebView mWebView = null;
	private SeekBar webviewSeekBar = null;
	private LinearLayout layout_refresh = null;
	private LinearLayout layout_wait = null;
	private RelativeLayout mainWaitLayout;
	private final String strUpdateUrl = "http://www.xxxx.com/ver.php?ver=1";
	private TextView tv_title;
	private UyghurCharUtilities uyConvert = UyghurCharUtilities.getUtilities();

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);
		// HostUrl = this.getIntent().getStringExtra(Activity_Player.HOSTURL);
		if (HostUrl == null) {
			HostUrl = "http://yanfunum.com";
		}
		Typeface typeface = Typeface.createFromAsset(this.getAssets(),
				"UkijTuzTom.ttf");
		mWebView = (WebView) this.findViewById(R.id.mwebview);
		mWebView.setWebChromeClient(new AlmasWebChromeClient());
		mWebView.setWebViewClient(new AlmasWebViewClient());
		// mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		mWebView.getSettings().setUserAgentString("Android");

		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		// mWebView.getSettings().setBlockNetworkImage(true);
		// mWebView.getSettings().setBlockNetworkLoads(true);
		mWebView.getSettings().setAllowFileAccess(true);

		mWebView.getSettings().setLoadsImagesAutomatically(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		webviewSeekBar = (SeekBar) this.findViewById(R.id.webviewSeekBar);
		webviewSeekBar.setEnabled(false);
		webviewSeekBar.setMax(100);
		webviewSeekBar.setProgress(10);
		// mWebView.clearCache(true);
		tv_title = (TextView) this.findViewById(R.id.detail_title);
		tv_title.setTypeface(typeface);
		layout_refresh = (LinearLayout) this.findViewById(R.id.layout_refresh);
		layout_wait = (LinearLayout) this.findViewById(R.id.layout_wait);
		mainWaitLayout = (RelativeLayout) this
				.findViewById(R.id.mainWaitLayout);
		tv_title.setText(UyghurCharUtilities.getUtilities().getUyPFStr(name));
		ImageView detail_titlebar_back = (ImageView) this
				.findViewById(R.id.detail_titlebar_back);
		detail_titlebar_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mWebView.stopLoading();
				mWebView.removeAllViews();
				finish();
				System.exit(0);
			}
		});
		ImageButton btn_fenxiang = (ImageButton) this
				.findViewById(R.id.detail_titlebar_download);
		btn_fenxiang.setVisibility(View.VISIBLE);
		btn_fenxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				FenXiang();
			}
		});
		btn_fenxiang.setVisibility(View.GONE);
		RelativeLayout rel_refresh = (RelativeLayout) this
				.findViewById(R.id.rel_refresh);
		rel_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mWebView.setVisibility(View.GONE);
				layout_wait.setVisibility(View.VISIBLE);
				mainWaitLayout.setVisibility(View.VISIBLE);
				layout_refresh.setVisibility(View.GONE);
				mWebView.reload();
			}
		});

		mWebView.loadUrl(HostUrl);
		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {

				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				if (mWebView != null && mWebView.canGoBack()) {
					mWebView.goBack();
				}
			}
		});

		findViewById(R.id.btn_prev).setOnClickListener(new controlerOnClick());
		findViewById(R.id.btn_home).setOnClickListener(new controlerOnClick());
		findViewById(R.id.btn_next).setOnClickListener(new controlerOnClick());
		checkUpdate();
	}

	private void checkUpdate() {
		try {
			HttpUtils http = new HttpUtils();
			http.configDefaultHttpCacheExpiry(0);
			http.configCurrentHttpCacheExpiry(0);
			http.send(HttpMethod.GET, strUpdateUrl,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {

							try {
								String result = arg0.result;
								if (result != null
										&& result.trim().length() > 0) {
									JSONObject obj = new JSONObject(result);
									int verCode = obj.getInt("vercode");
									final String url = obj.getString("apkurl");
									if (verCode > getVersion()) {
										// 有新版本
										ConfirmDialog dialog = new ConfirmDialog(
												Activity_WebView.this,
												new YesNoConfirmListener() {

													@Override
													public void confirmYes() {
														// TODO Auto-generated
														// method stub
														Intent intent = new Intent();
														intent.setAction("android.intent.action.VIEW");
														Uri content_url = Uri
																.parse(url);
														intent.setData(content_url);
														startActivity(intent);
													}

													@Override
													public void confirmNo() {
														// TODO Auto-generated
														// method stub

													}
												});
										dialog.setTitle("يېڭى نەشىرى تارقىتىلدى");
										dialog.setYesNoText("ھەئە", "ياق");
										dialog.setContentText(obj
												.getString("des") == null ? "دەرھال يېڭىلىۋىلىڭ..."
												: obj.getString("des"));
										dialog.show();
									}
								}
							} catch (Exception ex) {

							}

						}

						private int getVersion() throws Exception {
							// 获取packagemanager的实例
							PackageManager packageManager = getPackageManager();
							// getPackageName()是你当前类的包名，0代表是获取版本信息
							PackageInfo packInfo = packageManager
									.getPackageInfo(getPackageName(), 0);
							return packInfo.versionCode;
						}

					});
		} catch (Exception ex) {

		}
	}

	private void FenXiang() {
		try {
		} catch (Exception ex) {
			Log.e("Sabirjan", ex.getMessage());
		}
	}

	class controlerOnClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			if (mWebView == null) {
				return;
			}
			try {
				if (v.getId() == R.id.btn_prev) {

					if (mWebView.canGoBack()) {
						mWebView.stopLoading();
						mWebView.goBack();
					} else {
						// MyToast.ShowToast(Activity_WebView.this,
						// "قايتالمايدۇ.");
					}

				} else if (v.getId() == R.id.btn_home) {
					mWebView.stopLoading();
					mWebView.removeAllViews();
					mWebView.loadUrl(HostUrl);

				} else if (v.getId() == R.id.btn_next) {
					if (mWebView.canGoForward()) {
						mWebView.stopLoading();
						mWebView.goForward();
					} else {
						// MyToast.ShowToast(Activity_WebView.this,
						// "قايتالمايدۇ.");
					}
				}

			} catch (Exception ex) {
				// MyToast.ShowToast(Activity_WebView.this, "خاتالىق كۆرۈلدى.");
			}

		}
	}

	long mExitTime = 0;

	private void Back() {
		try {
			if (mWebView != null) {
				if (mWebView.canGoBack()) {
					mWebView.stopLoading();
					mWebView.goBack();
				} else {
					try {

						if ((System.currentTimeMillis() - mExitTime) > 2000) {
							MyToast.ShowToast(
									getApplicationContext(),
									UyghurCharUtilities
											.getUtilities()
											.getUyPFStr(
													"يەنە بىر قېتىم بېسىپ چېكىنىڭ!"));
							mExitTime = System.currentTimeMillis();
						} else {

							this.finish();
							System.exit(0);
						}
					} catch (Exception ex) {
						// System.out.println("Error onKeyDown:" + ex);
					}
				}
			}

		} catch (Exception ex) {
			// MyLog.Log("back error:" + ex);
		}
	}

	public class AlmasWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			// return super.onJsAlert(view, url, message, result);
			return false;
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			// TODO Auto-generated method stub
			// return super.onJsConfirm(view, url, message, result);
			return false;

		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			webviewSeekBar.setProgress(newProgress);
		}

	}

	public class AlmasWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			tv_title.setText(mWebView.getTitle() == null ? name : uyConvert
					.getUyPFStr(mWebView.getTitle()));
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			mWebView.setVisibility(View.GONE);
			layout_wait.setVisibility(View.GONE);
			mainWaitLayout.setVisibility(View.VISIBLE);
			layout_refresh.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			mWebView.setVisibility(View.VISIBLE);
			mainWaitLayout.setVisibility(View.GONE);

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			webviewSeekBar.setProgress(0);
			return super.shouldOverrideUrlLoading(view, url);

		}
	}

	String urlname = "";
	String url = "";

	private String getUrl() {
		// HostUrl = "http://m.zilkuy.cn";
		return HostUrl;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
