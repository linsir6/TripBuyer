package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.annotation.SuppressLint;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.model.UserInfoModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.utils.FileUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.example.lin_sir_one.tripbuyer.utils.MD5;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/17.设置界面,
 */
public class SetActivity extends AppCompatActivity {

    private Dialog setLoginPwdDialog;
    private Dialog setuserNameDialog;
    private Dialog setPayPwdDialog;
    private Dialog modifyPwdDialog;

    private static final String IMAGE_NAME = "user_avatar.jpg";
    private static final int GALLERY_REQUEST = 102;
    private static final int GALLERY_KITKAT_REQUEST = 103;
    private static final int CAMERA_REQUEST = 104;
    private static final int RESULT = 105;
    private static final int RESULT_CANCELED = 0;
    private Bitmap avatarBmp;

    @BindView(R.id.exit_set) RelativeLayout rl;
    @BindView(R.id.userName_set_) TextView userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);

        if (Shared.getUserInfo() == null)
            rl.setVisibility(View.GONE);

        if (Shared.getUserInfo() != null)
            userName.setText(Shared.getUserInfo().getName());

    }

    @OnClick(R.id.exit_set)
    public void exit() {
        Shared.clearUserInfo();
        Shared.saveCookie(null);
        //Shared.saveTel(null);
        finish();
    }

    @OnClick(R.id.back_set_activity)
    public void back() {
        finish();
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.userInfo_set)
    public void setUserInfo() {
        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        } else {
            chooseImgDialog();

        }


    }

    /**
     * 修改昵称
     */
    @OnClick(R.id.userName_set)
    public void setNickName() {
        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        } else {
            showSetNameDialog(this);
        }

    }

    /**
     * 修改登录密码
     */
    @OnClick(R.id.modify_login_pwd_set)
    public void loginPwd() {
        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        } else {
            showSetPasswordDialog(this);
        }


    }

    /**
     * 修改支付密码
     */
    @OnClick(R.id.modify_pay_pwd_set)
    public void payPwd() {
        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        } else {

            HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                @Override public void onSuccess(Obj obj) {
                    if (obj.getIsHavePassword() == 0) {

                        showSetPayPwdDialog(SetActivity.this);

                    } else {

                        showModifyPayPwdDialog(SetActivity.this);

                    }


                }

                @Override public void onError(Throwable e) {
                    Log.i("lin", "-----lin----->  no " + e.toString());

                    Toast.makeText(SetActivity.this, "打开设置密码界面失败" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            A.getA(5, 0, 1).purse(listener, "11");
        }
    }

    /**
     * 法律条文
     */
    @OnClick(R.id.fa_lv_tiao_kuan)
    public void fa_lv_tiao_kuan() {
        startActivity(new Intent(SetActivity.this, ClauseActivity.class));
    }


    /**
     * 身份信息验证
     */
    @OnClick(R.id.authentication)
    public void authentication() {
        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        } else {
            Intent intent = new Intent(SetActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        }
    }


    /**
     * 关于我们
     */
    @OnClick(R.id.about_me_set)
    public void aboutMe() {
        Intent intent = new Intent(SetActivity.this, AboutActivity.class);
        startActivity(intent);


    }

    private void showSetPasswordDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_modify_pwd, null);

        final EditText e1 = (EditText) mView.findViewById(R.id.e1);
        final EditText e2 = (EditText) mView.findViewById(R.id.e2);
        final EditText e3 = (EditText) mView.findViewById(R.id.e3);

        TextView b1 = (TextView) mView.findViewById(R.id.b1);
        TextView b2 = (TextView) mView.findViewById(R.id.b2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setLoginPwdDialog.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {


                if (e2.getText().toString().length() < 6) {
                    Toast.makeText(SetActivity.this, "新密码长度不能小于6位", Toast.LENGTH_SHORT).show();
                }

                if (!e2.getText().toString().equals(e3.getText().toString())) {
                    Toast.makeText(SetActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpResultListener<Boolean> listener = new HttpResultListener<Boolean>() {
                    @Override public void onSuccess(Boolean aBoolean) {
                        Toast.makeText(SetActivity.this, "登陆密码修改成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onError(Throwable e) {
                        Toast.makeText(SetActivity.this, "登陆密码修改失败" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                A.getA(1, 0, 1).updatePwd(listener, MD5.GetMD5Code(e1.getText().toString()), MD5.GetMD5Code(e2.getText().toString()));
                setLoginPwdDialog.dismiss();

            }
        });
        builder.setView(mView);
        builder.setCancelable(false);
        setLoginPwdDialog = builder.create();
        setLoginPwdDialog.show();
    }

    private void showSetNameDialog(Context mContext) {

        if (Shared.getUserInfo() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
            dialog2(builder);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_modify_username, null);
        builder.setView(mView);
        builder.setCancelable(true);
        setuserNameDialog = builder.create();
        setuserNameDialog.show();

        final EditText e1 = (EditText) mView.findViewById(R.id.e1);
        TextView b1 = (TextView) mView.findViewById(R.id.b1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                if (e1.getText().toString() == null) {
                    Toast.makeText(SetActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    HttpResultListener<Boolean> listener = new HttpResultListener<Boolean>() {
                        @Override public void onSuccess(Boolean aBoolean) {
                            Log.i("lin", "昵称修改成功");
                            Toast.makeText(SetActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();

//                            Obj user = new Obj();
//                            user.setName(e1.getText().toString());
//                            user.setHead(Shared.getUserInfo().getHead());
//
//                            Shared.saveUserInfo(user);
                            userName.setText(e1.getText().toString());
                            UserInfoModel userInfoModel = Shared.getUserInfo();
                            userInfoModel.setName(e1.getText().toString());
                            Obj obj = new Obj();
                            obj.setName(userInfoModel.getName());
                            obj.setHead(userInfoModel.getHead());
                            obj.setIsReal(userInfoModel.getIsReal());
                            obj.setUid(userInfoModel.getUid());
                            Shared.saveUserInfo(obj);

                        }

                        @Override public void onError(Throwable e) {
                            Log.i("lin", "昵称修改失败" + e.toString());
                            Toast.makeText(SetActivity.this, "昵称修改失败", Toast.LENGTH_SHORT).show();
                        }
                    };
                    A.getA(1, 0, 1).updateName(listener, e1.getText().toString());
                    setuserNameDialog.dismiss();
                }

            }
        });
    }

    private void showSetPayPwdDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_pay_pwd, null);
        builder.setView(mView);
        builder.setCancelable(true);
        setPayPwdDialog = builder.create();
        setPayPwdDialog.show();

        final EditText e1 = (EditText) mView.findViewById(R.id.e1);
        final EditText e2 = (EditText) mView.findViewById(R.id.e2);
        TextView b1 = (TextView) mView.findViewById(R.id.b1);
        TextView b2 = (TextView) mView.findViewById(R.id.b2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setPayPwdDialog.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View view) {
                HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                    @Override public void onSuccess(Obj obj) {
                        Toast.makeText(SetActivity.this, "修改支付密码成功", Toast.LENGTH_SHORT).show();
                        setPayPwdDialog.dismiss();
                    }

                    @Override public void onError(Throwable e) {
                        Toast.makeText(SetActivity.this, "修改支付密码失败", Toast.LENGTH_SHORT).show();

                    }
                };
                if (!e1.getText().toString().equals(e2.getText().toString())) {
                    Toast.makeText(SetActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (e1.getText().toString().length() != 6) {
                    Toast.makeText(SetActivity.this, "密码的长度只能是六尾", Toast.LENGTH_SHORT).show();
                    return;
                }

                A.getA(4, 0, 1).setPayPwd(listener, MD5.GetMD5Code(e1.getText().toString()));
            }
        });


    }

    private void showModifyPayPwdDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_modify_pay_pwd, null);
        builder.setView(mView);
        builder.setCancelable(true);
        modifyPwdDialog = builder.create();
        modifyPwdDialog.show();
        final EditText e1 = (EditText) mView.findViewById(R.id.e1);
        final EditText e2 = (EditText) mView.findViewById(R.id.e2);
        final EditText e3 = (EditText) mView.findViewById(R.id.e3);
        TextView b1 = (TextView) mView.findViewById(R.id.b1);
        TextView b2 = (TextView) mView.findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                modifyPwdDialog.dismiss();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (!e2.getText().toString().equals(e3.getText().toString())) {
                    Toast.makeText(SetActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (e2.getText().toString().length() != 6) {
                    Toast.makeText(SetActivity.this, "密码的长度只能是六尾", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });
    }

    protected void dialog2(AlertDialog.Builder builder) {
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setMessage("请登录");
        builder.show();

    }

    @Override protected void onResume() {
        super.onResume();
        if (Shared.getUserInfo() != null)
            userName.setText(Shared.getUserInfo().getName());
    }

    /**
     * 选择头像图片的对话框
     */
    private void chooseImgDialog() {
        new android.app.AlertDialog.Builder(SetActivity.this)
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
        String SuserName;
        String SuserTel;
        String SuserAddress;

        final AVFile file2;

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
            String url = getPath(SetActivity.this, uri);
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

                        HttpResultListener<Boolean> listener = new HttpResultListener<Boolean>() {
                            @Override public void onSuccess(Boolean aBoolean) {
                                Toast.makeText(SetActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                                UserInfoModel user = Shared.getUserInfo();
                                Obj obj = new Obj();
                                obj.setName(user.getName());
                                obj.setUid(user.getUid());
                                obj.setIsReal(user.getIsReal());
                                obj.setHead(avatarFile.getUrl());
                                Shared.saveUserInfo(obj);
                            }

                            @Override public void onError(Throwable e) {
                                Toast.makeText(SetActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                            }
                        };

                        A.getA(1, 0, 1).updateHead(listener, avatarFile.getUrl());


                    }
                }
            });
        }
    }

}











