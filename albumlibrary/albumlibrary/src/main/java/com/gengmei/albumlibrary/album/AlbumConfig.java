package com.gengmei.albumlibrary.album;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 相册配置类
 */
public class AlbumConfig {
    //限定最大选择个数 视频+图片
    public static int ALBUM_CHECK_MAX = 50;
    //限定最大选择个数 视频
    public static int ALBUM_CHECK_VIDEO_MAX = 10;
    //限定最大可选视频大小
    public static final int VIDEO_SIZE_MAX = 300 * 1024 * 1024;
    public static final int ALBUM_REQUESTCODE = 996;
    //拆分规则 正常拆分
    public static final String DEFAULT_SPLIT = "default";
    //拆分规则 按照时间拆分
    public static final String TIME_SPLIT = "time";
    //添加进入相机位
    public static final String ADD_CAMERA = "camera";
    //是否显示弹窗提示用户选择拆分规则
    public static final String IS_SPLIT = "is_show_dialog";
    public static final String NO_SHOW_DIALOG = "no";
    public static final String YES_SHOW_DIALOG = "yes";
    //key 限定最大选择视频个数
    public static final String VIDEO_NUMBER = "video_number";
    //key 限定最大选择个数
    public static final String MAX_NUMBER = "max_number";
    //key 是否按照时间拆分
    public static final String RESULT_IS_SPLIT_TIME = "is_split_time";
    //key 返回已选择数据
    public static final String RESULT_TIMEALBUM_DATA = "timealbum";

}
