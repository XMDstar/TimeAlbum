package com.gengmei.albumlibrary.album.callback;

import com.gengmei.albumlibrary.album.bean.MaterialBean;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 预览页图片，视频选择回调
 */
public interface RefreshCallBack {
    void checkNews(MaterialBean media, boolean type, String name);
}
