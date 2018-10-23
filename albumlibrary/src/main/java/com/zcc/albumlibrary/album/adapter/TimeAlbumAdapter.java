package com.zcc.albumlibrary.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zcc.albumlibrary.R;
import com.zcc.albumlibrary.album.bean.AlbumData;

import java.util.List;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 相册页适配器
 */
public class TimeAlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private List<String> checkId;
    private boolean isRefresh = false, videoGray = false;//isRefresh全部置灰 videoGray视频置灰
    private List<AlbumData> photos;
    private Context mContext;
    private RecyclerView.RecycledViewPool viewPool;
    private GridPhotoAdapter adapter;

    public TimeAlbumAdapter(List<AlbumData> photos, Context mContext) {
        this.photos = photos;
        this.mContext = mContext;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    public void setCheckId(List<String> checkId) {
        this.checkId = checkId;
        notifyDataSetChanged();
    }

    public void setRefresh(boolean refresh, boolean videoGray) {
        this.isRefresh = refresh;
        this.videoGray = videoGray;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_photo, parent, false);
        AlbumViewHolder holder = new AlbumViewHolder(view);
        holder.itemDataRC.setRecycledViewPool(viewPool);
        holder.itemDataRC.setHasFixedSize(true);
        ((SimpleItemAnimator) holder.itemDataRC.getItemAnimator())
                .setSupportsChangeAnimations(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.itemDateTv.setText(photos.get(position).getDate());
        adapter = new GridPhotoAdapter(photos.get(position).getList(), mContext, checkId);
        adapter.setRefresh(isRefresh, videoGray);
        holder.itemDataRC.setAdapter(adapter);

    }


    @Override
    public int getItemCount() {
        if (photos != null) {
            return photos.size();
        } else return 0;
    }

}