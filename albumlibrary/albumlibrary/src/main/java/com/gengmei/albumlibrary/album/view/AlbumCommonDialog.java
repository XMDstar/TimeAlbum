package com.gengmei.albumlibrary.album.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gengmei.albumlibrary.R;

/**
 * @anthor zcc
 * @time 2018/09/30
 * @class 是否拆分弹窗
 */
public class AlbumCommonDialog extends Dialog {

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_btnLeft;
    private TextView tv_btnRight;
    private OnClickButtonListener mOnClickButtonListener;
    private boolean isBackAvailable = true;// 实体返回键是否可用

    public AlbumCommonDialog(Context context) {
        this(context, true);
    }

    public AlbumCommonDialog(Context context, boolean canceledOnTouchOutside) {
        super(context, R.style.common_dialog);
        isBackAvailable = canceledOnTouchOutside;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        loadLayout();
        getWindow().setGravity(Gravity.CENTER);
    }

    public AlbumCommonDialog(Context context, int theme) {
        this(context, theme, true);
    }

    public AlbumCommonDialog(Context context, int theme, boolean canceledOnTouchOutside) {
        super(context, theme);
        isBackAvailable = canceledOnTouchOutside;
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        loadLayout();
        getWindow().setGravity(Gravity.CENTER);
    }

    private void loadLayout() {
        setContentView(R.layout.dialog_common_album);

        tv_title = (TextView) findViewById(R.id.dialogCommon_tv_title);
        tv_content = (TextView) findViewById(R.id.dialogCommon_tv_content);
        tv_btnLeft = (TextView) findViewById(R.id.dialogCommon_tv_btnLeft);
        tv_btnRight = (TextView) findViewById(R.id.dialogCommon_tv_btnRight);

        addListener();
    }

    private void addListener() {
        tv_btnLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mOnClickButtonListener) {
                    dismiss();
                    mOnClickButtonListener.onClickButtonLeft();
                }
            }
        });
        tv_btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mOnClickButtonListener) {
                    dismiss();
                    mOnClickButtonListener.onClickButtonRight();
                }
            }
        });
    }

    public AlbumCommonDialog setTitleText(String title) {
        tv_title.setText(title + "");
        return this;
    }

    public AlbumCommonDialog setTitleText(int resid) {
        tv_title.setText(resid);
        return this;
    }

    public AlbumCommonDialog setTitleVisible(boolean visible) {
        tv_title.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public AlbumCommonDialog setContent(String content) {
        tv_content.setText(content + "");
        return this;
    }

    public AlbumCommonDialog setContent(int resid) {
        tv_content.setText(resid);
        return this;
    }

    public AlbumCommonDialog setContentVisible(boolean visible) {
        tv_content.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public AlbumCommonDialog setButtonText(String left, String right) {
        tv_btnLeft.setText(left + "");
        tv_btnRight.setText(right + "");
        return this;
    }

    public AlbumCommonDialog setButtonTextColor(int left, int right) {
        tv_btnLeft.setTextColor(left);
        tv_btnRight.setTextColor(right);
        return this;
    }

    public AlbumCommonDialog setButtonText(int left, int right) {
        tv_btnLeft.setText(left);
        tv_btnRight.setText(right);
        return this;
    }

    public AlbumCommonDialog setBackAvailable(boolean available) {
        isBackAvailable = available;
        return this;
    }

    public AlbumCommonDialog setOnClickButtonListener(OnClickButtonListener listener) {
        mOnClickButtonListener = listener;
        return this;
    }

    public interface OnClickButtonListener {
        void onClickButtonLeft();

        void onClickButtonRight();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return !isBackAvailable;
        }
        return super.onKeyDown(keyCode, event);
    }

}
