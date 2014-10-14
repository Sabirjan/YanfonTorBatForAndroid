/**
 * Copyright ? 2012-12-13 shine.cn.Co.,Ltd
 * Kaptar 上午10:04:09
 * Version TODO
 * All right reserved.
 *
 */

package com.izqisoft.yanfonbat;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 类描述：是否，确认框，比如删除一个短信或者内容的时候弹出 创建者：Sabirjan 项目名称： Kaptar 创建时间： 2012-12-13
 * 上午10:04:09 版本号： v1.0 开发商： 金钻电脑公司
 */
public class ConfirmDialog extends Dialog implements
		android.view.View.OnClickListener

{

	YesNoConfirmListener l_btn_ok;
	private TextView tv_title;// 标题
	private TextView tv_content;// 内容
	private TextView tv_yesBtn;// 是 按钮的Text
	private TextView tv_noBtn;// 否 按钮的Text
	private RelativeLayout rel_yesBtn;// 是 按钮的点击
	private RelativeLayout rel_noBtn;// 否 按钮的点击
	private Typeface typeface;
	/**
	 * 
	 * @param context
	 *            Activity
	 * @param listner
	 *            YesNoConfirmListener 监听器 ，点击是 或者否的时候触发
	 */
	public ConfirmDialog(Context context, YesNoConfirmListener listner) {
		super(context,R.style.readerDialog);
		//
		typeface = Typeface.createFromAsset(context.getAssets(),
				"UkijTuzTom.ttf");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_confrim);
		l_btn_ok = listner;
		initControl();

	}

	/**
	 * 
	 * 方法描述 :设置提示框的标题
	 * 
	 * @param strtitle
	 *            void
	 */
	public void setTitle(String strtitle) {
		if (strtitle != null && strtitle.length() > 0) {
			try {
				this.tv_title.setText(UyghurCharUtilities.getUtilities().getUyPFStr(strtitle));
			} catch (Exception ex) {
				//
			}
		}
	}
	public void Hide_NoButton()
	{
		this.rel_noBtn.setVisibility(View.GONE);
	}
	/**
	 * 
	 * 方法描述 : 设置提示的内容
	 * 
	 * @param strcontent
	 *            void
	 */
	public void setContentText(String strcontent) {
		if (strcontent != null && strcontent.length() > 0) {
			try {
				this.tv_content.setText( UyghurCharUtilities.getUtilities().getUyPFStr(strcontent));
			} catch (Exception ex) {
				//
			}
		}
	}

	/**
	 * 
	 * 方法描述 :设置按钮文本
	 * 
	 * @param strYesBtnText
	 *            确认框的"是"的文本
	 * @param strNoBtnText
	 *            确认框的"否"的文本
	 */
	public void setYesNoText(String strYesBtnText, String strNoBtnText) {
		try {
			
			this.tv_yesBtn.setText(strYesBtnText == null ? "" : UyghurCharUtilities.getUtilities().getUyPFStr(strYesBtnText));
			this.tv_noBtn.setText(strNoBtnText == null ? "" : UyghurCharUtilities.getUtilities().getUyPFStr(strNoBtnText));
		} catch (Exception ex) {
			//
		}
	}

	private void initControl() {
		try {
			this.tv_title = (TextView) this
					.findViewById(R.id.txt_confrim_title);
			this.tv_content = (TextView) this
					.findViewById(R.id.txt_confrim_content);
			this.tv_noBtn = (TextView) this
					.findViewById(R.id.txt_confrim_no);
			this.tv_yesBtn = (TextView) this
					.findViewById(R.id.txt_confrim_yes);

			//
			this.rel_yesBtn = (RelativeLayout) this
					.findViewById(R.id.rellayout_confirm_yes);
			this.rel_noBtn = (RelativeLayout) this
					.findViewById(R.id.rellayout_confirm_no);
			this.rel_noBtn.setOnClickListener(this);
			this.rel_yesBtn.setOnClickListener(this);
			
			this.tv_title.setTypeface(typeface);
			this.tv_content.setTypeface(typeface);
			this.tv_noBtn.setTypeface(typeface);
			this.tv_yesBtn.setTypeface(typeface);
			
		} catch (Exception ex) {
			//System.out.println("initControl error:" + ex);
		}
	}

	/*
	 * 处理Yes ，No 按钮的点击事件处理
	 */
	@Override
	public void onClick(View v) {
		
		if (l_btn_ok != null) {
			if (v == this.rel_yesBtn) {
				l_btn_ok.confirmYes();
				this.dismiss();
			} else if (v == this.rel_noBtn) {
				l_btn_ok.confirmNo();
				this.dismiss();
			}
		}
		else
		{
			this.dismiss();
		}
		
	}

}
