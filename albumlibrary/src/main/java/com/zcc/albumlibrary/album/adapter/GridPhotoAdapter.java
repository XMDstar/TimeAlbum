package com.zcc.albumlibrary.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.zcc.albumlibrary.R;
import com.zcc.albumlibrary.album.AlbumConfig;
import com.zcc.albumlibrary.album.bean.MaterialBean;
import com.zcc.albumlibrary.event.AlbumEventCode;
import com.zcc.albumlibrary.event.AlbumMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 相册页item RecyclerView适配器
 */
public class GridPhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private List<MaterialBean> materialBeans;
    private Context mContext;
    private List<String> checkId;
    private boolean isRefresh = false, videoGray = false;

    public GridPhotoAdapter(List<MaterialBean> materialBeans, Context mContext, List<String> checkId) {
        this.materialBeans = materialBeans;
        this.mContext = mContext;
        this.checkId = checkId;
    }

    public void setRefresh(boolean refresh, boolean videoGray) {
        this.isRefresh = refresh;
        this.videoGray = videoGray;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_album_img, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        holder.cbPhoto.setVisibility(View.VISIBLE);
        holder.ivGray.setVisibility(View.GONE);
        holder.tvTime.setVisibility(View.GONE);
        final MaterialBean bean = materialBeans.get(position);
        if (bean != null) {
            if (bean.getType() == 2) {
                Glide.with(mContext).load(new File(bean.getPath())).error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder).fallback(R.drawable.image_placeholder).into(holder.img);
                holder.tvTime.setText(bean.getFormatDuration());
                holder.tvTime.setVisibility(View.VISIBLE);
                if (bean.getFileSize() > AlbumConfig.VIDEO_SIZE_MAX) {
                    holder.ivGray.setVisibility(View.VISIBLE);
                }
            } else if (bean.getType() == 3) {
                holder.img.setImageResource(R.mipmap.album_camera_icom);
                holder.tvTime.setText("");
                holder.cbPhoto.setVisibility(View.GONE);
            } else {
                Glide.with(mContext).load(new File(bean.getPath())).error(R.drawable.image_placeholder)
                        .placeholder(R.drawable.image_placeholder).fallback(R.drawable.image_placeholder).into(holder.img);
                holder.tvTime.setText("");
            }
            holder.cbPhoto.setOnCheckedChangeListener(null);
            if (checkId!=null&&checkId.size() > 0 && checkId.contains(bean.getName())) {
                holder.cbPhoto.setChecked(true);
                holder.ivGray.setVisibility(View.GONE);
            } else {
                holder.cbPhoto.setChecked(false);
                if (bean.getType() == 2 && videoGray) {
                    holder.ivGray.setVisibility(View.VISIBLE);
                }
                if (isRefresh) {
                    holder.ivGray.setVisibility(View.VISIBLE);
                    if (bean.getType() == 3) {
                        holder.ivGray.setVisibility(View.GONE);
                    }
                }
            }
            holder.cbPhoto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.ivGray.getVisibility() == View.VISIBLE) {
                        holder.cbPhoto.setChecked(false);
                        if (isRefresh) {
                            Toast.makeText(mContext, "小主不要贪心哦，一次最多不超过" + AlbumConfig.ALBUM_CHECK_MAX + "个哦～", Toast.LENGTH_SHORT).show();
                        } else {
                            if (videoGray) {
                                Toast.makeText(mContext, "一次上传视频最多不超过" + AlbumConfig.ALBUM_CHECK_VIDEO_MAX + "个", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "视频最大不超过300MB", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (isChecked) {
                            checkId.add(bean.getName());
                            EventBus.getDefault().post(new AlbumMessageEvent(AlbumEventCode.ALBUM_CHECK, bean));
                        } else {
                            checkId.remove(bean.getName());
                            EventBus.getDefault().post(new AlbumMessageEvent(AlbumEventCode.ALBUM_CHECK_NO, bean));
                        }
                    }
                }
            });
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new AlbumMessageEvent(AlbumEventCode.ALBUM_SHOW_DETAIL, bean));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (materialBeans != null) {
            return materialBeans.size();
        } else return 0;
    }
}
