package com.example.lin_sir_one.tripbuyer.leanchat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKitUser;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.activity.LCIMConversationActivity;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.utils.LCIMConstants;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.viewholder.LCIMCommonViewHolder;
import com.squareup.picasso.Picasso;


/**
 * Created by wli on 15/11/24.
 */
public class ContactItemHolder extends LCIMCommonViewHolder<LCChatKitUser> {

  TextView nameView;
  ImageView avatarView;

  public LCChatKitUser lcChatKitUser;

  public ContactItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.common_user_item);
    initView();
  }

  public void initView() {
    nameView = (TextView)itemView.findViewById(R.id.tv_friend_name);
    avatarView = (ImageView)itemView.findViewById(R.id.img_friend_avatar);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getContext(), LCIMConversationActivity.class);
        intent.putExtra(LCIMConstants.PEER_ID, lcChatKitUser.getUserId());
        getContext().startActivity(intent);
      }
    });
  }

  @Override
  public void bindData(LCChatKitUser lcChatKitUser) {
    this.lcChatKitUser = lcChatKitUser;
    final String avatarUrl = lcChatKitUser.getAvatarUrl();
    if (!TextUtils.isEmpty(avatarUrl)) {
      Picasso.with(getContext()).load(avatarUrl).into(avatarView);
    } else {
      avatarView.setImageResource(R.drawable.lcim_default_avatar_icon);
    }
    nameView.setText(lcChatKitUser.getUserName());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ContactItemHolder>() {
    @Override
    public ContactItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new ContactItemHolder(parent.getContext(), parent);
    }
  };
}
