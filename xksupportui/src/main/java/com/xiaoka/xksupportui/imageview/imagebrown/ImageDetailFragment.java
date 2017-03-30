package com.xiaoka.xksupportui.imageview.imagebrown;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.xiaoka.xksupportui.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDetailFragment extends Fragment {

    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });
        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setMessage("是否保存该图片").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         String path=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+System.currentTimeMillis() + ".jpg";
                        new Thread(() -> {
                            Bitmap bitmap = null;
                            try {
                                bitmap = Glide.with(getActivity())
                                        .load(mImageUrl)
                                        .asBitmap()
                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .get();
                                saveBitmap(bitmap,100,path,false);
                                showToast("图片成功保存到：" + path);


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }).start();

                    }
                })
                        .setNegativeButton("取消", null)
                        .show();
                return false;
            }
        });
        Glide.with(getActivity())
                .load(mImageUrl)
                .asBitmap()
//				.placeholder(R.drawable.)
//				.error(defaultDrawable)
//				.override(width, height)
//				.diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
//				.skipMemoryCache(true)
//				.into(mImageView);
                //.centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        mImageView.setImageBitmap(bitmap);
                        mAttacher.update();
                    }
                });

    }

    public void showToast(String msg) {
        Looper.prepare();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public static void saveBitmap(Bitmap src, int quality, String savePath,
                                  boolean isRecycle) {
        try {
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }
            src.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(
                    savePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (isRecycle)
                if (null != src) {
                    src.recycle();
                    src = null;
                }
        }
    }
}
