package com.zcc.albumlibrary.album.view;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 提供回调的接口
 * Created by zcc on 2017/3/15.
 */

public abstract class VideoPlayerListener implements IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}
