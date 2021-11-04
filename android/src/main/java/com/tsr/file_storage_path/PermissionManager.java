package com.tsr.file_storage_path;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import io.flutter.plugin.common.PluginRegistry;

public class PermissionManager implements PluginRegistry.ActivityResultListener,
        PluginRegistry.RequestPermissionsResultListener{
    @Nullable
    private Activity activity;

    @Nullable
    private String type;

    @Nullable
    private RequestPermissionsSuccessCallback successCallback;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1) {
            return false;
        }

        if(resultCode == Activity.RESULT_OK){
            setResult();
        }

        return true;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return false;
    }

    @FunctionalInterface
    interface RequestPermissionsSuccessCallback {
        void onSuccess(String results);
    }

    private void setResult(){
        String result = StorageManager.getFile(activity, type);
        Log.d(PermissionConstants.LOG_TAG, result);
        successCallback.onSuccess(result);
    }

    private boolean ongoing = false;

    void requestPermissions(
            Activity activity,
            String type,
            RequestPermissionsSuccessCallback successCallback,
            ErrorCallback errorCallback) {
        if (ongoing) {
            errorCallback.onError(
                    "FileStoragePath.PermissionManager",
                    "A request for permissions is already running, please wait for it to finish before doing another request (note that you can request multiple permissions at the same time).");
            return;
        }

        if (activity == null) {
            Log.d(PermissionConstants.LOG_TAG, "Unable to detect current Activity.");

            errorCallback.onError(
                    "FileStoragePath.PermissionManager",
                    "Unable to detect current Android Activity.");
            return;
        }

        this.type = type;
        this.successCallback = successCallback;
        this.activity = activity;

        ArrayList<String> permissionsToRequest = new ArrayList<>();
        @PermissionConstants.PermissionStatus final int permissionStatus = determinePermissionStatus(activity);
        Log.d(PermissionConstants.LOG_TAG, "Status code" +permissionStatus);
        if (permissionStatus == PermissionConstants.PERMISSION_STATUS_GRANTED) {
            Log.d(PermissionConstants.LOG_TAG, type);
            setResult();
            return;
        }else{
            Log.d(PermissionConstants.LOG_TAG, "Not granted");
        }

        final List<String> names = new ArrayList<>();
        names.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            executeIntent(
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    PermissionConstants.PERMISSION_CODE_MANAGE_EXTERNAL_STORAGE);
        } else {
            permissionsToRequest.addAll(names);
        }

        final String[] requestPermissions = permissionsToRequest.toArray(new String[0]);
        if (permissionsToRequest.size() > 0) {
            ongoing = true;

            ActivityCompat.requestPermissions(
                    activity,
                    requestPermissions,
                    PermissionConstants.PERMISSION_CODE);
        } else {
            ongoing = false;
            setResult();
        }
    }

    @PermissionConstants.PermissionStatus
    private int determinePermissionStatus(
            Context context) {

        final List<String> names = new ArrayList<>();
        names.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        final boolean targetsMOrHigher = context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M;
        Log.d(PermissionConstants.LOG_TAG, "SDK version: " + Build.VERSION.SDK_INT);
        if (targetsMOrHigher) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return Environment.isExternalStorageManager()
                        ? PermissionConstants.PERMISSION_STATUS_GRANTED
                        : PermissionConstants.PERMISSION_STATUS_DENIED;
            }
        }
        return PermissionConstants.PERMISSION_STATUS_GRANTED;
    }

    private void executeIntent(String action, int requestCode) {
        String packageName = activity.getPackageName();
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(Uri.parse("package:" + packageName));
        activity.startActivityForResult(intent, requestCode);
    }

    private void executeSimpleIntent(String action, int requestCode) {
        activity.startActivityForResult(new Intent(action), requestCode);
    }


}
