package com.gt.photopicker;

/**
 * 照片选择类型
 * Created by foamtrace on 2015/8/25.
 */
public enum SelectModel {
    SINGLE(PhotoPickerActivity.MODE_SINGLE), //单张
    MULTI(PhotoPickerActivity.MODE_MULTI); // 多张

    private int model;

    SelectModel(int model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return String.valueOf(this.model);
    }
}
