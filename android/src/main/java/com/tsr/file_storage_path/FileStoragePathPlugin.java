package com.tsr.file_storage_path;


import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

/** FileStoragePathPlugin */
public class FileStoragePathPlugin implements FlutterPlugin, ActivityAware {
  private MethodChannel channel;
  private final PermissionManager permissionManager = new PermissionManager();

  @Nullable
  private MethodHandler methodCallHandler;

  @Nullable
  private ActivityPluginBinding pluginBinding;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    startListening(
            flutterPluginBinding.getApplicationContext(),
            flutterPluginBinding.getBinaryMessenger()
    );
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    stopListening();
  }


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    startListeningToActivity(
            binding.getActivity()
    );

    this.pluginBinding = binding;
    registerListeners();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivity() {
    stopListeningToActivity();

    deregisterListeners();
  }

  private void startListening(Context applicationContext, BinaryMessenger messenger) {
    channel = new MethodChannel(messenger, "file_storage_path");

    methodCallHandler = new MethodHandler(
            applicationContext,
            this.permissionManager);

    channel.setMethodCallHandler(methodCallHandler);
  }

  private void stopListening() {
    channel.setMethodCallHandler(null);
    channel = null;
    methodCallHandler = null;
  }

  private void startListeningToActivity(Activity activity) {
    if (methodCallHandler != null) {
      methodCallHandler.setActivity(activity);
    }
  }

  private void stopListeningToActivity() {
    if (methodCallHandler != null) {
      methodCallHandler.setActivity(null);
    }
  }

  private void registerListeners() {
    if (pluginBinding != null) {
      this.pluginBinding.addActivityResultListener(this.permissionManager);
      this.pluginBinding.addRequestPermissionsResultListener(this.permissionManager);
    }
  }

  private void deregisterListeners() {
    if (this.pluginBinding != null) {
      this.pluginBinding.removeActivityResultListener(this.permissionManager);
      this.pluginBinding.removeRequestPermissionsResultListener(this.permissionManager);
    }
  }
}
