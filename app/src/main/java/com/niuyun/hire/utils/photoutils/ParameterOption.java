package com.niuyun.hire.utils.photoutils;

/**
 * Created by chen.zhiwei on 2017-8-2.
 */

public class ParameterOption {
    private int Type = 1;//0为拍照获取，1为从相册获取
    private int number = 1;//图片数量
    private int takeFrom = 0;//从那里获取图片，0为从相册，1为从文件中
    private boolean isNeedCrop = false;//是否需要裁剪，0是，1否
    private boolean isNeedCompress = false;//是否需要压缩，0是，1否
    /**
     * 如果需要裁剪，配置尺寸大小
     */
    private int height = 800;
    private int width = 800;
    private boolean isUseTakePhotoCrop;//裁剪工具是否用takephoto自带的

    /**
     * 如果需要压缩，配置尺寸大小
     */
    private int compressHeight = 800;
    private int compressWidth = 800;
    private boolean isUseSystem = true;//压缩工具是否使用系统自带的，否的话会用luban压缩；
    private int compressMaxSize;//图片压缩后的大小
    private boolean isShowCompressProgressBar = false;//是否显示压缩进度条

    private boolean isSafeOrigin=false;//拍照压缩后是否保存原图

    private boolean isCorrectTPDirection = true;//是否纠正拍照的照片的旋转角度
    private boolean isUseTPAlbun = false;//是否用takephoto的自带相册

    private String pickByTakeOrAlbum = "1";//拍照，还是从相册取，0是拍照，1是从相册取

    public ParameterOption(int number, String pickByTakeOrAlbum) {
        this.number = number;
        this.pickByTakeOrAlbum = pickByTakeOrAlbum;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTakeFrom() {
        return takeFrom;
    }

    public void setTakeFrom(int takeFrom) {
        this.takeFrom = takeFrom;
    }

    public boolean isNeedCrop() {
        return isNeedCrop;
    }

    public void setNeedCrop(boolean needCrop) {
        isNeedCrop = needCrop;
    }

    public boolean isNeedCompress() {
        return isNeedCompress;
    }

    public void setNeedCompress(boolean needCompress) {
        isNeedCompress = needCompress;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isUseTakePhotoCrop() {
        return isUseTakePhotoCrop;
    }

    public void setUseTakePhotoCrop(boolean useTakePhotoCrop) {
        isUseTakePhotoCrop = useTakePhotoCrop;
    }

    public int getCompressHeight() {
        return compressHeight;
    }

    public void setCompressHeight(int compressHeight) {
        this.compressHeight = compressHeight;
    }

    public int getCompressWidth() {
        return compressWidth;
    }

    public void setCompressWidth(int compressWidth) {
        this.compressWidth = compressWidth;
    }

    public boolean isUseSystem() {
        return isUseSystem;
    }

    public void setUseSystem(boolean useSystem) {
        isUseSystem = useSystem;
    }

    public int getCompressMaxSize() {
        return compressMaxSize;
    }

    public void setCompressMaxSize(int compressMaxSize) {
        this.compressMaxSize = compressMaxSize;
    }

    public boolean isShowCompressProgressBar() {
        return isShowCompressProgressBar;
    }

    public void setShowCompressProgressBar(boolean showCompressProgressBar) {
        isShowCompressProgressBar = showCompressProgressBar;
    }

    public boolean isSafeOrigin() {
        return isSafeOrigin;
    }

    public void setSafeOrigin(boolean safeOrigin) {
        isSafeOrigin = safeOrigin;
    }

    public boolean isCorrectTPDirection() {
        return isCorrectTPDirection;
    }

    public void setCorrectTPDirection(boolean correctTPDirection) {
        isCorrectTPDirection = correctTPDirection;
    }

    public boolean isUseTPAlbun() {
        return isUseTPAlbun;
    }

    public void setUseTPAlbun(boolean useTPAlbun) {
        isUseTPAlbun = useTPAlbun;
    }

    public String getPickByTakeOrAlbum() {
        return pickByTakeOrAlbum;
    }

    public void setPickByTakeOrAlbum(String pickByTakeOrAlbum) {
        this.pickByTakeOrAlbum = pickByTakeOrAlbum;
    }

    public ParameterOption() {
    }

    public ParameterOption(int type, int number, int takeFrom, boolean isNeedCrop, boolean isNeedCompress, int height, int width, boolean isUseTakePhotoCrop, int compressHeight, int compressWidth, boolean isUseSystem, int compressMaxSize, boolean isShowCompressProgressBar, boolean isSafeOrigin, boolean isCorrectTPDirection, boolean isUseTPAlbun, String pickByTakeOrAlbum) {
        Type = type;
        this.number = number;
        this.takeFrom = takeFrom;
        this.isNeedCrop = isNeedCrop;
        this.isNeedCompress = isNeedCompress;
        this.height = height;
        this.width = width;
        this.isUseTakePhotoCrop = isUseTakePhotoCrop;
        this.compressHeight = compressHeight;
        this.compressWidth = compressWidth;
        this.isUseSystem = isUseSystem;
        this.compressMaxSize = compressMaxSize;
        this.isShowCompressProgressBar = isShowCompressProgressBar;
        this.isSafeOrigin = isSafeOrigin;
        this.isCorrectTPDirection = isCorrectTPDirection;
        this.isUseTPAlbun = isUseTPAlbun;
        this.pickByTakeOrAlbum = pickByTakeOrAlbum;
    }
}
