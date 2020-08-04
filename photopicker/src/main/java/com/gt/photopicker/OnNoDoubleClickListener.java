package com.gt.photopicker;

import android.view.View;

import java.util.Calendar;

/**
 * 防止点击事件重复调用
 * Created by Administrator on 2017/7/7.
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    private int time;
    private long lastClickTime = 0;

    public OnNoDoubleClickListener() {
        this(1000);
    }

    public OnNoDoubleClickListener(int time) {
        this.time = time;
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > time) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
