package com.imswy.okhttpdemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.imswy.okhttpdemo.activity.BaseActivity;
import com.imswy.okhttpdemo.interfaces.NetworkEvent;
import com.imswy.okhttpdemo.util.AppUtil;
import com.imswy.okhttpdemo.util.NetUtils;

import static com.imswy.okhttpdemo.util.AppUtil.showToast;

public abstract class NetWorkChangReceiver extends BroadcastReceiver {

    private NetworkEvent networkEvent = BaseActivity.networkEvent;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if(networkEvent != null) {
                networkEvent.onNetworkChange(NetUtils.getNetWorkState(context));
            }
        }
    }
}
