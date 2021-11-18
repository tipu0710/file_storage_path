package com.tsr.file_storage_path;

import android.app.Activity;
import io.flutter.plugin.common.MethodCall;
import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodChannel;

/** FileStoragePathPlugin */
public class FileStoragePathPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "file_storage_path");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
      onDetachedFromActivity();
  }


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivity() {
    channel.setMethodCallHandler(null);
  }


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    String data = StorageManager.getFile(activity, call.method);
    if(data == null){
      result.notImplemented();
    }else{
      result.success(data);
    }
  }
}
