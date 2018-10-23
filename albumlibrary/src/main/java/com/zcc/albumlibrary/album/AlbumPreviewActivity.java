package com.zcc.albumlibrary.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.zcc.albumlibrary.R;
import com.zcc.albumlibrary.album.adapter.PreviewPGAdapter;
import com.zcc.albumlibrary.album.bean.MaterialBean;
import com.zcc.albumlibrary.album.callback.RefreshCallBack;
import com.zcc.albumlibrary.album.view.PreviewViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 视频，图片预览页面
 */
public class AlbumPreviewActivity extends Activity implements View.OnClickListener, RefreshCallBack {
    private PreviewViewPager viewPager;
    private TextView back, next;

    private ArrayList<MaterialBean> materialBeans;
    private List<MaterialBean> checkList;
    private List<String> checkId;

    private int position;
    private int videoCount = 0;
    private boolean isRefresh = false;

    private PreviewPGAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_album_preview);
        initView();
    }

    private void initView() {
        position = getIntent().getIntExtra("position", 0);
        materialBeans = getIntent().getParcelableArrayListExtra("materialBeans");
        checkList = getIntent().getParcelableArrayListExtra("checkBeans");
        checkId = getIntent().getStringArrayListExtra("checkId");
        videoCount = getIntent().getIntExtra("videocount", 0);

        viewPager = (PreviewViewPager) findViewById(R.id.vp_preview);
        back = (TextView) findViewById(R.id.tv_back_pre);
        next = (TextView) findViewById(R.id.tv_next_pre);
        next.setText("下一步(" + checkId.size() + ")");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int i) {
                position = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        adapter = new PreviewPGAdapter(materialBeans, this, checkId, this, videoCount);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_back_pre) {
            finish();
        } else if (i == R.id.tv_next_pre) {
            if (isRefresh) {
                Intent intent = new Intent();
                intent.putExtra("videocount", videoCount);
                intent.putStringArrayListExtra("checkname", (ArrayList<String>) checkId);
                intent.putParcelableArrayListExtra("changedata", (ArrayList<? extends Parcelable>) checkList);
                setResult(Activity.RESULT_OK, intent);
            }
            finish();

        }
    }

    @Override
    public void checkNews(MaterialBean media, boolean type, String name) {
        if (type) {
            if (!checkList.contains(media)) {
                checkList.add(media);
                if (media.getType() == 2) {
                    videoCount = videoCount + 1;
                }
            }
            if (!checkId.contains(name)) {
                checkId.add(name);
            }
        } else {
            if (checkList.contains(media)) {
                checkList.remove(media);
                if (media.getType() == 2) {
                    videoCount = videoCount - 1;
                }
            }
            if (checkId.contains(name)) {
                checkId.remove(name);
            }
        }
        next.setText("下一步(" + checkList.size() + ")");
        isRefresh = true;
    }
}
