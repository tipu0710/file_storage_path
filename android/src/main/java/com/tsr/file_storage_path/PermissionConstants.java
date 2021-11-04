package com.tsr.file_storage_path;

public class PermissionConstants {
    static final String LOG_TAG = "permission_controller";
    static final int PERMISSION_CODE = 24;
    static final int PERMISSION_CODE_IGNORE_BATTERY_OPTIMIZATIONS = 209;
    static final int PERMISSION_STATUS_GRANTED = 1;
    static final int PERMISSION_STATUS_DENIED = 0;
    static final int PERMISSION_STATUS_RESTRICTED = 2;
    static final int PERMISSION_STATUS_LIMITED = 3;
    static final int PERMISSION_STATUS_NEVER_ASK_AGAIN = 4;
    static final int PERMISSION_GROUP_STORAGE = 15;
    static final int PERMISSION_CODE_MANAGE_EXTERNAL_STORAGE = 210;
    static final int PERMISSION_GROUP_MANAGE_EXTERNAL_STORAGE = 22;
    static final int PERMISSION_GROUP_IGNORE_BATTERY_OPTIMIZATIONS = 16;


    static final int SERVICE_STATUS_DISABLED = 0;
    static final int SERVICE_STATUS_ENABLED = 1;
    static final int SERVICE_STATUS_NOT_APPLICABLE = 2;

    @interface PermissionGroup {
    }

    @interface PermissionStatus {
    }

    @interface ServiceStatus {
    }
}
