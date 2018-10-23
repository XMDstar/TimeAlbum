package com.zcc.albumlibrary.album.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 图片，视频实体类
 */
public class MaterialBean implements Parcelable {
    private String path;
    private String formatDate;
    private long id;
    private String discr;
    private String name;
    private int type;//1.img   2.video 3.进入相机 4.添加图片
    private long fileSize;
    private long date;
    private long duration;//时长
    private String formatDuration;
    private boolean isCheck;
    private String url;

    public MaterialBean() {

    }

    public MaterialBean(String path, String formatDate, long id, String discr, String name, int type, long fileSize, long date, long duration, String formatDuration) {
        this.path = path;
        this.formatDate = formatDate;
        this.id = id;
        this.discr = discr;
        this.name = name;
        this.type = type;
        this.fileSize = fileSize;
        this.date = date;
        this.duration = duration;
        this.formatDuration = formatDuration;
        isCheck = false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDiscr() {
        return discr;
    }

    public void setDiscr(String discr) {
        this.discr = discr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFormatDuration() {
        return formatDuration;
    }

    public void setFormatDuration(String formatDuration) {
        this.formatDuration = formatDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.formatDate);
        dest.writeLong(this.id);
        dest.writeString(this.discr);
        dest.writeString(this.name);
        dest.writeInt(this.type);
        dest.writeLong(this.fileSize);
        dest.writeLong(this.date);
        dest.writeLong(this.duration);
        dest.writeString(this.formatDuration);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);

    }

    protected MaterialBean(Parcel in) {
        this.path = in.readString();
        this.formatDate = in.readString();
        this.id = in.readLong();
        this.discr = in.readString();
        this.name = in.readString();
        this.type = in.readInt();
        this.fileSize = in.readLong();
        this.date = in.readLong();
        this.duration = in.readLong();
        this.formatDuration = in.readString();
        this.isCheck = in.readByte() != 0;
        this.url = in.readString();
    }

    public static final Parcelable.Creator<MaterialBean> CREATOR = new Parcelable.Creator<MaterialBean>() {
        @Override
        public MaterialBean createFromParcel(Parcel source) {
            return new MaterialBean(source);
        }

        @Override
        public MaterialBean[] newArray(int size) {
            return new MaterialBean[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MaterialBean) {
            MaterialBean bean = (MaterialBean) obj;
            return this.getName().equals(bean.getName());
        }
        return super.equals(obj);
    }
}
