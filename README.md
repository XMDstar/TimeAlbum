# TimeAlbum
This is a photo album.
主要实现功能如下：
1.按时间分类显示 
2.时间条目滑动吸顶 
3.可同时选中多个视频与图片，数量可动态控制
4.可按照时间以及选中顺序拆分 
5.视频太大不可选置灰 
6.限定总数或者视频数已经达到置灰未选项或置灰未选视频项
7.可以预览视频以及图片并选中或取消
等等。。。

使用方式：
跳转参数可传可不传
 Bundle bundle = new Bundle();
 bundle.putInt(AlbumConfig.MAX_NUMBER, TextUtils.isEmpty(etMax.getText().toString()) ? 9 : Integer.parseInt(etMax.getText().toString()));
 bundle.putInt(AlbumConfig.VIDEO_NUMBER, TextUtils.isEmpty(etVideoMax.getText().toString()) ? 1 : Integer.parseInt(etVideoMax.getText().toString()));
 bundle.putString(AlbumConfig.IS_SPLIT, AlbumConfig.YES_SHOW_DIALOG);
 startActivityForResult(new Intent(MainActivity.this, TimeAlbumActivity.class).putExtras(bundle), AlbumConfig.ALBUM_REQUESTCODE);

返回结果获取
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AlbumConfig.ALBUM_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            //list为选中视频=图片集合
            List<MaterialBean> list = data.getParcelableArrayListExtra(AlbumConfig.RESULT_TIMEALBUM_DATA);
        }
    }
