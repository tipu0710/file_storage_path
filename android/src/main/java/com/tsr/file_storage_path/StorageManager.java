package com.tsr.file_storage_path;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageManager {

    static String getFile(@NonNull Activity activity, @NonNull String type){
        switch (type) {
            case "getImagesPath":
                return getImagePaths(activity);
            case "getVideosPath":
                return getVideoPath(activity);
            case "getAudioPath":
                return getAudioPath(activity);
            case "getFilesPath":
                return getFilesPath(activity);
            default:
                return null;
        }
    }

    private static String getImagePaths(@NonNull Activity activity) {
        ArrayList<FileModel> filesModelArrayList = new ArrayList<>();
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
        return json;
    }

    private static String getVideoPath(@NonNull Activity activity) {

        ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>();
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

            ArrayList<MetaData> metaDataArrayList = new ArrayList<>();
            if (hasFolder) {
                metaDataArrayList.addAll(mediaModelArrayList.get(position).getFiles());
                metaDataArrayList.add(metaData);

                mediaModelArrayList.get(position).setFiles(metaDataArrayList);
            } else {
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
        cursor.close();
        System.out.println(json);
        return json;
    }

    private static String getAudioPath(@NonNull Activity activity) {

        ArrayList<MediaModel> mediaModelArrayList = new ArrayList<>();
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
        return json;
    }

    private static String getFilesPath(@NonNull Activity activity) {
        ArrayList<DocumentModel> fileModelArrayList = new ArrayList<>();
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
        return json;
    }
}
