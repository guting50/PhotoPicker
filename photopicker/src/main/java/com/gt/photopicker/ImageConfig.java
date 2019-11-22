package com.gt.photopicker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 读取手机照片的限制参数
 * Created by foamtrace on 2015/8/26.
 */
public class ImageConfig implements Parcelable {

    // 图片最小宽度
    public int minWidth;
    // 图片最小高度
    public int minHeight;
    // 图片大小，单位字节
    public long minSize;
    // 文件后缀: 例如 { image/jpeg, image/png, ... }
    public String[] mimeType;
    // 文件类型: 例如 {MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO, ... }
    public int[] mediaType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.minWidth);
        dest.writeInt(this.minHeight);
        dest.writeLong(this.minSize);
        dest.writeStringArray(this.mimeType);
        dest.writeIntArray(this.mediaType);
    }

    public ImageConfig() {
    }

    protected ImageConfig(Parcel in) {
        this.minWidth = in.readInt();
        this.minHeight = in.readInt();
        this.minSize = in.readLong();
        this.mimeType = in.createStringArray();
        this.mediaType = in.createIntArray();
    }

    public static final Creator<ImageConfig> CREATOR = new Creator<ImageConfig>() {
        @Override
        public ImageConfig createFromParcel(Parcel source) {
            return new ImageConfig(source);
        }

        @Override
        public ImageConfig[] newArray(int size) {
            return new ImageConfig[size];
        }
    };
}
