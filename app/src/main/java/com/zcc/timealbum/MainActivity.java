package com.zcc.timealbum;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.zcc.albumlibrary.album.AlbumConfig;
import com.zcc.albumlibrary.album.TimeAlbumActivity;
import com.zcc.albumlibrary.album.adapter.GridPhotoAdapter;
import com.zcc.albumlibrary.album.bean.AlbumSplitData;
import com.zcc.albumlibrary.album.bean.MaterialBean;
import com.zcc.albumlibrary.album.utils.SplitUtils;
import com.zcc.albumlibrary.album.view.AutoGridLayoutManager;
import com.zcc.albumlibrary.album.view.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etMax;
    private EditText etVideoMax;
    private TextView tvShow;
    private RecyclerView listShow;
    private GridPhotoAdapter adapter;
    private AutoGridLayoutManager gridLayoutManager;

    private void assignViews() {
        etMax = (EditText) findViewById(R.id.et_max);
        etVideoMax = (EditText) findViewById(R.id.et_video_max);
        tvShow = (TextView) findViewById(R.id.tv_show);
        listShow = (RecyclerView) findViewById(R.id.list_show);
        gridLayoutManager = new AutoGridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setScrollEnabled(true);
        listShow.setLayoutManager(gridLayoutManager);
        listShow.addItemDecoration(new RecyclerItemDecoration(4, 5, true));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
    }

    public void toNext(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(AlbumConfig.MAX_NUMBER, TextUtils.isEmpty(etMax.getText().toString()) ? 9 : Integer.parseInt(etMax.getText().toString()));
        bundle.putInt(AlbumConfig.VIDEO_NUMBER, TextUtils.isEmpty(etVideoMax.getText().toString()) ? 1 : Integer.parseInt(etVideoMax.getText().toString()));
        bundle.putString(AlbumConfig.IS_SPLIT, AlbumConfig.NO_SHOW_DIALOG);
        startActivityForResult(new Intent(MainActivity.this, TimeAlbumActivity.class).putExtras(bundle), AlbumConfig.ALBUM_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumConfig.ALBUM_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            List<MaterialBean> list = data.getParcelableArrayListExtra(AlbumConfig.RESULT_TIMEALBUM_DATA);
            boolean isSplit = data.getBooleanExtra(AlbumConfig.RESULT_IS_SPLIT_TIME, false);
            tvShow.setText("选中" + list.size() + "张" + "     是否按照时间拆分=" + isSplit);
            List<String> checkId = new ArrayList<>();
            adapter = new GridPhotoAdapter(list, MainActivity.this, checkId);
            adapter.setRefresh(false, false);
            listShow.setAdapter(adapter);
        }
    }
}
