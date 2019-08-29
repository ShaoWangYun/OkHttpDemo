package com.imswy.okhttpdemo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imswy.okhttpdemo.R;
import com.imswy.okhttpdemo.bean.ServerResponse;
import com.imswy.okhttpdemo.bean.User;
import com.imswy.okhttpdemo.util.AppUtil;
import com.imswy.okhttpdemo.util.Constant;
import com.imswy.okhttpdemo.widget.ProgressBarDialog;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.imswy.okhttpdemo.util.Constant.Success;
import static com.imswy.okhttpdemo.util.Constant.UserHaveExit;

public class LoginActivity extends Activity {

    //View
    private EditText etAccount, etPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private ImageView imgShowPassword;
    private CheckBox cbRememberPassword;
    private Dialog progressDialog;

    //Value
    private boolean isActivityCheckboxChecked = false;
    private boolean isShowPassword = false;
    private ServerResponse serverResponse;
    private Gson gson = new Gson();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    private void initView () {
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);
        imgShowPassword = findViewById(R.id.img_showPassword);
        cbRememberPassword = findViewById(R.id.cb_rememberAccount);

        if (AppUtil.readSP("IsSPCheckboxChecked").equals("true")) {
            cbRememberPassword.setChecked(true);
            etAccount.setText(AppUtil.readSP("SaveAccount"));
        } else {
            cbRememberPassword.setChecked(false);
            etAccount.setText("");
        }

    }

    private void initEvent () {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (null != account && !account.equals("") && null != password && !password.equals("")) {
                    if (isActivityCheckboxChecked) {
                        AppUtil.saveSP("IsSPCheckboxChecked", "true");
                        AppUtil.saveSP("SaveAccount", account);
                    } else {
                        AppUtil.saveSP("IsSPCheckboxChecked", "false");
                        AppUtil.saveSP("SaveAccount", "");
                    }
                    new CheckTask().execute();
                } else {
                    AppUtil.showToast("请输入完整的账户信息", 0);
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        imgShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (isShowPassword) {
                    //如果当前密码是显示状态，点击时将设置密码不可见，并且源图片变为“隐藏密码”状态图
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    imgShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.hidepassword));
                    isShowPassword = false;
                } else {
                    //如果当前密码是隐藏状态，点击时将设置密码可见，并且原图片变为“显示密码”状态图
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imgShowPassword.setImageDrawable(getResources().getDrawable(R.drawable.showwpassword));
                    isShowPassword = true;
                }
            }
        });

        cbRememberPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isActivityCheckboxChecked = true;
                } else {
                    isActivityCheckboxChecked = false;
                }
            }
        });
    }

    //校验用户的异步任务
    private class CheckTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute () {
            progressDialog = ProgressBarDialog.createLoadingDialog(LoginActivity.this, "正在校验账户");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... params) {
            if (doCheck()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if (result) {
                new LoginTask().execute();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if(null!=serverResponse){
                AppUtil.showToast(serverResponse.getResponseInfo(), 0);
            }
        }
    }

    private boolean doCheck () {
        try {
            String account = etAccount.getText().toString().trim();
            OkHttpClient client = new OkHttpClient();
            User user = new User();
            user.setNote("check");
            user.setAccount(account);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), gson.toJson(user));
            final Request request = new Request.Builder().url(Constant.IpAddress + "/LoginServlet").post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                serverResponse = gson.fromJson(response.body().string(), ServerResponse.class);
                if (serverResponse.getResponseCode() == UserHaveExit) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //用户登录的异步任务
    private class LoginTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute () {
            progressDialog = ProgressBarDialog.createLoadingDialog(LoginActivity.this, "正在登录");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground (Void... params) {
            if (doLogin()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if(result){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if(null!=serverResponse){
                AppUtil.showToast(serverResponse.getResponseInfo(), 0);
            }
        }
    }

    private boolean doLogin () {
        try {
            OkHttpClient client = new OkHttpClient(); //RequestBody中的MediaType指定为纯文本，编码方式是utf-8
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            User user = new User();
            user.setNote("login");
            user.setAccount(account);
            user.setPassword(AppUtil.md5AddSalt(password, account));
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), gson.toJson(user));
            final Request request = new Request.Builder().url(Constant.IpAddress + "/LoginServlet").post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                serverResponse = gson.fromJson(response.body().string(), ServerResponse.class);
                if (serverResponse.getResponseCode() == Success) {
                    AppUtil.saveSP("CurrentUserAccount_Login", serverResponse.getUser().getAccount());
                    AppUtil.saveSP("CurrentUserIconUrl_Login", serverResponse.getUser().getIconUrl());
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
