package com.imswy.okhttpdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.imswy.okhttpdemo.R;
import com.imswy.okhttpdemo.util.AppUtil;
import com.imswy.okhttpdemo.util.NetUtils;

public class MainActivity extends BaseActivity {

    private ImageView img_icon;
    private TextView text_account;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

    }

    @Override
    public void onNetworkChanged (int netWorkState) {
        switch (netWorkState) {
            case  NetUtils.NETWORK_NONE:
                //没有网络
                AppUtil.showToast("当前无网络连接，请检查网络",0);
                break;
            case  NetUtils.NETWORK_MOBILE:
                //移动网络
                AppUtil.showToast("当前正在使用移动数据",0);
                break;
            case  NetUtils.NETWORK_WIFI:
                //WiFi网络
                break;
        }
    }

    private void initView(){
        img_icon = findViewById(R.id.img_icon);
        text_account = findViewById(R.id.text_account);
    }

    private void initEvent(){
        String account = AppUtil.readSP("CurrentUserAccount_Login");
        String iconurl = AppUtil.readSP("CurrentUserIconUrl_Login");
        if(null!=account){
            text_account.setText(account);
        }
        if(null!=iconurl){
            Glide.with(MainActivity.this).load(iconurl).into(img_icon);
        }
    }
}
