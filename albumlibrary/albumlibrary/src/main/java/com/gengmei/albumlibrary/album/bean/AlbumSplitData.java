package com.gengmei.albumlibrary.album.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @anthor zcc
 * @time 2018/09/30
 * @class 已拆分数据实体类
 */
public class AlbumSplitData implements Parcelable {
    private int id;
    private long time;
    private ArrayList<MaterialBean> data;

    public AlbumSplitData(int id, ArrayList<MaterialBean> data, long time) {
        this.id = id;
        this.data = data;
        this.time = time;
    }

    public AlbumSplitData() {

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<MaterialBean> getData() {
        return data;
    }

    public void setData(ArrayList<MaterialBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(data);
        dest.writeLong(time);
    }

    protected AlbumSplitData(Parcel in) {
        id = in.readInt();
        data = in.createTypedArrayList(MaterialBean.CREATOR);
        time = in.readLong();
    }

    public static final Creator<AlbumSplitData> CREATOR = new Creator<AlbumSplitData>() {
        @Override
        public AlbumSplitData createFromParcel(Parcel in) {
            return new AlbumSplitData(in);
        }

        @Override
        public AlbumSplitData[] newArray(int size) {
            return new AlbumSplitData[size];
        }
    };

}
