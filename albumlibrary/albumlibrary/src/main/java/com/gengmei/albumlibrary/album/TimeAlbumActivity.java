package com.gengmei.albumlibrary.album;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.gengmei.albumlibrary.R;
import com.gengmei.albumlibrary.album.adapter.TimeAlbumAdapter;
import com.gengmei.albumlibrary.album.bean.AlbumData;
import com.gengmei.albumlibrary.album.bean.AlbumSplitData;
import com.gengmei.albumlibrary.album.bean.MaterialBean;
import com.gengmei.albumlibrary.album.utils.AlbumUtils;
import com.gengmei.albumlibrary.album.utils.SplitUtils;
import com.gengmei.albumlibrary.album.view.AlbumCommonDialog;
import com.gengmei.albumlibrary.event.AlbumEventCode;
import com.gengmei.albumlibrary.event.AlbumMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 相册页面类
 * 实现效果：1.按时间分类显示 2.时间条目滑动吸顶 3.可同时选中多个视频与图片，数量可动态控制
 * 4.可按照时间以及选中顺序拆分 5.视频太大不可选置灰 6.限定总数或者视频数已经达到置灰未选项或置灰未选视频项
 * 7.可以预览视频已经图片并选中或取消  等等
 */
public class TimeAlbumActivity extends Activity implements View.OnClickListener {
    private int REQUEST_CODE_ADD_DATA = 2;
    private int REQUEST_CODE_TAKE_PHOTO = 3;

    private RecyclerView feedList;
    private LinearLayout mSuspensionBar, lyNext;
    private TextView tvDate, tvNext, tvBack;

    private ContentResolver resolver;
    private LinearLayoutManager mLayoutManager;
    private AlbumUtils albumUtils;
    private TimeAlbumAdapter adapter;

    private ArrayList<MaterialBean> materialBeans;
    private List<String> checkName;
    private List<AlbumData> photoDatas;
    private List<MaterialBean> checkList;

    private int mCurrentPosition = 0;
    private int mSuspensionHeight;
    //是否选中并添加拍照返回图片
    private boolean isAdd = false;
    //视频已选中数量
    private int videoCheckCount = 0;
    //是否显示弹窗
    private String isShowDialog = AlbumConfig.YES_SHOW_DIALOG;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    photoDatas = (ArrayList<AlbumData>) bundle.getParcelableArrayList("materialBeans").get(0);
                    materialBeans = (ArrayList<MaterialBean>) bundle.getParcelableArrayList("materialBeans").get(1);
                    if (isAdd && checkList.size() < AlbumConfig.ALBUM_CHECK_MAX) {
                        photoDatas.get(0).getList().get(1).setCheck(true);
                        checkList.add(materialBeans.get(0));
                        checkName.add(materialBeans.get(0).getName());
                        tvNext.setText(checkList.size() + "");
                        videoCheckCount = videoCheckCount + 1;
                    }
                    adapter = new TimeAlbumAdapter(photoDatas, TimeAlbumActivity.this);
                    adapter.setRefresh(false, false);
                    feedList.setAdapter(adapter);
                    adapter.setCheckId(checkName);
                    if (isAdd) {
                        toGray(checkList.size(), videoCheckCount, 3);
                        isAdd = false;
                    }
                    if (photoDatas != null && photoDatas.size() > 0) {
                        tvDate.setText(photoDatas.get(mCurrentPosition).getDate());
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarTextColor(true);
        setContentView(R.layout.activity_time_album);
        parseIntent(getIntent());
        EventBus.getDefault().register(this);
        initData();
        getData();
    }

