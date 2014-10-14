package com.izqisoft.yanfonbat;


import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

	public static final int Error = -1;
	public static final int OK = 1;

	public static void ShowToast(Context con, String info) {

		Typeface typeface = Typeface.createFromAsset(con.getAssets(),
				"UkijTuzTom.ttf");

		LayoutInflater li = (LayoutInflater) con
				.getSystemService(con.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.toast, null);
		// 把布局文件toast.xml转换成一个view
		Toast toast = new Toast(con);
		toast.setView(view);
		toast.setGravity(Gravity.BOTTOM, 0, 30);
		// 载入view,即显示toast.xml的内容
		TextView tv = (TextView) view.findViewById(R.id.tvToust);
		// tv=UyControl.getUyTextView(con, tv);
		tv.setTypeface(typeface);

		tv.setTextSize(16);
		tv.setText(UyghurCharUtilities.getUtilities().getUyPFStr(info));
		// 修改TextView里的内容
		toast.setDuration(Toast.LENGTH_SHORT);
		// 设置显示时间，长时间Toast.LENGTH_LONG，短时间为Toast.LENGTH_SHORT,不可以自己编辑
		toast.show();

	}

}
