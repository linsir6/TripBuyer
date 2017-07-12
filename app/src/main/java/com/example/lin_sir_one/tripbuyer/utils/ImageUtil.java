package com.example.lin_sir_one.tripbuyer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.lin_sir_one.tripbuyer.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by linSir on 16/7/21.处理图片的工具类
 */
public class ImageUtil {

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    /**
     * 将图片处理成圆形
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int r;

        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        Bitmap backgroundBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        // 设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        // 宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        // 且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉

        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);

        return backgroundBmp;
    }

    /**
     * 将url的图片加载到指定 ImageView 中
     */
    public static void requestImg(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(R.color.grey_300)
                .bitmapTransform(new ImageTransformation(context))
                .error(R.color.red_300)
                .into(view);
    }

    public static void requestImg2(Context context, Integer resourceId, ImageView view) {
        Glide.with(context)
                .load(resourceId)
                .placeholder(R.color.grey_300)
                .bitmapTransform(new ImageTransformation(context))
                .error(R.color.red_300)
                .into(view);
    }


    /**
     * 将url编程圆形的加载在 ImageView中
     */

    public static void requestCircleImg(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public static void requestImgFromBytes(Context context, byte[] bytes, ImageView view) {
        Glide.with(context)
                .load(bytes)
                .placeholder(R.color.grey_300)
                .error(R.color.red_300)
                .into(view);
    }

    public static class ImageTransformation implements Transformation<Bitmap> {
//        private Context mContext;

        private static Paint mMaskingPaint = new Paint();
        private BitmapPool mBitmapPool;

        public ImageTransformation(Context context) {
            this(context, Glide.get(context).getBitmapPool());
        }

        public ImageTransformation(Context context, BitmapPool pool) {
            mBitmapPool = pool;
//            mContext = context.getApplicationContext();
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {

            Bitmap source = resource.get();

            int width = source.getWidth();
            int height = source.getHeight();

            Bitmap result = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(source, 0, 0, mMaskingPaint);
            canvas.drawColor(0x44000000);

            return BitmapResource.obtain(result, mBitmapPool);
        }

        @Override
        public String getId() {
            return "MaskTransformation(maskId)";
        }
    }


}


