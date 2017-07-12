package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.utils.FileUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/26.身份认证界面
 */
public class AuthenticationActivity extends AppCompatActivity {

    private static final String IMAGE_NAME = "user_avatar.jpg";
    private static final int GALLERY_REQUEST = 102;
    private static final int GALLERY_KITKAT_REQUEST = 103;
    private static final int CAMERA_REQUEST = 104;
    private static final int RESULT = 105;
    private static final int RESULT_CANCELED = 0;
    private Bitmap avatarBmp;
    private Dialog dialog_shen_fen_zheng;
    private Dialog dialog_hu_zhao;


    String _name, _card, _cardView;


    @BindView(R.id.name_online_pay) EditText name;
    @BindView(R.id.card_online_pay) EditText card;
    @BindView(R.id.add_online_pay) ImageView add;
    @BindView(R.id.card_view) ImageView cardView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
    }


    /**
     * 点击回退按钮
     */
    @OnClick(R.id.back_online_pay)
    public void back() {
        Log.i("lin", "-----lin----->   back ");
        finish();
    }

    /**
     * 点击添加按钮
     */
    @OnClick(R.id.add_online_pay)
    public void add() {
        chooseImgDialog();
    }

    @OnClick(R.id.finish_online_pay)
    public void finish2() {

        _name = name.getText().toString().trim();
        _card = card.getText().toString().trim();

        if (_name == null) {
            Toast.makeText(AuthenticationActivity.this, "输入的姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_card == null) {
            Toast.makeText(AuthenticationActivity.this, "输入的身份证号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_cardView == null) {
            Toast.makeText(AuthenticationActivity.this, "身份证的图片不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpResultListener<Boolean> listener = new HttpResultListener<Boolean>() {
            @Override public void onSuccess(Boolean aBoolean) {
                Log.i("lin", "-----lin----->  successed");
                Toast.makeText(AuthenticationActivity.this, "身份信息验证提交成功，请等待 ", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override public void onError(Throwable e) {
                Log.i("lin", "-----lin----->  onFail" + e.toString());
                Toast.makeText(AuthenticationActivity.this, "身份信息验证失败，请重新尝试", Toast.LENGTH_SHORT).show();
            }
        };
        A.getA(1, 0, 1).certification(listener, _name, _card, _cardView, "");
    }


    /**
     * 选择商品图片的对话框
     */
    private void chooseImgDialog() {
        new AlertDialog.Builder(AuthenticationActivity.this)
                .setTitle(getResources().getString(R.string.text_choose_image))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.text_img_album), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromGallery();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.text_img_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromCamera();
                    }
                }).show();
    }

    /**
     * 启动手机相册
     */
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, GALLERY_KITKAT_REQUEST);
        } else {
            startActivityForResult(intent, GALLERY_REQUEST);
        }

//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, 103);103
    }

    /**
     * 启动手机相机拍摄照片作为头像
     */
    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (FileUtil.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Log.i("sys", "--lin--> SD not exist");
        }
    }

    /**
     * 返回结果处理，这里需要注意resultCode，正常情况返回值为 -1 没有任何操作直接后退则返回 0
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 用户进行了有效的操作，结果码不等于取消码的时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case GALLERY_REQUEST:                       //从相册选择
                    cropPhoto(data.getData());
                    break;
                case GALLERY_KITKAT_REQUEST:                //从相册选择,兼容版本
                    cropPhoto(data.getData());
                    break;
                case CAMERA_REQUEST://从拍照选择
                    if (FileUtil.hasSdcard()) {
                        cropPhoto(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));

                    } else {
                        Log.i("sys", "--tc-->EditUserInfo no sd card found");
                    }
                    break;
                case RESULT://选择完成，将头像放在ImageView中
                    if (data != null) {
                        avatarBmp = data.getExtras().getParcelable("data");
                        updateUserInfo(avatarBmp);
                        cardView.setImageBitmap(avatarBmp);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪裁照片
     */
    public void cropPhoto(Uri uri) {
        if (uri == null) {
            Log.i("sys", "--tc--> The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(AuthenticationActivity.this, uri);
            if (url != null) {
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                Log.i("sys", "--tc-->EditUserInfo cropPhoto url is null");
            }
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 210);
        intent.putExtra("aspectY", 130);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 130);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT);
    }

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 上传用户信息，首先上传头像，上传成功后赶回头像地址，然后上传其他信息
     */


    private void updateUserInfo(Bitmap avatar) {

        //如果头像为空，也就是用户没有上传头像，则使用之前的头像地址
        if (avatar == null) {

        } else {
            final AVFile avatarFile = new AVFile("user_avatar.jpeg", ImageUtil.bitmap2Bytes(avatar));
            avatarFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {

                        Log.i("lin", "----lin---->  imgUrl" + avatarFile.getUrl());
                        _cardView = avatarFile.getUrl();

                    }
                }
            });
        }
    }

    private void showShenFenZheng(final Context mContext) {

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_shen_fen_zheng, null);
        builder.setView(mView);
        builder.setCancelable(true);
        dialog_shen_fen_zheng = builder.create();
        dialog_shen_fen_zheng.show();

    }

    @OnClick(R.id.iv_shen_fen_zheng)
    public void shen_fen_zheng() {
        showShenFenZheng(AuthenticationActivity.this);
    }

    private void showHuZhao(final Context mContext) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_shen_fen_zheng, null);
        builder.setView(mView);
        builder.setCancelable(true);
        dialog_hu_zhao = builder.create();
        dialog_hu_zhao.show();
    }

    @OnClick(R.id.iv_hu_zhao)
    public void hu_zhao() {
        showHuZhao(AuthenticationActivity.this);
    }


}








