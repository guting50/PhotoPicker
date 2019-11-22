package com.gt.githublibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.view.View;

import com.gt.photopicker.ImageConfig;
import com.gt.photopicker.PhotoPickerActivity;
import com.gt.photopicker.SelectModel;
import com.gt.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;

public class MainActivity extends Activity {
    AppCompatTextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        textView = findViewById(R.id.text_path);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(textView.getText().toString() + "\n" + "<<<<<<<< img >>>>>>>>");
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
                            textView.setText(textView.getText().toString() + "\n" + resultList.lastIndexOf(str) + "：" + str);
                        }
                        textView.setText(textView.getText().toString() + "\n");
                    }
                });
            }
        });

        findViewById(R.id.img_and_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(textView.getText().toString() + "\n" + "<<<<<<<< img_and_video >>>>>>>>");
                PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true);
                ImageConfig config = new ImageConfig();
                config.mimeType = new String[]{"image/jpg", "image/jpeg", "image/png", "video/mp4"};
                config.mediaType = new int[]{MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO};
                intent.setImageConfig(config);
                //方式一
//                startActivityForResult(intent, 999);
                //方式二
                intent.gotoPhotoPickerActivity(MainActivity.this, new PhotoPickerActivity.OnSelectedCallbackListener() {
                    @Override
                    public void onSelectedCallback(ArrayList<String> resultList) {
                        for (String str : resultList) {
                            textView.setText(textView.getText().toString() + "\n" + resultList.lastIndexOf(str) + "：" + str);
                        }
                        textView.setText(textView.getText().toString() + "\n");
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
                        textView.setText(textView.getText().toString() + "\n" + imgPaths.lastIndexOf(str) + "：" + str);
                    }
                    textView.setText(textView.getText().toString() + "\n");
                    break;
            }
        }
    }
}
