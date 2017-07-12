package com.example.lin_sir_one.tripbuyer.leancloudchatkit;


import java.util.List;

/**
 * Created by wli on 16/2/2.
 */
public interface LCChatProfilesCallBack {
  public void done(List<LCChatKitUser> userList, Exception exception);
}
