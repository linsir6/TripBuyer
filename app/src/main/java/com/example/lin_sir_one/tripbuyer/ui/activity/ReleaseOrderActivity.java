package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.example.lin_sir_one.tripbuyer.Constants;
import com.example.lin_sir_one.tripbuyer.Pay.PayResult;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.event.FirstEvent;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.utils.FileUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;

/**
 * Created by mac on 16/7/10.发布商品界面，16/07/23重构此界面,
 */
public class ReleaseOrderActivity extends AppCompatActivity {

    private HttpResultListener<String> listener;
    private static final String IMAGE_NAME = "user_avatar.jpg";
    private static final int GALLERY_REQUEST = 102;
    private static final int GALLERY_KITKAT_REQUEST = 103;
    private static final int CAMERA_REQUEST = 104;
    private static final int RESULT = 105;
    private static final int RESULT_CANCELED = 0;
    private Bitmap avatarBmp;
    private int imageWhere;
    private IWXAPI api;
    @BindView(R.id.sum_release) TextView sum;                           //总价
    @BindView(R.id.shopping_address) RelativeLayout setAddress;         //设置地址
    @BindView(R.id.product_name_release) EditText productName1;         //商品1的名称
    @BindView(R.id.product_name_release2) EditText productName2;        //商品2的名称
    @BindView(R.id.product_name_release3) EditText productName3;        //商品3的名称
    @BindView(R.id.img_product) ImageView picture1;                     //商品1的图片
    @BindView(R.id.img_product2) ImageView picture2;                    //商品2的图片
    @BindView(R.id.img_product3) ImageView picture3;                    //商品3的图片
    @BindView(R.id.release_price) EditText price1;                      //商品1的价格
    @BindView(R.id.release_price2) EditText price2;                     //商品2的图片
    @BindView(R.id.release_price3) EditText price3;                     //商品3的图片
    @BindView(R.id.product_count_relese) TextView count1;               //商品1的数量
    @BindView(R.id.product_count_relese2444) TextView count2;           //商品2的数量
    @BindView(R.id.product_count_relese3) TextView count3;              //商品3的数量
    @BindView(R.id.tv_user_address) TextView userName;                  //用户姓名
    @BindView(R.id.tv_tel_address) TextView userTel;                    //用户的手机号
    @BindView(R.id.tv_address_address) TextView userAddress;            //用户的地址
    @BindView(R.id.goods2) RelativeLayout good2;                        //添加的第二个商品
    @BindView(R.id.goods3) RelativeLayout good3;                        //添加的第三个商品

    String _productName1, _productName2, _productName3;
    String _picture1, _picture2, _picture3, _picture;
    String _price1, _price2, _price3;
    String _count1, _count2, _count3;

    String productName, price, num, address, deliveryArea, requirements, picture;
    int deliveryTime, pack;
    Double total;

    String wid;

