package com.tsr.file_storage_path;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodHandler implements MethodChannel.MethodCallHandler{
    private final Context applicationContext;
    private final PermissionManager permissionManager;

    public MethodHandler(Context applicationContext, PermissionManager permissionManager) {
        this.applicationContext = applicationContext;
        this.permissionManager = permissionManager;
    }

    @Nullable
    private Activity activity;

    public void setActivity(@Nullable Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        permissionManager.requestPermissions(
                activity,
                call.method,
                result::success,
                (String errorCode, String errorDescription) -> result.error(
                        errorCode,
                        errorDescription,
                        null
                )
        );
    }
}
