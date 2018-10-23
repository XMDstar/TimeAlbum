package com.gengmei.albumlibrary.album.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.gengmei.albumlibrary.album.AlbumConfig;
import com.gengmei.albumlibrary.album.bean.MaterialBean;
import com.gengmei.albumlibrary.album.callback.RefreshCallBack;
import com.gengmei.albumlibrary.album.utils.AlbumUtils;
import com.gengmei.albumlibrary.album.view.photoview.PhotoView;


import java.io.File;
import java.util.List;

import com.gengmei.albumlibrary.R;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 预览页适配器
 */
public class PreviewPGAdapter extends PagerAdapter {
    private List<MaterialBean> images;
    private List<String> checkId;
    private RefreshCallBack callBack;
    private Context mContext;
    private int videoCheck;

    public PreviewPGAdapter(List<MaterialBean> images, Context context, List<String> checkId, RefreshCallBack callBack, int videoCheck) {
        super();
        this.images = images;
        this.mContext = context;
        this.checkId = checkId;
        this.callBack = callBack;
        this.videoCheck = videoCheck;
    }

    @Override
    public int getCount() {
        if (images != null) {
            return images.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_image_preview, container, false);
        final PLVideoTextureView videoView = (PLVideoTextureView) contentView.findViewById(R.id.preview_videoview);
        final PhotoView imageView = (PhotoView) contentView.findViewById(R.id.preview_img);
        final ImageView iv_play = (ImageView) contentView.findViewById(R.id.iv_play);
        final CheckBox check = (CheckBox) contentView.findViewById(R.id.preview_cb);
        final FrameLayout fl = (FrameLayout) contentView.findViewById(R.id.preview_fl);
        final MaterialBean media = images.get(position);
        if (media != null) {
            final boolean isVideo = media.getType() == 1 ? false : true;
            iv_play.setVisibility(isVideo ? View.VISIBLE : View.GONE);
            Glide.with(mContext).load(new File(media.getPath())).error(R.drawable.image_placeholder).fallback(R.drawable.image_placeholder).into(imageView);
            if (checkId.size() > 0 && checkId.contains(media.getName())) {
                check.setChecked(true);
            } else {
                check.setChecked(false);
            }
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (checkId.size() >= AlbumConfig.ALBUM_CHECK_MAX) {
                            check.setChecked(false);
                            Toast.makeText(mContext, "小主不要贪心哦，一次最多不超过50个哦～", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (videoCheck >= AlbumConfig.ALBUM_CHECK_VIDEO_MAX && isVideo) {
                            check.setChecked(false);
                            Toast.makeText(mContext, "一次上传视频最多不超过10个", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (isVideo && media.getFileSize() > AlbumConfig.VIDEO_SIZE_MAX) {
                            check.setChecked(false);
                            Toast.makeText(mContext, "视频最大不超过300MB", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!checkId.contains(media.getName())) {
                            if (isVideo) {
                                videoCheck = videoCheck + 1;
                            }
                            checkId.add(media.getName());
                            callBack.checkNews(media, true, media.getName());
                        }
                    } else {
                        if (checkId.contains(media.getName())) {
                            if (isVideo) {
                                videoCheck = videoCheck - 1;
                            }
                            checkId.remove(media.getName());
                            callBack.checkNews(media, false, media.getName());
                        }
                    }

                }
            });
            iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoPlay(videoView, media.getPath(), imageView, iv_play, fl);

                }
            });
            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView != null) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                            iv_play.setVisibility(View.VISIBLE);
                        } else {
                            iv_play.setVisibility(View.GONE);
                            videoView.start();
                        }
                    }
                }
            });

        }
        (container).addView(contentView, 0);
        return contentView;
    }

    private void videoPlay(final PLVideoTextureView videoView, final String path, final PhotoView imageView, final ImageView iv_play, final FrameLayout fl) {
        imageView.setVisibility(View.GONE);
        iv_play.setVisibility(View.GONE);
        fl.setVisibility(View.VISIBLE);
        videoView.stopPlayback();
        if (videoView != null) {
            videoView.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(PLMediaPlayer plMediaPlayer) {
                    videoView.start();
                }
            });
            videoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(PLMediaPlayer plMediaPlayer) {
                    imageView.setVisibility(View.VISIBLE);
                    iv_play.setVisibility(View.VISIBLE);
                    fl.setVisibility(View.GONE);
                }
            });
            videoView.setDisplayOrientation(PLVideoView.ASPECT_RATIO_ORIGIN);
            AVOptions options = new AVOptions();

            // the unit of timeout is ms
            options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
            // Some optimization with buffering mechanism when be set to 1
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);

            // whether start play automatically after prepared, default value is 1
            options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);

            videoView.setAVOptions(options);
            videoView.setVideoPath(path);
            if (videoView.getViewTreeObserver().isAlive()) {
                videoView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        imageView.setVisibility(View.VISIBLE);
                        iv_play.setVisibility(View.VISIBLE);
                        fl.setVisibility(View.GONE);
                        videoView.stopPlayback();
                    }
                });
            }
        }
    }

}
