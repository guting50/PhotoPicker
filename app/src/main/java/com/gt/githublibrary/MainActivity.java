package com.gt.githublibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.gt.photopicker.PhotoPickerActivity;
import com.gt.photopicker.SelectModel;
import com.gt.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        findViewById(R.id.rootView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true);
                //方式一
//                startActivityForResult(intent, 999);
                //方式二
                intent.gotoPhotoPickerActivity(MainActivity.this, new PhotoPickerActivity.OnSelectedCallbackListener() {
                    @Override
                    public void onSelectedCallback(ArrayList<String> resultList) {
                        for (String str : resultList) {
                            Log.e("imgPath", str);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> imgPaths = new ArrayList<>();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 999:
                    imgPaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    for (String str : imgPaths) {
                        Log.e("imgPath", str);
                    }
                    break;
            }
        }
    }
}
