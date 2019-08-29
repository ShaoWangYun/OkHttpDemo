package com.imswy.okhttpdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imswy.okhttpdemo.interfaces.NetworkEvent;

public abstract class BaseActivity extends AppCompatActivity implements NetworkEvent {

    public static NetworkEvent networkEvent;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkEvent = this;
    }

    @Override
    public void onNetworkChange (int netWorkState) {
        onNetworkChanged(netWorkState);
    }

    /**
     * 全局检测网络广播的回调 处理网络变化
     * 注:在程序第一次启动的时候,没网并不会回调,需要自己在启动页面,或者主页自己再判断一次
     *
     * @param netWorkState 网络状态    -1:没网络 0:移动网络 1:WiFi网络
     */
    public abstract void onNetworkChanged (int netWorkState);

}