    String event = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_order);
        initAddress();
        ButterKnife.bind(this);

        listener = new HttpResultListener<String>() {
            @Override public void onSuccess(String s) {
                KLog.i("----lin---->  发布成功+   " + s);
                wid = s;
                choosePayDialog();


            }

            @Override public void onError(Throwable e) {
                KLog.i("____lin____>  发布失败+   " + e.toString());
            }
        };
    }

    /**
     * 选择支付方式的对话框
     */

    private void choosePayDialog() {
        new AlertDialog.Builder(ReleaseOrderActivity.this)
                .setTitle(getResources().getString(R.string.pay))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.zhi_fu_bao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HttpResultListener<String> listener = new HttpResultListener<String>() {
                            @Override public void onSuccess(String s) {
                                KLog.i("____lin____> 获取成功" + s);
//                                Intent intent = new Intent(ReleaseOrderActivity.this, PayDemoActivity.class);
//                                intent.putExtra("payId", s);
//                                startActivity(intent);

                                final String ss = s;

                                Runnable payRunnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask(ReleaseOrderActivity.this);
                                        // 调用支付接口，获取支付结果
                                        String result = alipay.pay(ss, true);

                                        Message msg = new Message();
                                        msg.what = 1;
                                        msg.obj = result;
                                        mHandler.sendMessage(msg);
                                    }
                                };

                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }

                            @Override public void onError(Throwable e) {
                                KLog.i("____lin____> 获取失败" + e.toString());
                            }
                        };
                        KLog.i("----linsir6---->" + wid);
                        A.getA(5, 0, 1).wantedPay(listener, wid);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.wei_xin), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        api = WXAPIFactory.createWXAPI(ReleaseOrderActivity.this, "wx0f4493dbeca4d2b2");
                        api.registerApp(Constants.APP_ID);

                        KLog.i("____lin____> 点击了 微信 支付。。。。。");

                        try {

                            HttpResultListener<Obj> listener3 = new HttpResultListener<Obj>() {
                                @Override public void onSuccess(Obj obj) {
                                    PayReq req = new PayReq();
                                    req.appId = "wx0f4493dbeca4d2b2";
                                    req.partnerId = "1377137302";
                                    req.prepayId = obj.getPrepay_id();
                                    req.nonceStr = obj.getNonce_str();
                                    req.timeStamp = obj.getTimestamp();
                                    req.packageValue = "Sign=WXPay";
                                    req.sign = obj.getSign();
                                    req.extData = "app data"; // optional
                                    api.sendReq(req);

                                }

                                @Override public void onError(Throwable e) {
                                }
                            };

                            A.getA(5, 0, 1).weiXinquankuan(listener3, wid);


//                            String url = "http://139.129.221.22:8080/lxms-user/weiPay/depositPay";
//                            Intent intent = getIntent();
//                            url = url + "?id=" + wid;
//                            String cookie = "JSESSIONID=2362C814E09F771E850ED3E7B5268685";
//                            OkHttpUtils
//                                    .post()
//                                    .url(url)
//                                    .addHeader("Cookie", cookie)
//                                    .addParams("id", "200")
//                                    .build()
//                                    .execute(new StringCallback() {
//                                        @Override public void onError(Request request, Exception e) {
//                                            Log.i("lin", "----lin---->appId:  " + "on error");
//                                        }
//
//                                        @Override public void onResponse(String response) {
//                                            Log.i("lin", "----lin---->appId:  " + "on response" + response);
//
//
//                                            String[] result = response.split("\"");
//
//                                            Log.i("lin", "----lin---->appId:  " + result[11]);
//                                            Log.i("lin", "----lin---->mch_id:  " + result[15]);
//                                            Log.i("lin", "----lin---->nonce_str:  " + result[19]);
//                                            Log.i("lin", "----lin---->prepay_id:  " + result[23]);
//                                            Log.i("lin", "----lin---->sign:  " + result[29]);
//                                            Log.i("lin", "----lin---->timestamp:  " + result[33]);
//
//                                            PayReq req = new PayReq();
//                                            req.appId = "wx0f4493dbeca4d2b2";
//                                            req.partnerId = "1377137302";
//                                            req.prepayId = result[23];
//                                            req.nonceStr = result[19];
//                                            req.timeStamp = result[33];
//                                            req.packageValue = "Sign=WXPay";
//                                            req.sign = result[29];
//                                            req.extData = "app data"; // optional
//                                            api.sendReq(req);
//
//
//                                        }
//                                    });


                            Toast.makeText(ReleaseOrderActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信


                        } catch (Exception e) {
                            Log.e("PAY_GET", "异常：" + e.getMessage());
                            Toast.makeText(ReleaseOrderActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }).show();
    }

    private void initAddress() {
        HttpResultListener<List<AllAddress>> listener2;
        listener2 = new HttpResultListener<List<AllAddress>>() {
            @Override
            public void onSuccess(List<AllAddress> allAddresses) {
                Toast.makeText(ReleaseOrderActivity.this, "获取默认地址成功", Toast.LENGTH_SHORT).show();
                address = String.valueOf(allAddresses.get(0).getAddressId());
                userName.setText(allAddresses.get(0).getShipName());
                userTel.setText(allAddresses.get(0).getPhone());
                String address = allAddresses.get(0).getProvince() + " " + allAddresses.get(0).getCity() +
                        " " + allAddresses.get(0).getArea() + " " + allAddresses.get(0).getDetail();
                userAddress.setText(address);
            }

            @Override
            public void onError(Throwable e) {

            }
        };
        //ApiService5.getInstance().allAddress(listener2, 1);
        A.getA(1, 0, 1).allAddress(listener2, 1);
    }

    /**
     * 减少商品
     */
    @OnClick(R.id.sub_goods)
    public void subGoods() {
        Boolean g2IsVisible = good2.getVisibility() == View.VISIBLE;
        Boolean g3IsVisible = good3.getVisibility() == View.VISIBLE;
        if (!g2IsVisible && !g3IsVisible) {
            Toast.makeText(ReleaseOrderActivity.this, "至少要添加一个商品", Toast.LENGTH_SHORT).show();
        } else if (g2IsVisible && !g3IsVisible) {
            good2.setVisibility(View.GONE);
            productName2.setText("");
            price2.setText("");
            count2.setText("");
            picture2.setImageResource(R.mipmap.empty_img);

        } else if (g2IsVisible && g3IsVisible) {
            good3.setVisibility(View.GONE);
            productName3.setText("");
            price3.setText("");
            count3.setText("");
            picture3.setImageResource(R.mipmap.empty_img);
        }
    }

    /**
     * 增加商品
     */
    @OnClick(R.id.add_goods)
    public void addGoods() {
        Boolean g2IsVisible = good2.getVisibility() == View.VISIBLE;
        Boolean g3IsVisible = good3.getVisibility() == View.VISIBLE;
        if (!g2IsVisible && !g3IsVisible) {
            good2.setVisibility(View.VISIBLE);
        } else if (g2IsVisible && !g3IsVisible) {
            good3.setVisibility(View.VISIBLE);
        } else if (g2IsVisible && g3IsVisible) {
            Toast.makeText(ReleaseOrderActivity.this, "最多只能添加三个商品", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回按钮
     */
    @OnClick(R.id.back_order)
    public void back() {
        finish();
    }

    /**
     * 点击地址框
     */
    @OnClick(R.id.shopping_address)
    public void address() {
        startActivityForResult(new Intent(ReleaseOrderActivity.this, ShippingAddressActivity.class), 1);
    }

    /**
     * 特殊要求
     */
    @OnClick(R.id.rel20)
    public void specialRequest() {

        startActivityForResult(new Intent(ReleaseOrderActivity.this, SpecialRequisitionActivity.class), 2);

    }

    /**
     * 商品1的减少按钮
     */
    @OnClick(R.id.sub_relase)
    public void subRelase1() {
        int count = Integer.parseInt(count1.getText().toString().trim());
        if (count > 0) {
            count = count - 1;
            count1.setText(String.valueOf(count));
        }
    }

    /**
     * 商品1的增加按钮
     */
    @OnClick(R.id.add_release)
    public void addRelase1() {
        int count = Integer.parseInt(count1.getText().toString().trim());
        if (count >= 0 && count < 10) {
            count = count + 1;
            count1.setText(String.valueOf(count));
        }
    }

    /**
     * 商品2的减少按钮
     */
    @OnClick(R.id.sub_relase2)
    public void subRelase2() {
        int count = Integer.parseInt(count2.getText().toString().trim());
        if (count > 0) {
            count = count - 1;
            count2.setText(String.valueOf(count));
        }
    }

    /**
     * 商品2的增加按钮
     */
    @OnClick(R.id.add_release2)
    public void addRelase2() {
        int count = Integer.parseInt(count2.getText().toString().trim());
        if (count >= 0 && count < 10) {
            count = count + 1;
            count2.setText(String.valueOf(count));
        }
    }

    /**
     * 商品3的减少按钮
     */
    @OnClick(R.id.sub_relase3)
    public void subRelase3() {
        int count = Integer.parseInt(count3.getText().toString().trim());
        if (count >= 0 && count < 10) {
            count = count - 1;
            count3.setText(String.valueOf(count));
        }
    }

    /**
     * 商品3的增加按钮
     */
    @OnClick(R.id.add_release3)
    public void addRelase3() {
        int count = Integer.parseInt(count3.getText().toString().trim());
        if (count >= 0 && count < 10) {
            count = count + 1;
            count3.setText(String.valueOf(count));
        }
    }


    /**
     * 商品1的图片
     */
    @OnClick(R.id.img_product)
    public void img1() {
        chooseImgDialog();
        imageWhere = 1;
    }

    /**
     * 商品2的图片
     */
    @OnClick(R.id.img_product2)
    public void img2() {
        chooseImgDialog();
        imageWhere = 2;
    }

    /**
     * 商品3的图片
     */
    @OnClick(R.id.img_product3)
    public void img3() {
        chooseImgDialog();
        imageWhere = 3;
    }

    /**
     * 点击rlayout让商品名称获取焦点
     */
    @OnClick(R.id.rel3)
    public void rel3() {
        productName1.requestFocus();
        InputMethodManager imm = (InputMethodManager) productName1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 点击rlayout让商品单价获取焦点
     */
    @OnClick(R.id.rel6)
    public void rel6() {
        price1.requestFocus();
        InputMethodManager imm = (InputMethodManager) price1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }


    /**
     * 选择商品图片的对话框
     */
    private void chooseImgDialog() {
        new AlertDialog.Builder(ReleaseOrderActivity.this)
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
        switch (requestCode) {
            case 1://地址框，返回的结果

                try {
                    address = String.valueOf(data.getExtras().getString("addressId"));
                    userName.setText(data.getExtras().getString("userName"));
                    userTel.setText(data.getExtras().getString("userPhone"));
                    userAddress.setText(data.getExtras().getString("userAddress"));
                    //Toast.makeText(ReleaseOrderActivity.this, "aaaaa   " + address, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(ReleaseOrderActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }


                break;

            case 2://特殊要求框，返回的结果


                Log.i("lin", "----lin--->  code =2");

                deliveryTime = data.getExtras().getInt("sendTime");
                pack = data.getExtras().getInt("isHave");

                requirements = data.getExtras().getString("feedBack");

                break;
        }


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
                        switch (imageWhere) {
                            case 1:
                                picture1.setImageBitmap(avatarBmp);
                                updateUserInfo(avatarBmp);
                                break;
                            case 2:
                                picture2.setImageBitmap(avatarBmp);
                                updateUserInfo(avatarBmp);
                                break;
                            case 3:
                                picture3.setImageBitmap(avatarBmp);
                                updateUserInfo(avatarBmp);
                                break;
                        }
                        //updateUserInfo(avatarBmp);
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
            String url = getPath(ReleaseOrderActivity.this, uri);
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

                        switch (imageWhere) {

                            case 1:
                                _picture1 = avatarFile.getUrl();
                                break;

                            case 2:
                                _picture2 = avatarFile.getUrl();
                                break;

                            case 3:
                                _picture3 = avatarFile.getUrl();
                                break;
                        }
                    }
                }
            });
        }
    }


    @OnTextChanged({R.id.release_price, R.id.product_count_relese, R.id.release_price2
            , R.id.product_count_relese2444, R.id.release_price3, R.id.product_count_relese3})
    public void change() {
        Double c1, c2, c3, p1, p2, p3;

        try {
            c1 = Double.parseDouble(count1.getText().toString().trim());
        } catch (Exception e) {
            c1 = 0.0;
        }

        try {
            c2 = Double.parseDouble(count2.getText().toString().trim());
        } catch (Exception e) {
            c2 = 0.0;
        }

        try {
            c3 = Double.parseDouble(count3.getText().toString().trim());
        } catch (Exception e) {
            c3 = 0.0;
        }

        try {
            p1 = Double.parseDouble(price1.getText().toString().trim());
        } catch (Exception e) {
            p1 = 0.0;
        }

        try {
            p2 = Double.parseDouble(price2.getText().toString().trim());
        } catch (Exception e) {
            p2 = 0.0;
        }

        try {
            p3 = Double.parseDouble(price3.getText().toString().trim());
        } catch (Exception e) {
            p3 = 0.0;
        }

        Double _sum = c1 + c2 + c3;
        total = c1 * p1 + c2 * p2 + c3 * p3;
        String text = "总价：" + String.valueOf(total) + "  (" + String.valueOf(_sum) + ")件";
        sum.setText(text);
    }

    @OnClick(R.id.release_order_release)
    public void releaseOrder() {
        Intent intent = new Intent(ReleaseOrderActivity.this, OrderRuleActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.send_order_release)
    public void sendOrder() {


        _productName1 = productName1.getText().toString().trim();
        _productName2 = productName2.getText().toString().trim();
        _productName3 = productName3.getText().toString().trim();

        _price1 = price1.getText().toString();
        _price2 = price2.getText().toString();
        _price3 = price3.getText().toString();

        _count1 = count1.getText().toString();
        _count2 = count2.getText().toString();
        _count3 = count3.getText().toString();

        if (_productName2.equals("") && _productName3.equals("")) {
            productName = _productName1;
            picture = _picture1;
            price = _price1;
            num = _count1;
        }

        if (!_productName2.equals("") && _productName3.equals("")) {
            productName = _productName1 + ";" + _productName2;
            picture = _picture1 + ";" + _picture2;
            price = _price1 + ";" + _price2;
            num = _count1 + ";" + _count2;
        }

        if (!_productName2.equals("") && !_productName3.equals("")) {
            productName = _productName1 + ";" + _productName2 + ";" + _productName3;
            picture = _picture1 + ";" + _picture2 + ";" + _picture3;
            price = _price1 + ";" + _price2 + ";" + _price3;
            num = _count1 + ";" + _count2 + ";" + _count3;
        }

        if (productName == null) {
            Toast.makeText(ReleaseOrderActivity.this, "商品名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (price == null) {
            Toast.makeText(ReleaseOrderActivity.this, "商品价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (num == null) {
            Toast.makeText(ReleaseOrderActivity.this, "商品数量不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address == null) {
            Toast.makeText(ReleaseOrderActivity.this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (deliveryTime == 0) {
            Toast.makeText(ReleaseOrderActivity.this, "发货时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pack == 0) {
            Toast.makeText(ReleaseOrderActivity.this, "有无包装不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requirements == null) {
            Toast.makeText(ReleaseOrderActivity.this, "特殊要求不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (picture == null) {
            Toast.makeText(ReleaseOrderActivity.this, "商品图片不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        KLog.i("____lin____>" + productName);
        KLog.i("____lin____>" + price);
        KLog.i("____lin____>" + num);
        KLog.i("____lin____>" + address);
        KLog.i("____lin____>" + deliveryTime);
        KLog.i("____lin____>" + pack);
        KLog.i("____lin____>" + requirements);
        KLog.i("____lin____>" + picture);
        KLog.i("____lin____>" + total);

        String createType = "0";
        try {
            Intent intent = getIntent();
            if (intent.getExtras().getString("type").equals("talkHome")) {
                createType = "1";
            }
        } catch (Exception e) {

        }


        A.getA(3, 0, 1).wanted(listener, productName, price, num, address, deliveryTime, pack, "1", requirements, picture, total, createType);
        event = "~!@#$♂" + picture + "♂" + Shared.getUserInfo().getName() + "♂" + productName + "♂" + num + "♂" + price + "♂" + wid;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ReleaseOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        event = "~!@#$♂" + picture + "♂" + Shared.getUserInfo().getName() + "♂" + productName + "♂" + num + "♂" + price + "♂" + wid;

                        EventBus.getDefault().post(new FirstEvent(event));
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(ReleaseOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(ReleaseOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    public static String startUCrop(Activity activity, String sourceFilePath,
                                    int requestCode, float aspectRatioX, float aspectRatioY) {
        Uri sourceUri = Uri.fromFile(new File(sourceFilePath));
        File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
        //裁剪后图片的绝对路径
        String cameraScalePath = outFile.getAbsolutePath();
        Uri destinationUri = Uri.fromFile(outFile);
        //初始化，第一个参数：需要裁剪的图片；第二个参数：裁剪后图片
        UCrop uCrop = UCrop.of(sourceUri, destinationUri);
        //初始化UCrop配置
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        //UCrop配置
        uCrop.withOptions(options);
        //设置裁剪图片的宽高比，比如16：9
        uCrop.withAspectRatio(aspectRatioX, aspectRatioY);
        //uCrop.useSourceImageAspectRatio();
        //跳转裁剪页面
        uCrop.start(activity, requestCode);
        return cameraScalePath;
    }


}









