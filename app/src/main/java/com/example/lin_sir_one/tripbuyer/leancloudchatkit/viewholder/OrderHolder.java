package com.example.lin_sir_one.tripbuyer.leancloudchatkit.viewholder;

/**
 * Created by linSir on 16/9/4.聊天中的
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;


/**
 * Created by wli on 16/9/13.
 * 聊天页面中的订单 item 对应的 holder
 */
public class OrderHolder extends LCIMChatItemHolder {

    private ImageView productPicture;
    private TextView userName;
    private TextView productName;
    private TextView productCount;
    private TextView productPrice;

    public OrderHolder(Context context, ViewGroup root, boolean isLeft) {
        super(context, root, isLeft);
    }

    @Override
    public void initView() {
        super.initView();
        if (isLeft) {
            conventLayout.addView(View.inflate(getContext(), R.layout.item_order, null));
        } else {
            conventLayout.addView(View.inflate(getContext(), R.layout.item_order, null));
        }
//        playButton = (LCIMPlayButton) itemView.findViewById(R.id.chat_item_audio_play_btn);
//        durationView = (TextView) itemView.findViewById(R.id.chat_item_audio_duration_view);
        productPicture = (ImageView) itemView.findViewById(R.id.iv_user_item_buyer2);
        userName = (TextView) itemView.findViewById(R.id.tv_user_name);
        productName = (TextView) itemView.findViewById(R.id.tv_product_name);
        productCount = (TextView) itemView.findViewById(R.id.tv_product_price);
        productPrice = (TextView) itemView.findViewById(R.id.tv_product_price2);

    }

//    @Override
//    public void bindData(Object o) {
//        super.bindData(o);
//        AVIMMessage message = (AVIMMessage) o;
//        if (message instanceof AVIMTextMessage) {
//            AVIMTextMessage textMessage = (AVIMTextMessage) message;
//            contentView.setText(textMessage.getText());
//        }
//    }

    @Override
    public void bindData(Object o) {
        super.bindData(o);
        AVIMMessage message = (AVIMMessage) o;
        if (message instanceof AVIMTextMessage) {
            AVIMTextMessage textMessage = (AVIMTextMessage) message;
            //contentView.setText(textMessage.getText());
            try {
                ImageUtil.requestImg(BaseApplication.get().getAppContext(), textMessage.getText().split("♂")[1], productPicture);
                KLog.i("_____linsir_____1" + textMessage.getText().split("♂")[1]);
                userName.setText(textMessage.getText().split("♂")[2]);
                KLog.i("_____linsir_____2" + textMessage.getText().split("♂")[2]);
                productName.setText(textMessage.getText().split("♂")[3]);
                KLog.i("_____linsir_____3" + textMessage.getText().split("♂")[3]);
                productCount.setText(textMessage.getText().split("♂")[4]);
                KLog.i("_____linsir_____4" + textMessage.getText().split("♂")[4]);
                productCount.setText(textMessage.getText().split("♂")[5]);
                KLog.i("_____linsir_____5" + textMessage.getText().split("♂")[5]);
            } catch (Exception e) {

            }


        }


    }
}
