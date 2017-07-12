package com.example.lin_sir_one.tripbuyer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.uikit.CameraUtil;
import com.example.lin_sir_one.tripbuyer.uikit.MMAlert;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXEmojiObject;
import com.tencent.mm.sdk.modelmsg.WXFileObject;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

public class AddFavoriteToWXActivity extends Activity {
	private static final int THUMB_SIZE = 150;

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private IWXAPI api;
	private static final int MMAlertSelect1  =  0;
	private static final int MMAlertSelect2  =  1;
	private static final int MMAlertSelect3  =  2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		
		setContentView(R.layout.add_fav_to_wx);
		initView();
	}

	private void initView() {

		// send to weixin
		findViewById(R.id.send_text).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
								
				final EditText editor = new EditText(AddFavoriteToWXActivity.this);
				editor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				editor.setText(R.string.send_text_default);
								
				MMAlert.showAlert(AddFavoriteToWXActivity.this, "send text", editor, getString(R.string.app_share), getString(R.string.app_cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = editor.getText().toString();
						if (text == null || text.length() == 0) {
							return;
						}
						
						// 初始化一个WXTextObject对象
						WXTextObject textObj = new WXTextObject();
						textObj.text = text;

						// 用WXTextObject对象初始化一个WXMediaMessage对象
						WXMediaMessage msg = new WXMediaMessage();
						msg.mediaObject = textObj;
						// 发送文本类型的消息时，title字段不起作用
						// msg.title = "Will be ignored";
						msg.description = text;

						// 构造一个Req
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
						req.message = msg;
						req.scene = SendMessageToWX.Req.WXSceneFavorite;
						req.openId = getOpenId();
						// 调用api接口发送数据到微信
						api.sendReq(req);
						finish();
					}
				}, null);
			}
		});

		findViewById(R.id.send_img).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_img), 
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_img_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1: {
							Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
							WXImageObject imgObj = new WXImageObject(bmp);
							
							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = imgObj;
							
							Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
							bmp.recycle();
							msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("img");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect2: {
							String path = SDCARD_ROOT + "/test.png";
							File file = new File(path);
							if (!file.exists()) {
								String tip = AddFavoriteToWXActivity.this.getString(R.string.send_img_file_not_exist);
								Toast.makeText(AddFavoriteToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
								break;
							}
							
							WXImageObject imgObj = new WXImageObject();
							imgObj.setImagePath(path);
							
							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = imgObj;
							
							Bitmap bmp = BitmapFactory.decodeFile(path);
							Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
							bmp.recycle();
							msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("img");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect3: {
							String url = "http://weixin.qq.com/zh_CN/htmledition/images/weixin/weixin_logo0d1938.png";
								
							try{
								WXImageObject imgObj = new WXImageObject();
								imgObj.imageUrl = url;
								
								WXMediaMessage msg = new WXMediaMessage();
								msg.mediaObject = imgObj;

								Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
								Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
								bmp.recycle();
								msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
								
								SendMessageToWX.Req req = new SendMessageToWX.Req();
								req.transaction = buildTransaction("img");
								req.message = msg;
								req.scene = SendMessageToWX.Req.WXSceneFavorite;
								req.openId = getOpenId();
								api.sendReq(req);
								
								finish();
							} catch(Exception e) {
								e.printStackTrace();
							}
					
							break;
						}
						default:
							break;
						}
					}
					
				});
			}
		});
		
		findViewById(R.id.send_file).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_file), 
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_file_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1: {
							Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img);
							byte[] dataBuf = null;
							try {
								final ByteArrayOutputStream os = new ByteArrayOutputStream();
								bmp.compress(Bitmap.CompressFormat.JPEG, 85, os);
								dataBuf = os.toByteArray();
								os.close();

							} catch (Exception e) {
								e.printStackTrace();
							}
							WXFileObject fileObject = new WXFileObject(dataBuf);
							
							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = fileObject;
							
							Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
							bmp.recycle();
							msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("file");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect2: {
							String path = SDCARD_ROOT + "/test.jpg";
							File file = new File(path);
							if (!file.exists()) {
								String tip = AddFavoriteToWXActivity.this.getString(R.string.send_img_file_not_exist);
								Toast.makeText(AddFavoriteToWXActivity.this, tip + " path = " + path, Toast.LENGTH_LONG).show();
								break;
							}
							
							WXFileObject fileObject = new WXFileObject();
							fileObject.setFilePath(path);
							
							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = fileObject;
							
							Bitmap bmp = BitmapFactory.decodeFile(path);
							Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
							bmp.recycle();
							msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("file");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						
						default:
							break;
						}
					}
					
				});
			}
		});
		

		findViewById(R.id.send_music).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_music),
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_music_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1: {
							WXMusicObject music = new WXMusicObject();
							//music.musicUrl = "http://www.baidu.com";
							music.musicUrl="http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
							//music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = music;
							msg.title = "Music Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
							msg.description = "Music Album Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";

							Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
							msg.thumbData = Util.bmpToByteArray(thumb, true);

							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("music");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect2: {
							WXMusicObject music = new WXMusicObject();
							music.musicLowBandUrl = "http://www.qq.com";

							WXMediaMessage msg = new WXMediaMessage();
							msg.mediaObject = music;
							msg.title = "Music Title";
							msg.description = "Music Album";

							Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
							msg.thumbData = Util.bmpToByteArray(thumb, true);

							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("music");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						default:
							break;
						}
					}
				});
			}
		});
		
		findViewById(R.id.send_video).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_video), 
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_video_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1: {
							WXVideoObject video = new WXVideoObject();
							video.videoUrl = "http://www.baidu.com";

							WXMediaMessage msg = new WXMediaMessage(video);
							msg.title = "Video Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
							msg.description = "Video Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
							Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
							msg.thumbData = Util.bmpToByteArray(thumb, true);
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("video");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect2: {
							WXVideoObject video = new WXVideoObject();
							video.videoLowBandUrl = "http://www.qq.com";

							WXMediaMessage msg = new WXMediaMessage(video);
							msg.title = "Video Title";
							msg.description = "Video Description";

							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("video");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						default:
							break;
						}
					}
				});
			}
		});

		findViewById(R.id.send_webpage).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_webpage),
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_webpage_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case MMAlertSelect1:
							WXWebpageObject webpage = new WXWebpageObject();
							webpage.webpageUrl = "http://www.baidu.com";
							WXMediaMessage msg = new WXMediaMessage(webpage);
							msg.title = "WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title WebPage Title Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
							msg.description = "WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description WebPage Description Very Long Very Long Very Long Very Long Very Long Very Long Very Long";
							Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
							msg.thumbData = Util.bmpToByteArray(thumb, true);
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("webpage");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						default:
							break;
						}
					}
				});
			}
		});

		findViewById(R.id.send_appdata).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_appdata), 
					AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_appdata_item),
					null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {
						switch(whichButton){
						case MMAlertSelect1:
							final String dir = SDCARD_ROOT + "/tencent/";
							File file = new File(dir);
							if (!file.exists()) {
								file.mkdirs();
							}
							CameraUtil.takePhoto(AddFavoriteToWXActivity.this, dir, "send_appdata", 0x101);
							break;
						case MMAlertSelect2: {
							final WXAppExtendObject appdata = new WXAppExtendObject();
							final String path = SDCARD_ROOT + "/test.png";
							appdata.fileData = Util.readFromFile(path, 0, -1);
							appdata.extInfo = "this is ext info";

							final WXMediaMessage msg = new WXMediaMessage();
							msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
							msg.title = "this is title";
							msg.description = "this is description sjgksgj sklgjl sjgsgskl gslgj sklgj sjglsjgs kl gjksss ssssssss sjskgs kgjsj jskgjs kjgk sgjsk Very Long Very Long Very Long Very Longgj skjgks kgsk lgskg jslgj";
							msg.mediaObject = appdata;
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("appdata");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						case MMAlertSelect3: {
							// send appdata with no attachment
							final WXAppExtendObject appdata = new WXAppExtendObject();
							appdata.extInfo = "this is ext info";
							final WXMediaMessage msg = new WXMediaMessage();
							msg.title = "this is title";
							msg.description = "this is description";
							msg.mediaObject = appdata;
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("appdata");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						default:
							break;
						}
					}
					
				});
			}
		});
		
		findViewById(R.id.send_emoji).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(AddFavoriteToWXActivity.this, getString(R.string.send_emoji),
						AddFavoriteToWXActivity.this.getResources().getStringArray(R.array.send_emoji_item),
						null, new MMAlert.OnAlertSelectId(){

					@Override
					public void onClick(int whichButton) {						
						final String EMOJI_FILE_PATH = SDCARD_ROOT + "/emoji.gif";
						final String EMOJI_FILE_THUMB_PATH = SDCARD_ROOT + "/emojithumb.jpg";
						switch(whichButton){
						case MMAlertSelect1: {
							WXEmojiObject emoji = new WXEmojiObject();
							emoji.emojiPath = EMOJI_FILE_PATH;
							
							WXMediaMessage msg = new WXMediaMessage(emoji);
							msg.title = "Emoji Title";
							msg.description = "Emoji Description";
							msg.thumbData = Util.readFromFile(EMOJI_FILE_THUMB_PATH, 0, (int) new File(EMOJI_FILE_THUMB_PATH).length());
				
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("emoji");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						
						case MMAlertSelect2: {
							WXEmojiObject emoji = new WXEmojiObject();
							emoji.emojiData = Util.readFromFile(EMOJI_FILE_PATH, 0, (int) new File(EMOJI_FILE_PATH).length());
							WXMediaMessage msg = new WXMediaMessage(emoji);
							
							msg.title = "Emoji Title";
							msg.description = "Emoji Description";
							msg.thumbData = Util.readFromFile(EMOJI_FILE_THUMB_PATH, 0, (int) new File(EMOJI_FILE_THUMB_PATH).length());
							
							SendMessageToWX.Req req = new SendMessageToWX.Req();
							req.transaction = buildTransaction("emoji");
							req.message = msg;
							req.scene = SendMessageToWX.Req.WXSceneFavorite;
							req.openId = getOpenId();
							api.sendReq(req);
							
							finish();
							break;
						}
						default:
							break;
						}
					}
				});
			}
		});

		// get token
		findViewById(R.id.get_token).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText scopeEt = (EditText) findViewById(R.id.get_token_scope_et);
				String scope = scopeEt.getText().toString();
				if (scope == null || scope.length() == 0) {
					scope = "snsapi_userinfo";
				}
				
				// send oauth request
				final SendAuth.Req req = new SendAuth.Req();
				//req.scope = "post_timeline";
				req.scope = scope;
				req.state = "none";
				req.openId = getOpenId();
				api.sendReq(req);
				finish();
			}
		});
		
		// unregister from weixin
		findViewById(R.id.unregister).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				api.unregisterApp();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0x101: {
			final WXAppExtendObject appdata = new WXAppExtendObject();
			final String path = CameraUtil.getResultPhotoPath(this, data, SDCARD_ROOT + "/tencent/");
			appdata.filePath = path;
			appdata.extInfo = "this is ext info";

			final WXMediaMessage msg = new WXMediaMessage();
			msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true));
			msg.title = "this is title";
			msg.description = "this is description";
			msg.mediaObject = appdata;
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("appdata");
			req.message = msg;
			req.scene = SendMessageToWX.Req.WXSceneFavorite;
			req.openId = getOpenId();
			api.sendReq(req);
			
			finish();
			break;
		}
		default:
			break;
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	// supported during Build.OPENID_SUPPORTED_SDK_INT = 0x22000001
	private String getOpenId() {
		EditText openIdEt = (EditText) findViewById(R.id.openid_et);
		return openIdEt.getText().toString();
	}
}
