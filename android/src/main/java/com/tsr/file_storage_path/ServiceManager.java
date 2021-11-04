package com.tsr.file_storage_path;

import static android.content.Context.BLUETOOTH_SERVICE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

public class ServiceManager {
    @FunctionalInterface
    interface SuccessCallback {
        void onSuccess(@PermissionConstants.ServiceStatus int serviceStatus);
    }

    void checkServiceStatus(
            int permission,
            Context context,
            SuccessCallback successCallback,
            ErrorCallback errorCallback) {
        if(context == null) {
            Log.d(PermissionConstants.LOG_TAG, "Context cannot be null.");
            errorCallback.onError("PermissionHandler.ServiceManager", "Android context cannot be null.");
            return;
        }

        if (permission == PermissionConstants.PERMISSION_GROUP_IGNORE_BATTERY_OPTIMIZATIONS) {
            final int serviceStatus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    ? PermissionConstants.SERVICE_STATUS_ENABLED
                    : PermissionConstants.SERVICE_STATUS_NOT_APPLICABLE;
            successCallback.onSuccess(serviceStatus);
            return;
        }

        successCallback.onSuccess(PermissionConstants.SERVICE_STATUS_NOT_APPLICABLE);
    }
}
