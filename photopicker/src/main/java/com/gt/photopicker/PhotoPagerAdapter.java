package com.gt.photopicker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.gt.photopicker.intent.PhotoPickerIntent;
import com.gt.utils.FileUtils;
import com.gt.utils.widget.OnNoDoubleClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }

    public PhotoViewClickListener listener;

    private List<String> paths = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public PhotoPagerAdapter(Context mContext, List<String> paths) {
        this.mContext = mContext;
        this.paths = paths;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_preview, container, false);

        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.iv_pager);
        ImageView mediaType = itemView.findViewById(R.id.media_type);

        final String path = paths.get(position);
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }
        mediaType.setVisibility(View.GONE);
        if (FileUtils.getMIMEType(path).contains("video")) {
            mediaType.setVisibility(View.VISIBLE);
            mediaType.setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(path);
                    Uri uri = Uri.fromFile(file);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        uri = FileProvider.getUriForFile(mContext, PhotoPickerIntent.fileProvider, file);
                    intent.setDataAndType(uri, "video/*");
                    mContext.startActivity(intent);
                }
            });
        }
        Glide.with(mContext)
                .load(uri)
//            .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .into(imageView);

        imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) {
                    listener.OnPhotoTapListener(view, x, y);
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
