package com.gt.photopicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.gt.photopicker.widget.ViewPagerFixed;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

/**
 * 预览照片
 * Created by gt on 2015/8/25.
 */
public class PhotoPreviewActivity extends AppCompatActivity implements PhotoPagerAdapter.PhotoViewClickListener {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";

    /**
     * 预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<String> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_preview);

        initViews();

        paths = new ArrayList<>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        if (pathArr != null) {
            paths.addAll(pathArr);
        }

        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);

        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateActionBarTitle();
    }

    private void initViews() {
        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_photos);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.pickerToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    public void updateActionBarTitle() {
        getSupportActionBar().setTitle(
                getString(R.string.image_index, mViewPager.getCurrentItem() + 1, paths.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, paths);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (AntiShake.check(i, 500)) {
            return super.onOptionsItemSelected(item);
        }
        if (i == android.R.id.home) {
            onBackPressed();
        } else if (i == R.id.action_discard) {
            final int index = mViewPager.getCurrentItem();
            final String deletedPath = paths.get(index);
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.deleted_a_photo,
                    Snackbar.LENGTH_LONG);
            if (paths.size() <= 1) {
                // 最后一张照片弹出删除提示
                // show confirm dialog
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_to_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                paths.remove(index);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            } else {
                snackbar.show();
                paths.remove(index);
                mPagerAdapter.notifyDataSetChanged();
            }

            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paths.size() > 0) {
                        paths.add(index, deletedPath);
                    } else {
                        paths.add(deletedPath);
                    }
                    mPagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(index, true);
                }
            });
        } else if (i == R.id.action_edit) {
            currentPath = paths.get(mViewPager.getCurrentItem());
            if (FileUtils.getMIMEType(currentPath).contains("video")) {
                Toast.makeText(PhotoPreviewActivity.this, R.string.error_edit_video, Toast.LENGTH_LONG).show();
            } else {
                File photoFile = new File(currentPath);
                Uri imageUri = Uri.fromFile(photoFile);
                UCrop.Options options = new UCrop.Options();
                options.setToolbarColor(Color.parseColor("#ff212121"));
                options.setStatusBarColor(Color.parseColor("#ff000000"));
                options.setToolbarWidgetColor(Color.parseColor("#ffffffff"));
                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                        + File.separator + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
                UCrop.of(imageUri, Uri.fromFile(storageDir))
                        .withOptions(options)
                        .start(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    String currentPath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Collections.replaceAll(paths, currentPath, resultUri.getPath());
            mPagerAdapter.notifyDataSetChanged();
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, resultUri));
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
