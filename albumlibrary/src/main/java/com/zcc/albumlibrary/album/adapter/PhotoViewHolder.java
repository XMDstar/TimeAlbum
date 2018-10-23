package com.zcc.albumlibrary.album.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcc.albumlibrary.R;
import com.zcc.albumlibrary.album.view.SquareImg;


/**
 * @anthor zcc
 * @time 2018/09/29
 * @class describe
 */
public class PhotoViewHolder extends RecyclerView.ViewHolder {
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
