package com.zcc.albumlibrary.album.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcc.albumlibrary.R;
import com.zcc.albumlibrary.album.view.AutoGridLayoutManager;
import com.zcc.albumlibrary.album.view.RecyclerItemDecoration;


/**
 * @anthor zcc
 * @time 2018/09/29
 * @class describe
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout card;
    public RecyclerView itemDataRC;
    public TextView itemDateTv;
    private AutoGridLayoutManager gridLayoutManager;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        card = (LinearLayout) itemView.findViewById(R.id.cardView);
        itemDataRC = (RecyclerView) itemView.findViewById(R.id.item_photo);
        itemDateTv = (TextView) itemView.findViewById(R.id.item_date);
        gridLayoutManager = new AutoGridLayoutManager(itemView.getContext(), 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setScrollEnabled(false);
        itemDataRC.setLayoutManager(gridLayoutManager);
        itemDataRC.addItemDecoration(new RecyclerItemDecoration(4, 5, true));
    }
}
