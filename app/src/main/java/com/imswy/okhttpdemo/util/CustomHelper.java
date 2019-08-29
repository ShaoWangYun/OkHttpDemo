package com.imswy.okhttpdemo.util;

import android.net.Uri;
import android.os.Environment;
import android.view.View;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

public class CustomHelper {
    private View rootView;

    public static CustomHelper of(View rootView) {
        return new CustomHelper(rootView);
    }

    private CustomHelper (View rootView) {
        this.rootView = rootView;
    }

    public void onClick(String type, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (type) {
            case "takephoto":
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            case "selectphoto":
                int limit = 1;
                if (limit > 1) {
                    takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                    return;
                }
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 10240;//大小不超过
        int width = 300;//大小不超过多宽
        int height = 300;//大小不超过多高
        boolean showProgressBar = true;//是否显示压缩进度条
        boolean enableRawFile = true;//压缩后是否保存原图
        CompressConfig config;
        //压缩工具：自带
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        takePhoto.onEnableCompress(config, showProgressBar);
    }

    private CropOptions getCropOptions() {
        int height = 300;//高
        int width = 300;//宽
        boolean withWonCrop = true;//压缩工具是否为第三方
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
