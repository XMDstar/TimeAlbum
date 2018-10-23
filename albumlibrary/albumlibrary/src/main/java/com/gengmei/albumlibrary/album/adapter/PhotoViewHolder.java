package com.gengmei.albumlibrary.album.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gengmei.albumlibrary.album.view.SquareImg;
import com.gengmei.albumlibrary.R;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class describe
 */
class PhotoViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout card;
    public SquareImg img;
    public TextView tvTime;
    public CheckBox cbPhoto;
    public SquareImg ivGray;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        card = (RelativeLayout) itemView.findViewById(R.id.rl_photo);
        img = (SquareImg) itemView.findViewById(R.id.iv_photo);
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        cbPhoto = (CheckBox) itemView.findViewById(R.id.cb_photo);
        ivGray = (SquareImg) itemView.findViewById(R.id.iv_gray);
    }
}
