package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by linSir on 16/7/31.,
 */

public class TestActivity extends Activity {
    private Button mButton1;
    private Button mButton2;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mButton1 = (Button) findViewById(R.id.button1);
        mImage = (ImageView) findViewById(R.id.image_show);



        mButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转至拍照界面
                Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                File out = new File(getPhotopath());
                KLog.i("----lin---->" + getPhotopath());
                Uri uri = Uri.fromFile(out);
                // 获取拍照后未压缩的原图片，并保存在uri路径中
                intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                intentPhote.putExtra(MediaStore.Images.Media.ORIENTATION, 180);
                startActivityForResult(intentPhote, 2000);
            }
        });


//        mButton2.setOnClickListener(new OnClickListener() {
//            @Override public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                File out = new File(getPhotopath());
//                Uri uri = Uri.fromFile(out);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    startActivityForResult(intent, 103);
//                } else {
//                    startActivityForResult(intent, 102);
//                }
//            }
//        });


    }

    /**
     * 获取原图片存储路径
     *
     * @return
     */
    private String getPhotopath() {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory() + "/mymy/";
        String imageName = "imageTest.jpg";
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        return fileName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = getBitmapFromUrl(getPhotopath(), 617, 924);
            saveScalePhoto(bitmap);
            mImage.setImageBitmap(bitmap);

            final AVFile file = new AVFile("imageTest.jpg", ImageUtil.bitmap2Bytes(bitmap));

            Log.i("----lin----", "----lin----" + bitmap);

            file.saveInBackground(new SaveCallback() {
                @Override public void done(AVException e) {
                    //Log.i("----lin----", "----lin----" + e.toString());
                    Log.i("----lin----", "----lin----" + file.getUrl());


                }
            });


        }

        if (requestCode == 103) {


        }

        if (requestCode == 102) {


        }


    }

    /**
     * 根据路径获取图片资源（已缩放）
     *
     * @param url    图片存储路径
     * @param width  缩放的宽度
     * @param height 缩放的高度
     * @return
     */
    private Bitmap getBitmapFromUrl(String url, double width, double height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = 1;
        float scaleHeight = 1;
//        try {
//            ExifInterface exif = new ExifInterface(url);
//            String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 按照固定宽高进行缩放
        // 这里希望知道照片是横屏拍摄还是竖屏拍摄
        // 因为两种方式宽高不同，缩放效果就会不同
        // 这里用了比较笨的方式
        if (mWidth <= mHeight) {
            scaleWidth = (float) (width / mWidth);
            scaleHeight = (float) (height / mHeight);
        } else {
            scaleWidth = (float) (height / mWidth);
            scaleHeight = (float) (width / mHeight);
        }
//        matrix.postRotate(90); /* 翻转90度 */
        // 按照固定大小对图片进行缩放
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
        // 用完了记得回收
        bitmap.recycle();
        return newBitmap;
    }

    /**
     * 存储缩放的图片
     *
     * @parama 图片数据
     */
    private void saveScalePhoto(Bitmap bitmap) {
        // 照片全路径
        String fileName = "";
        // 文件夹路径
        String pathUrl = Environment.getExternalStorageDirectory().getPath() + "/mymy/";
        String imageName = "imageScale.jpg";
        FileOutputStream fos = null;
        File file = new File(pathUrl);
        file.mkdirs();// 创建文件夹
        fileName = pathUrl + imageName;
        try {
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}