package com.zcc.albumlibrary.event;


public class AlbumMessageEvent<T> {
    private int code;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AlbumMessageEvent(int code, T data) {
        this.code = code;

        this.data = data;
    }
}
