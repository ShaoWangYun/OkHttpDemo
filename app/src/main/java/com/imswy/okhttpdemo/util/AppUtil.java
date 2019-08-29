package com.imswy.okhttpdemo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.imswy.okhttpdemo.application.OkHttpApplication;
import com.imswy.okhttpdemo.receivers.NetWorkChangReceiver;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppUtil {

    //向SP中存入数据
    public static void saveSP (String Name, String Value) {
        SharedPreferences.Editor editor = OkHttpApplication.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        editor.putString(Name, Value);
        editor.commit();
    }

    //从SP中获取数据
    public static String readSP (String Name) {
        SharedPreferences sp = OkHttpApplication.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
        return sp.getString(Name, "");
    }

    //显示Toast(防连点)
    private static Toast toast;
    public static void showToast (String toastInfo, int duration) {
        if (toast == null) {
            if (duration == 0) {
                toast = Toast.makeText(OkHttpApplication.getInstance(), toastInfo, Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(OkHttpApplication.getInstance(), toastInfo, Toast.LENGTH_LONG);
            }
            toast.show();
        } else {
            toast.setText(toastInfo);
        }
    }

    //显示Log
    public static void showLog (String tagInfo) {
        String contextString = OkHttpApplication.getInstance().toString();
        String activityName = contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
        Log.i(activityName, tagInfo);
    }

    //全局只使用一个AlertDialog
    private static AlertDialog.Builder builder;
    public AlertDialog.Builder getDialog(){
        if(builder == null){
            builder = new AlertDialog.Builder(OkHttpApplication.getInstance());
        }
        return builder;
    }

    //获取当前网络连接
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String md5AddSalt(String string, String salt) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + salt).getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        return datas;
    }

}
