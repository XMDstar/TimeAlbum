package com.gengmei.albumlibrary.album.bean;

import java.util.List;

/**
 * @anthor zcc
 * @time 2018/09/29
 * @class 按日期排序实体类
 */
public class AlbumData {
    private String date;
    private List<MaterialBean> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<MaterialBean> getList() {
        return list;
    }

    public void setList(List<MaterialBean> list) {
        this.list = list;
    }
}
