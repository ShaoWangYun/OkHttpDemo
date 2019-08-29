package com.imswy.okhttpdemo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.imswy.okhttpdemo.R;
import com.imswy.okhttpdemo.bean.ServerResponse;
import com.imswy.okhttpdemo.bean.User;
import com.imswy.okhttpdemo.util.AppUtil;
import com.imswy.okhttpdemo.util.Constant;
import com.imswy.okhttpdemo.util.CustomHelper;
import com.imswy.okhttpdemo.widget.Dialogchoosephoto;
import com.imswy.okhttpdemo.widget.ProgressBarDialog;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TResult;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.imswy.okhttpdemo.util.Constant.Success;
import static com.imswy.okhttpdemo.util.Constant.UserNotFound;

public class RegisterActivity extends TakePhotoActivity {

    //View
    private ImageView imgBack, imgUploadIcon, imgShowPassword, imgShowPasswordRepeat;
    private EditText etAccount, etPassword, etPasswordRepeat;
    private Button btnRegister;
    private Dialog progressDialog_check,progressDialog_register;
    private View contentView;

    //Value
    private boolean isShowPassword = false;
    private boolean isShowPasswordRepeat = false;
    private ServerResponse serverResponse;
    private Gson gson = new Gson();
    private CustomHelper customHelper;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = LayoutInflater.from(this).inflate(R.layout.activity_register, null);
        setContentView(contentView);
        initView();
        initEvent();
    }

    private void initView () {
        imgBack = findViewById(R.id.img_back);
        imgUploadIcon = findViewById(R.id.img_uploadIcon);
        imgShowPassword = findViewById(R.id.img_showPassword);
        imgShowPasswordRepeat = findViewById(R.id.img_showPasswordRepeat);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etPasswordRepeat = findViewById(R.id.et_password_repeat);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void initEvent () {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });

        customHelper = CustomHelper.of(contentView);
        imgUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                new Dialogchoosephoto(RegisterActivity.this) {
                    @Override
                    public void btnPickByTake () {
                        //拍照
                        customHelper.onClick("takephoto", getTakePhoto());
                    }

                    @Override
                    public void btnPickBySelect () {
                        //相册
                        customHelper.onClick("selectphoto", getTakePhoto());
                    }

                }.show();
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

        imgShowPasswordRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (isShowPasswordRepeat) {
                    //如果当前密码是显示状态，点击时将设置密码不可见，并且源图片变为“隐藏密码”状态图
                    etPasswordRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    imgShowPasswordRepeat.setImageDrawable(getResources().getDrawable(R.drawable.hidepassword));
                    isShowPasswordRepeat = false;
                } else {
                    //如果当前密码是隐藏状态，点击时将设置密码可见，并且原图片变为“显示密码”状态图
                    etPasswordRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imgShowPasswordRepeat.setImageDrawable(getResources().getDrawable(R.drawable.showwpassword));
                    isShowPasswordRepeat = true;
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String passwordrepeat = etPasswordRepeat.getText().toString().trim();
                if (null != account && !account.equals("") && null != password && !password.equals("") && null != passwordrepeat && !etPasswordRepeat.equals("")) {
                    if (password.equals(passwordrepeat)) {
                        new CheckTask().execute();
                    } else {
                        AppUtil.showToast("两次输入密码不一致，请重新输入", 0);
                        //为了方便同时保证安全，仅仅显示错误的重复输入密码
                        if (!isShowPasswordRepeat) {
                            imgShowPasswordRepeat.performClick();
                        }
                    }
                } else {
                    AppUtil.showToast("请输入完整的注册信息", 0);
                }
            }
        });
    }

    @Override
    public void takeCancel () {
        super.takeCancel();
    }

    @Override
    public void takeFail (TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess (TResult result) {
        super.takeSuccess(result);
        for (int i = 0, j = result.getImages().size(); i < j - 1; i += 2) {
            Glide.with(this).load(new File(result.getImages().get(i).getCompressPath())).into(imgUploadIcon);
            Glide.with(this).load(new File(result.getImages().get(i + 1).getCompressPath())).into(imgUploadIcon);
        }
        if (result.getImages().size() % 2 == 1) {
            Glide.with(this).load(new File(result.getImages().get(result.getImages().size() - 1).getCompressPath())).into(imgUploadIcon);
        }
    }

    //校验用户的异步任务
    private class CheckTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute () {
            progressDialog_check = ProgressBarDialog.createLoadingDialog(RegisterActivity.this, "正在校验账户");
            progressDialog_check.setCancelable(false);
            progressDialog_check.show();
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
                new RegisterTask().execute();
            }
            if (progressDialog_check != null) {
                progressDialog_check.dismiss();
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
            final Request request = new Request.Builder().url(Constant.IpAddress + "/RegisterServlet").post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                serverResponse = gson.fromJson(response.body().string(), ServerResponse.class);
                AppUtil.showLog("Check serverResponse is : "+serverResponse.toString());
                if (serverResponse.getResponseCode() == UserNotFound) {
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

    //用户注册的异步任务
    private class RegisterTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute () {
            progressDialog_register = ProgressBarDialog.createLoadingDialog(RegisterActivity.this, "正在注册");
            progressDialog_register.setCancelable(false);
            progressDialog_register.show();
        }

        @Override
        protected Boolean doInBackground (Void... params) {
            if (doRegister()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute (Boolean result) {
            if (result) {
                finish();
            }
            if (progressDialog_register != null) {
                progressDialog_register.dismiss();
            }
            if(null!=serverResponse){
                AppUtil.showToast(serverResponse.getResponseInfo(), 0);
            }
        }
    }

    private boolean doRegister () {
        try {
            OkHttpClient client = new OkHttpClient(); //RequestBody中的MediaType指定为纯文本，编码方式是utf-8
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            User user = new User();
            user.setNote("register");
            Bitmap bitmap = ((BitmapDrawable)imgUploadIcon.getDrawable()).getBitmap();
            user.setIcon(AppUtil.Bitmap2Bytes(bitmap));
            user.setAccount(account);
            user.setPassword(AppUtil.md5AddSalt(password, account));
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), gson.toJson(user));
            final Request request = new Request.Builder().url(Constant.IpAddress + "/RegisterServlet").post(requestBody).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                serverResponse = gson.fromJson(response.body().string(), ServerResponse.class);
                AppUtil.showLog("Register serverResponse is : "+serverResponse.toString());
                if (serverResponse.getResponseCode() == Success) {
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
}
