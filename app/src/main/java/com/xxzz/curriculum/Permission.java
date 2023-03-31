package com.xxzz.curriculum;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //保存真正需要去申请的权限
    private List<String> permissionList = new ArrayList<>();

    public static int RequestCode = 100;

    public void checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            //有需要去动态申请的权限
            if (permissionList.size() > 0) {
                requestPermission(activity);
            }
        }
    }
    //去申请的权限
    private void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,permissionList.toArray(new String[permissionList.size()]),RequestCode);
    }
}
