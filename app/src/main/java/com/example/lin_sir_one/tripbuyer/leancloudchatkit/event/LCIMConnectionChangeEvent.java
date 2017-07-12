package com.example.lin_sir_one.tripbuyer.leancloudchatkit.event;

/**
 * Created by wli on 15/12/16.
 * 网络连接状态变化的事件
 */
public class LCIMConnectionChangeEvent {
  public boolean isConnect;

  public LCIMConnectionChangeEvent(boolean isConnect) {
    this.isConnect = isConnect;
  }
}
