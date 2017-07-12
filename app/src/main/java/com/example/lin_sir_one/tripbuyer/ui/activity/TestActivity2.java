package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lin_sir_one.tripbuyer.R;
import com.socks.library.KLog;

import java.io.FileNotFoundException;

public class TestActivity2 extends Activity {
    private ImageView bigIv;
    private Button bt;
    private int Width, Height, ImWidth, ImHeight;//获取屏幕的高度和宽度以及图片的高度和宽度

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        bigIv = (ImageView) findViewById(R.id.big_iv);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);

                intent.setType("image/*");

                intent.putExtra("crop", "true");

                intent.putExtra("aspectX", 2);

                intent.putExtra("aspectY", 1);

                intent.putExtra("outputX", 600);

                intent.putExtra("outputY", 300);

                intent.putExtra("scale", true);

                intent.putExtra("return-data", true);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                intent.putExtra("noFaceDetection", true); // no face detection

                startActivityForResult(intent, 105);
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 105:

                Log.d("lin", "----lin---->CHOOSE_BIG_PICTURE: data = " + data);//it seems to be null
                KLog.i("----lin---->" + imageUri);
                if (imageUri != null) {

                    Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                    KLog.i("----lin---->" + bitmap);
                    bigIv.setImageBitmap(bitmap);

                }

                break;

            default:
                break;
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}