    /**
     * 解析Intent跳转方式
     *
     * @param intent
     */
    protected void parseIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri == null) {
                finish();
            } else {
                isShowDialog = uri.getQueryParameter(AlbumConfig.IS_SPLIT);
                AlbumConfig.ALBUM_CHECK_MAX = Integer.parseInt(uri.getQueryParameter(AlbumConfig.MAX_NUMBER));
                AlbumConfig.ALBUM_CHECK_VIDEO_MAX = Integer.parseInt(uri.getQueryParameter(AlbumConfig.VIDEO_NUMBER));
            }
        } else {
            Bundle bundle = intent.getExtras();
            if (null == bundle) {
                finish();
                return;
            }
            isShowDialog = bundle.getString(AlbumConfig.IS_SPLIT);
            AlbumConfig.ALBUM_CHECK_MAX = bundle.getInt(AlbumConfig.MAX_NUMBER, AlbumConfig.ALBUM_CHECK_MAX);
            AlbumConfig.ALBUM_CHECK_VIDEO_MAX = bundle.getInt(AlbumConfig.VIDEO_NUMBER, AlbumConfig.ALBUM_CHECK_VIDEO_MAX);
            ;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            //拍照返回数据
            isAdd = true;
            getData();
        }
        if (requestCode == REQUEST_CODE_ADD_DATA && resultCode == Activity.RESULT_OK) {
            //预览页返回数据
            checkList = data.getParcelableArrayListExtra("changedata");
            checkName = data.getStringArrayListExtra("checkname");
            videoCheckCount = data.getIntExtra("videocount", 0);
            adapter.setCheckId(checkName);
            toGray(checkList.size(), videoCheckCount, 3);
            tvNext.setText(checkList.size() + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageReceived(AlbumMessageEvent event) {
        if (event != null && event.getCode() == AlbumEventCode.ALBUM_CHECK) {
            MaterialBean checkBean = (MaterialBean) event.getData();
            if (!checkList.contains(checkBean)) {
                checkList.add(checkBean);
                if (checkBean.getType() == 2) {
                    videoCheckCount = videoCheckCount + 1;
                }
            }
            if (!checkName.contains(checkBean.getName())) {
                checkName.add(checkBean.getName());
            }
            //置灰
            toGray(checkList.size(), videoCheckCount, checkBean.getType());
            tvNext.setText(checkList.size() + "");
        }
        if (event != null && event.getCode() == AlbumEventCode.ALBUM_CHECK_NO) {
            MaterialBean checkBean = (MaterialBean) event.getData();
            if (checkList.contains(checkBean)) {
                checkList.remove(checkBean);
                if (checkBean.getType() == 2) {
                    videoCheckCount = videoCheckCount - 1;
                }
            }
            if (checkName.contains(checkBean.getName())) {
                checkName.remove(checkBean.getName());
            }
            //去除置灰
            clearGray(checkList.size(), videoCheckCount, checkBean.getType());
            tvNext.setText(checkList.size() + "");
        }
        if (event != null && event.getCode() == AlbumEventCode.ALBUM_SHOW_DETAIL) {
            MaterialBean checkBean = (MaterialBean) event.getData();
            if (checkBean != null && checkBean.getType() != 3) {
                int position = materialBeans.indexOf(checkBean);
                Intent intent = new Intent(this, AlbumPreviewActivity.class);
                intent.putParcelableArrayListExtra("materialBeans", materialBeans);
                intent.putParcelableArrayListExtra("checkBeans", (ArrayList<? extends Parcelable>) checkList);
                intent.putStringArrayListExtra("checkId", (ArrayList<String>) checkName);
                intent.putExtra("position", position);
                intent.putExtra("videocount", videoCheckCount);
                startActivityForResult(intent, REQUEST_CODE_ADD_DATA);
            } else {
                takePhoto();
            }

        }
    }

    /**
     * 清除置灰
     */
    private void clearGray(int size, int videoSize, int type) {
        if (type == 1) {
            if (size == AlbumConfig.ALBUM_CHECK_MAX - 1) {
                if (videoSize == AlbumConfig.ALBUM_CHECK_VIDEO_MAX) {
                    adapter.setRefresh(false, true);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setRefresh(false, false);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            if (videoSize == AlbumConfig.ALBUM_CHECK_VIDEO_MAX - 1) {
                adapter.setRefresh(false, false);
                adapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 置灰
     */
    private void toGray(int size, int videoSize, int type) {
        if (type == 1) {
            if (size == AlbumConfig.ALBUM_CHECK_MAX) {
                adapter.setRefresh(true, false);
                adapter.notifyDataSetChanged();
            }
        } else {
            if (size == AlbumConfig.ALBUM_CHECK_MAX) {
                adapter.setRefresh(true, false);
                adapter.notifyDataSetChanged();
            } else {
                if (videoSize == AlbumConfig.ALBUM_CHECK_VIDEO_MAX) {
                    adapter.setRefresh(false, true);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initData() {
        mSuspensionBar = (LinearLayout) findViewById(R.id.suspension_bar);
        feedList = (RecyclerView) findViewById(R.id.feed_list);
        lyNext = (LinearLayout) findViewById(R.id.ly_next);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvBack = (TextView) findViewById(R.id.tv_back);
        lyNext.setOnClickListener(this);
        tvBack.setOnClickListener(this);

        resolver = getContentResolver();
        checkList = new ArrayList<>();
        checkName = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(this);
        feedList.setLayoutManager(mLayoutManager);
        feedList.setHasFixedSize(true);
        ((SimpleItemAnimator) feedList.getItemAnimator())
                .setSupportsChangeAnimations(false);
        feedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = mLayoutManager.findViewByPosition(mCurrentPosition + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                    } else {
                        mSuspensionBar.setY(0);
                    }
                }

                if (mCurrentPosition != mLayoutManager.findFirstVisibleItemPosition()) {
                    mCurrentPosition = mLayoutManager.findFirstVisibleItemPosition();
                    updateSuspensionBar();
                    mSuspensionBar.setY(0);
                }
            }
        });
    }

    private void getData() {
        new Thread() {
            @Override
            public void run() {
                albumUtils = new AlbumUtils(resolver);
                ArrayList<MaterialBean> beans = albumUtils.getSortData();
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                ArrayList Alist = new ArrayList();
                Alist.add(albumUtils.getFormatData(beans, AlbumConfig.ADD_CAMERA));
                Alist.add(beans);
                bundle.putParcelableArrayList("materialBeans", Alist);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void updateSuspensionBar() {
        if (photoDatas != null && photoDatas.size() > 0) {
            tvDate.setText(photoDatas.get(mCurrentPosition).getDate());
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    private void changeStatusBarTextColor(boolean isBlack) {
        //透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_back) {
            finish();
        } else if (i == R.id.ly_next) {
            if (checkList == null || checkList.size() == 0) {
                Toast.makeText(TimeAlbumActivity.this, "请选择照片", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(isShowDialog) && isShowDialog.equals(AlbumConfig.NO_SHOW_DIALOG)) {
                setResult(Activity.RESULT_OK, new Intent().putParcelableArrayListExtra(AlbumConfig.RESULT_TIMEALBUM_DATA, (ArrayList<? extends Parcelable>) checkList).putExtra(AlbumConfig.RESULT_IS_SPLIT_TIME, false));
                finish();
                return;
            }
            final AlbumCommonDialog dialog = new AlbumCommonDialog(this);
            dialog.setTitleVisible(false);
            dialog.setContent("照片时间和日记贴不是同一天哦，是否按照照片时间拆分成多篇日记？");
            dialog.setButtonText("不用了", "好的");
            dialog.setOnClickButtonListener(new AlbumCommonDialog.OnClickButtonListener() {
                @Override
                public void onClickButtonLeft() {
                    dialog.dismiss();
                    setResult(Activity.RESULT_OK, new Intent().putParcelableArrayListExtra(AlbumConfig.RESULT_TIMEALBUM_DATA, (ArrayList<? extends Parcelable>) checkList).putExtra(AlbumConfig.RESULT_IS_SPLIT_TIME, false));
                    finish();
                }

                @Override
                public void onClickButtonRight() {
                    dialog.dismiss();
                    setResult(Activity.RESULT_OK, new Intent().putParcelableArrayListExtra(AlbumConfig.RESULT_TIMEALBUM_DATA, (ArrayList<? extends Parcelable>) checkList).putExtra(AlbumConfig.RESULT_IS_SPLIT_TIME, true));
                    finish();
                }
            });
            dialog.show();

        }
    }
}
