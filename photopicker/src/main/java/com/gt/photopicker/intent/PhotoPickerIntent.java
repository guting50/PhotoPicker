package com.gt.photopicker.intent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gt.photopicker.ImageConfig;
import com.gt.photopicker.PhotoPickerActivity;
import com.gt.photopicker.SelectModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * 选择照片
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPickerIntent extends Intent {

    public static String fileProvider;

    public PhotoPickerIntent(Context packageContext) {
        super(packageContext, PhotoPickerActivity.class);
        fileProvider = packageContext.getPackageName() + ".photopicker.fileprovider";
    }

    /**
     * 是否显示拍照按钮 默认不显示
     *
     * @param bool
     */
    public void setShowCarema(boolean bool) {
        this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, bool);
    }

    /**
     * 设置选择图片数量 默认9张
     *
     * @param total
     */
    public void setMaxTotal(int total) {
        this.putExtra(PhotoPickerActivity.EXTRA_SELECT_COUNT, total);
    }

    /**
     * 选择模式 默认单张
     *
     * @param model
     */
    public void setSelectModel(SelectModel model) {
        this.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, Integer.parseInt(model.toString()));
    }

    /**
     * 默认已选择的照片地址（做回显用）
     *
     * @param imagePathis
     */
    public void setSelectedPaths(ArrayList<String> imagePathis) {
        this.putStringArrayListExtra(PhotoPickerActivity.EXTRA_DEFAULT_SELECTED_LIST, imagePathis);
    }

    /**
     * 显示相册图片的属性（详见 ImageConfig）
     *
     * @param config
     */
    public void setImageConfig(ImageConfig config) {
        this.putExtra(PhotoPickerActivity.EXTRA_IMAGE_CONFIG, config);
    }

    public synchronized void gotoPhotoPickerActivity(Context context, PhotoPickerActivity.OnSelectedCallbackListener listener) {
        long key = new Date().getTime();
        PhotoPickerActivity.callbackListeners.put(key, listener);
        this.putExtra("key", key);
        context.startActivity(this);
        try {//增加一个线程阻塞，确保每次生成的key都不相同
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
