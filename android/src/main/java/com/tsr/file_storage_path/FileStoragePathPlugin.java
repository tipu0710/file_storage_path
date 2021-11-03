package com.tsr.file_storage_path;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FileStoragePathPlugin */
public class FileStoragePathPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  public static ArrayList<FileModel> filesModelArrayList;
  public static ArrayList<MediaModel> mediaModelArrayList;
  public static ArrayList<DocumentModel> fileModelArrayList;
  Context context;
  Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context  = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "file_storage_path");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if( ContextCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
      switch (call.method) {
        case "getImagesPath":
          getImagePaths(result);
          break;
        case "getVideosPath":
          getVideoPath(result);
          break;
        case "getAudioPath":
          getAudioPath(result);
          break;
        case "getFilesPath":
          getFilesPath(result);
          break;
        default:
          result.notImplemented();
          break;
      }
    }else{
      result.error("Permission",
              "Storage permission denied!",
              "Please accept storage permission from settings!");
    }

  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivity() {

  }

  private void getImagePaths(Result result) {
    filesModelArrayList = new ArrayList<>();
    boolean hasFolder = false;
    int position = 0;
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;

    String absolutePathOfImage = null;
    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = {
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);

      for (int i = 0; i < filesModelArrayList.size(); i++) {
        if (filesModelArrayList.get(i) != null &&
                filesModelArrayList.get(i).getFolder() != null &&
                filesModelArrayList.get(i).getFolder().equals(cursor.getString(column_index_folder_name))) {
          hasFolder = true;
          position = i;
          break;
        } else {
          hasFolder = false;
        }
      }


      ArrayList<String> arrayList = new ArrayList<>();
      if (hasFolder) {


        arrayList.addAll(filesModelArrayList.get(position).getFiles());
        arrayList.add(absolutePathOfImage);
        filesModelArrayList.get(position).setFiles(arrayList);

      } else {
        arrayList.add(absolutePathOfImage);
        FileModel obj_model = new FileModel();
        obj_model.setFolder(cursor.getString(column_index_folder_name));
        obj_model.setFiles(arrayList);

        filesModelArrayList.add(obj_model);

      }

    }
    Gson gson = new GsonBuilder().create();
    Type listType = new TypeToken<ArrayList<FileModel>>() {
    }.getType();
    String json = gson.toJson(filesModelArrayList, listType);
    if (cursor != null) {
      cursor.close();
    }
    result.success(json);
  }

  private void getVideoPath(Result result) {

    mediaModelArrayList = new ArrayList<>();
    boolean hasFolder = false;
    int position = 0;
    Uri uri;
    Cursor cursor;
    int column_index_data;

    String absolutePathOfImage;
    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    String[] projection = {
            MediaStore.Video.VideoColumns.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.ARTIST,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION
    };

    final String orderBy = MediaStore.Video.Media.DATE_ADDED;
    cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);
      for (int i = 0; i < mediaModelArrayList.size(); i++) {
        if (mediaModelArrayList.get(i) != null &&
                mediaModelArrayList.get(i).getFolder() != null &&
                mediaModelArrayList.get(i).getFolder().equals(new File(absolutePathOfImage).getParentFile().getName())) {
          hasFolder = true;
          position = i;
          break;
        } else {
          hasFolder = false;
        }
      }

      MetaData metaData = new MetaData();
      metaData.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)));
      metaData.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATA)));
      metaData.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.ALBUM)));
      metaData.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.ARTIST)));
      metaData.setDateAdded(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_ADDED)));
      metaData.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.SIZE)));
      metaData.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DISPLAY_NAME)));

      if (hasFolder) {

        ArrayList<MetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.addAll(mediaModelArrayList.get(position).getFiles());
        metaDataArrayList.add(metaData);

        mediaModelArrayList.get(position).setFiles(metaDataArrayList);

      } else {

        ArrayList<MetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.add(metaData);
        MediaModel audioModel = new MediaModel();
        audioModel.setFolder(new File(absolutePathOfImage).getParentFile().getName());
        audioModel.setFiles(metaDataArrayList);
        mediaModelArrayList.add(audioModel);

      }

    }
    Gson gson = new GsonBuilder().create();
    Type listType = new TypeToken<ArrayList<MediaModel>>() {
    }.getType();
    String json = gson.toJson(mediaModelArrayList, listType);
    if (cursor != null) {
      cursor.close();
    }

    result.success(json);
  }

  private void getAudioPath(Result result) {

    mediaModelArrayList = new ArrayList<>();
    boolean hasFolder = false;
    int position = 0;
    Uri uri;
    Cursor cursor;
    int column_index_data;

    String absolutePathOfImage;
    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    String[] projection = {
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION
    };

    final String orderBy = MediaStore.Audio.Media.DATE_ADDED;
    cursor = activity.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);
      for (int i = 0; i < mediaModelArrayList.size(); i++) {
        if (mediaModelArrayList.get(i) != null &&
                mediaModelArrayList.get(i).getFolder() != null &&
                mediaModelArrayList.get(i).getFolder().equals(new File(absolutePathOfImage).getParentFile().getName())) {
          hasFolder = true;
          position = i;
          break;
        } else {
          hasFolder = false;
        }
      }

      MetaData metaData = new MetaData();
      metaData.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)));
      metaData.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)));
      metaData.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)));
      metaData.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)));
      metaData.setDateAdded(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_ADDED)));
      metaData.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.SIZE)));
      metaData.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));

      if (hasFolder) {

        ArrayList<MetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.addAll(mediaModelArrayList.get(position).getFiles());
        metaDataArrayList.add(metaData);

        mediaModelArrayList.get(position).setFiles(metaDataArrayList);

      } else {

        ArrayList<MetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.add(metaData);
        MediaModel audioModel = new MediaModel();
        audioModel.setFolder(new File(absolutePathOfImage).getParentFile().getName());
        audioModel.setFiles(metaDataArrayList);
        mediaModelArrayList.add(audioModel);

      }

    }
    Gson gson = new GsonBuilder().create();
    Type listType = new TypeToken<ArrayList<MediaModel>>() {
    }.getType();
    String json = gson.toJson(mediaModelArrayList, listType);
    if (cursor != null) {
      cursor.close();
    }
    result.success(json);
  }

  private void getFilesPath(Result result) {
    fileModelArrayList = new ArrayList<>();
    boolean hasFolder = false;
    int position = 0;
    Uri uri;
    Cursor cursor;
    int column_index_data;

    String absolutePathOfImage;
    uri = MediaStore.Files.getContentUri("external");

    String[] projection = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.TITLE
    };
    String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
    String doc = MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc");
    String docx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx");
    String xls = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls");
    String xlsx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx");
    String ppt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ppt");
    String pptx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pptx");
    String txt = MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt");
    String rtx = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtx");
    String rtf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("rtf");
    String html = MimeTypeMap.getSingleton().getMimeTypeFromExtension("html");
    String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?"
            + " OR " + MediaStore.Files.FileColumns.MIME_TYPE + "=?";
    String[] args = new String[]{pdf, doc, docx, xls, xlsx, ppt, pptx, txt, rtx, rtf, html};
    final String orderBy = MediaStore.Files.FileColumns.DATE_ADDED;
    cursor = activity.getContentResolver().query(uri, projection, where, args, orderBy + " DESC");

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);
      for (int i = 0; i < fileModelArrayList.size(); i++) {
        if (fileModelArrayList.get(i) != null &&
                fileModelArrayList.get(i).getFolderName() != null &&
                fileModelArrayList.get(i).getFolderName().equals(new File(absolutePathOfImage).getParentFile().getName())) {
          hasFolder = true;
          position = i;
          break;
        } else {
          hasFolder = false;
        }
      }

      FileMetaData metaData = new FileMetaData();
      metaData.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)));
      metaData.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)));
      metaData.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)));
      metaData.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)));
      metaData.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)));

      if (hasFolder) {

        ArrayList<FileMetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.addAll(fileModelArrayList.get(position).getFileMetaData());
        metaDataArrayList.add(metaData);

        fileModelArrayList.get(position).setFileMetaData(metaDataArrayList);

      } else {

        ArrayList<FileMetaData> metaDataArrayList = new ArrayList<>();
        metaDataArrayList.add(metaData);

        DocumentModel fileModel = new DocumentModel();
        fileModel.setFolderName(new File(absolutePathOfImage).getParentFile().getName());
        fileModel.setFileMetaData(metaDataArrayList);
        fileModelArrayList.add(fileModel);

      }

    }
    Gson gson = new GsonBuilder().create();
    Type listType = new TypeToken<ArrayList<FileModel>>() {
    }.getType();
    String json = gson.toJson(fileModelArrayList, listType);
    if (cursor != null) {
      cursor.close();
    }
    result.success(json);
  }
}
