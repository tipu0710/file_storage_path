package com.tsr.file_storage_path;

@FunctionalInterface
interface ErrorCallback {
    void onError(String errorCode, String errorDescription);
}
